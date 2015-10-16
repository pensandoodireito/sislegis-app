package br.gov.mj.sislegis.app.parser.senado;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.senado.xstream.DetalheMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListMateriaClass;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListaSubtiposMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materia;
import br.gov.mj.sislegis.app.parser.senado.xstream.PesquisaBasicaMateria;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;

public class ParserProposicaoSenado implements ProposicaoSearcher {

	public static void main(String[] args) throws Exception {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		Long idProposicao = 24257L; // Informação que vem do filtro
		System.out.println(parser.getProposicao(idProposicao).toString());
		System.out.println(parser.listaTipos());
		System.out.println(parser.searchProposicao("pls", null, 2013));
	}

	public Proposicao getProposicao(Long idProposicao) throws Exception {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/" + idProposicao + "?v=3";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		DetalheMateria detalheMateria = new DetalheMateria();

		DetalheMateria.configXstream(xstream);
		ParserFetcher.fetchXStream(wsURL, xstream, detalheMateria);

		Proposicao proposicao = new Proposicao();

		proposicao = detalheMateria.getProposicao();
		if (proposicao == null) {
			proposicao = new Proposicao();
		}
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
				+ proposicao.getIdProposicao());

		return proposicao;
	}

	@Override
	public List<TipoProposicao> listaTipos() throws IOException {

		String wsUrl = "http://legis.senado.leg.br/dadosabertos/materia/subtipos";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		ListaSubtiposMateria list = new ListaSubtiposMateria();
		ListaSubtiposMateria.configXstream(xstream);

		ParserFetcher.fetchXStream(wsUrl, xstream, list);
		return list.getTiposProposicao();
	}

	/**
	 * Retorna uma busca de proposições.<br>
	 * Documentação do serviço aqui:<br>
	 * http://legis.senado.leg.br/dadosabertos/docs/path__materia_pesquisa_lista
	 * .html
	 * 
	 * @return
	 * @throws IOException
	 */
	public Collection<Proposicao> searchProposicao(String sigla, Integer numero, Integer ano) throws IOException {
		StringBuilder wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/materia/pesquisa/lista?");
		wsURL.append("v=4");
		wsURL.append("&sigla=").append(URLEncoder.encode(sigla, "UTF-8"));
		if (numero != null) {
			wsURL.append("&numero=").append(numero);
		}
		wsURL.append("&ano=").append(ano);

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		PesquisaBasicaMateria pesquisaMateria = new PesquisaBasicaMateria();
		PesquisaBasicaMateria.configXstream(xstream);

		ParserFetcher.fetchXStream(wsURL.toString(), xstream, pesquisaMateria);
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE,
				"Descricao do data set retornado:  '" + pesquisaMateria.getDescricaoResposta() + "'");

		List<Materia> listMaterias = pesquisaMateria.getMaterias();
		Collection<Proposicao> listProposicao = new ListMateriaClass(listMaterias);

		return listProposicao;
	}
}
