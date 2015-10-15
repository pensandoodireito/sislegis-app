package br.gov.mj.sislegis.app.parser.senado;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.senado.xstream.DetalheMateriaV4;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListMateriaClass;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListaSubtiposMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materia;
import br.gov.mj.sislegis.app.parser.senado.xstream.PesquisaBasicaMateria;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;
/**
 * Obtem dados de proposicao dos webservices da Senado: <br>
 * O principal webservice é o ListarProposicao cuja url é:
 * 
 * <pre>
 * http://legis.senado.leg.br/dadosabertos/materia/113361?v=4
 * </pre>
 * 
 * <br>
 * O mapeamento dos campos da proposicao e do webservices são:<br>
 * <ul>
 * <li>CAMPO - XPATH</li>
 * <li>idProposicao - DetalheMateria/Materia/IdentificacaoMateria/CodigoMateria</li>
 * <li>tipo - DetalheMateria/Materia/IdentificacaoMateria/SiglaSubtipoMateria</li>
 * <li>ano - DetalheMateria/Materia/IdentificacaoMateria/AnoMateria</li>
 * <li>numero - DetalheMateria/Materia/IdentificacaoMateria/NumeroMateria</li>
 * <li>situacao - DetalheMateria/Materia/SituacaoAtual/Autuacoes/Autuacao/Situacao/SiglaSituacao</li>
 * <li>comissao - DetalheMateria/Materia/SituacaoAtual/Autuacoes/Autuacao/Local/SiglaLocal</li>
 * <li>autor - DetalheMateria/Materia/Autoria/Autor/NomeAutor</li>
 * <li>ementa - DetalheMateria/Materia/DadosBasicosMateria/EmentaMateria</li>
 * <li>linkProposicao - DetalheMateria/Materia/id</li>
 * </ul>
 * 
 * Veja o metodo conversor em @see ProposicaoWS
 */
public class ParserProposicaoSenado implements ProposicaoSearcher {

	public static void main(String[] args) throws Exception {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		System.out.println(parser.listaTipos());
		Collection<Proposicao> searchProps = parser.searchProposicao("RQS", 1, 2015);
		System.out.println("Busca retornou " + searchProps.size() + " proposicoes");
		Proposicao propLista = searchProps.iterator().next();
		Proposicao propGet = parser.getProposicao(propLista.getIdProposicao().longValue());
		System.out.println(propGet.toString());
		System.out.println(propLista.toString());
	}

	@Override
	public Proposicao getProposicao(Long idProposicao) throws IOException {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/" + idProposicao + "?v=4";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		DetalheMateriaV4 detalhaMateria = new DetalheMateriaV4();
		DetalheMateriaV4.configXstream(xstream);

		ParserFetcher.fetchXStream(wsURL, xstream, detalhaMateria);

		Proposicao proposicao = new Proposicao();

		proposicao = detalhaMateria.getProposicao();
		if (proposicao == null) {
			throw new IOException("Nao foi possível parsera proposicao");
		}
		

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
		wsURL.append("&sigla=").append(sigla);
		if (numero != null) {
			wsURL.append("&numero=").append(numero);
		}
		wsURL.append("&ano=").append(ano);

		try {
			XStream xstream = new XStream();
			xstream.ignoreUnknownElements();

			PesquisaBasicaMateria pesquisaMateria = new PesquisaBasicaMateria();
			PesquisaBasicaMateria.configXstream(xstream);

			ParserFetcher.fetchXStream(wsURL.toString(), xstream, pesquisaMateria);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
					"Descricao do data set retornado:  '" + pesquisaMateria.getDescricaoResposta() + "'");

			List<Materia> listMaterias = pesquisaMateria.getMaterias();
			Collection<Proposicao> listProposicao = new ListMateriaClass(listMaterias);

			return listProposicao;
		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(
					Level.SEVERE,
					"Falhou ao procurar por proposicao. Params " + sigla + "," + numero + "," + ano + ". URL "
							+ wsURL.toString(), e);
			return null;
		}
	}
}
