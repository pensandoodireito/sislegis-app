package br.gov.mj.sislegis.app.parser.camara;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.camara.xstream.Erro;
import br.gov.mj.sislegis.app.parser.camara.xstream.ListProposicaoLazy;
import br.gov.mj.sislegis.app.parser.camara.xstream.ListaSigla;
import br.gov.mj.sislegis.app.parser.camara.xstream.ObterProposicaoPorID;
import br.gov.mj.sislegis.app.parser.camara.xstream.Proposicoes;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;

/**
 * Obtem dados de proposicao dos webservices da Câmara: <br>
 * O principal webservice é o ListarProposicao cuja url é:
 * 
 * <pre>
 * http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla=CON&numero=1&ano=2015&datApresentacaoIni=&datApresentacaoFim=&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=&v=4
 * </pre>
 * 
 * <br>
 * O mapeamento dos campos da proposicao e do webservices são:<br>
 * <ul>
 * <li>CAMPO - XPATH</li>
 * <li>idProposicao - proposicoes/proposicao/id</li>
 * <li>tipo - proposicoes/proposicao/tipoProposicao/sigla</li>
 * <li>ano - proposicoes/proposicao/ano</li>
 * <li>numero - proposicoes/proposicao/numero</li>
 * <li>situacao - proposicoes/proposicao/situacao/descricao</li>
 * <li>comissao - proposicoes/proposicao/situacao/orgao/siglaOrgaoEstado</li>
 * <li>autor - proposicoes/proposicao/autor1/txtNomeAutor</li>
 * <li>ementa - proposicoes/proposicao/txtEmenta</li>
 * <li>linkProposicao - proposicoes/proposicao/id + link estatico http://www2.camara.leg.br/proposicoesWeb/fichadetramitacao?idProposicao=</li>
 * </ul>
 * 
 * Veja o metodo conversor em @see ProposicaoWS
 */
public class ParserProposicaoCamara implements ProposicaoSearcher {

	public static void main(String[] args) throws Exception {
		ParserProposicaoCamara parser = new ParserProposicaoCamara();
		Long idProposicao = 1197825l; // TODO: Informação que vem do filtro
//		System.out.println(parser.getProposicao(idProposicao).toString());
//		System.out.println(parser.listaTipos());
		Collection<Proposicao> prop = parser.searchProposicao("PL", 5965, 2013);
		for (Iterator iterator = prop.iterator(); iterator.hasNext();) {
			Proposicao proposicaoLista = (Proposicao) iterator.next();
			Proposicao proposicaoId = parser.getProposicao(proposicaoLista.getIdProposicao().longValue());
			System.out.println("Busca '" + proposicaoLista.toString());
			System.out.println("PorId '" + proposicaoId);
			if (!proposicaoId.toString().equals(proposicaoLista.toString())) {
				System.err.println("Proposicoes sao diferntes dependendo do WS usado");
			}

		}

	}

	/**
	 * Busca proposicao da camara por parametros<br>
	 * veja endpoint da camara aqui:<br>
	 * http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx?op=
	 * ListarProposicoes
	 */
	public Collection<Proposicao> searchProposicao(String tipo, Integer numero, Integer ano) throws IOException {
		// http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla=DIS&numero=&ano=2015&datApresentacaoIni=&datApresentacaoFim=&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=
		StringBuilder wsURL = new StringBuilder(
				"http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?");
		wsURL.append("sigla=").append(tipo);
		wsURL.append("&numero=").append(numero);
		wsURL.append("&ano=").append(ano);
		wsURL.append("&datApresentacaoIni=&datApresentacaoFim=&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=&v=4");
		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		Proposicoes proposicoes = new Proposicoes();
		Proposicoes.configXstream(xstream);

		Collection<Proposicao> listProposicao = new ArrayList<Proposicao>();
		try {
			ParserFetcher.fetchXStream(wsURL.toString(), xstream, proposicoes);

			listProposicao = new ListProposicaoLazy(proposicoes.getProposicoes());

		} catch (FileNotFoundException e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "Nenhum resultado encontrado");
		} catch (Exception e) {
			if (e.getMessage().equals("erro")) {

				try {
					Erro erro = new Erro();
					xstream = new XStream();
					xstream.ignoreUnknownElements();
					xstream.processAnnotations(Erro.class);
					ParserFetcher.fetchXStream(wsURL.toString(), xstream, erro);
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(
							Level.SEVERE,
							"WebService retornou erro '" + erro.getDescricao() + "' para URL '" + wsURL.toString()
									+ "'");

				} catch (Exception e1) {
					throw new IOException(e);
				}

			} else {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
						"Falhou na conversão do parser para url " + wsURL.toString(), e);
				throw new IOException(e);
			}
		}

		return listProposicao;
	}

	public Proposicao getProposicao(Long idProposicao) throws IOException {

		// Versao mais precisa usando o search.
		String wsURL = "http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ObterProposicaoPorID?idProp="
				+ idProposicao;

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		ObterProposicaoPorID obterProposicaoWS = new ObterProposicaoPorID();
		ObterProposicaoPorID.config(xstream);
		ParserFetcher.fetchXStream(wsURL, xstream, obterProposicaoWS);
		Proposicao prop = obterProposicaoWS.toProposicao();
		// por algum motivo o search é melhor (possui todos os campos), portanto
		// bucsamos novamente por
		// ele.
		Collection<Proposicao> props = searchProposicao(prop.getTipo(), Integer.parseInt(prop.getNumero()),
				Integer.parseInt(prop.getAno()));
		if (!props.isEmpty() && props.size() == 1) {
			prop = props.iterator().next();
		}

		return prop;
	}

	public List<TipoProposicao> listaTipos() throws IOException {
		String wsUrl = "http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarSiglasTipoProposicao";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		ListaSigla list = new ListaSigla();
		xstream.alias("siglas", ListaSigla.class);
		xstream.alias("sigla", TipoProposicao.class);
		xstream.addImplicitCollection(ListaSigla.class, "siglas");
		xstream.aliasAttribute(TipoProposicao.class, "sigla", "tipoSigla");
		xstream.aliasAttribute(TipoProposicao.class, "nome", "descricao");

		ParserFetcher.fetchXStream(wsUrl, xstream, list);

		return list.getSiglas();
	}
}