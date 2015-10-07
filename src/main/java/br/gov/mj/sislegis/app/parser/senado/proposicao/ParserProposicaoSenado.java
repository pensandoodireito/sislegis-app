package br.gov.mj.sislegis.app.parser.senado.proposicao;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.CollectionLazyConverter;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
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

		config(xstream);
		ParserFetcher.fetchXStream(wsURL, xstream, detalheMateria);

		Proposicao proposicao = new Proposicao();

		proposicao = detalheMateria.getProposicoes().isEmpty() ? proposicao : detalheMateria.getProposicoes().get(0);
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
				+ proposicao.getIdProposicao());

		return proposicao;
	}

	private static void config(XStream xstream) {
		xstream.alias("DetalheMateria", DetalheMateria.class);
		xstream.alias("Materia", Proposicao.class);
		// Conversao de tipos Autoria e Autor
		xstream.alias("Autoria", Autoria.class);
		xstream.alias("Autor", Autor.class);

		xstream.aliasField("Materias", DetalheMateria.class, "proposicoes");

		xstream.aliasField("Codigo", Proposicao.class, "idProposicao");
		xstream.aliasField("Subtipo", Proposicao.class, "tipo");
		xstream.aliasField("Numero", Proposicao.class, "numero");
		xstream.aliasField("Ano", Proposicao.class, "ano");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
		// Forcar o tratamento de autoria como string
		xstream.aliasField("Autoria", Proposicao.class, "autor");
		xstream.registerLocalConverter(Proposicao.class, "autor", new AuthorConverter());

	}

	@Override
	public List<TipoProposicao> listaTipos() throws IOException {

		String wsUrl = "http://legis.senado.leg.br/dadosabertos/materia/subtipos";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		ListaSubtiposMateria list = new ListaSubtiposMateria();
		ListaSubtiposMateria.configXstream(xstream);

		ParserFetcher.fetchXStream(wsUrl, xstream, list);
		return list.SubtiposMateria.subtipos;
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
		wsURL.append("&sigla=").append(sigla);
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
				"Descricao do data set retornado:  '" + pesquisaMateria.descricaoDataSet + "'");

		List<Materia> listMaterias = pesquisaMateria.materias.materias;
		Collection<Proposicao> listProposicao = new ListMateriaClass(listMaterias);

		return listProposicao;
	}
}

class ListMateriaClass extends CollectionLazyConverter<Proposicao, Materia> {

	public ListMateriaClass(List<Materia> materias) {
		super(materias);
	}

	@Override
	protected Proposicao convertKtoE(Materia next) {
		return next.toProposicao();
	}

}
