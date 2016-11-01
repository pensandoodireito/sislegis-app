package br.gov.mj.sislegis.app.parser.senado;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.senado.xstream.DetalheMateriaV4;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListMateriaClass;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListaSubtiposMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materia;
import br.gov.mj.sislegis.app.parser.senado.xstream.MovimentacaoMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.PesquisaBasicaMateria;
import br.gov.mj.sislegis.app.parser.senado.xstream.Tramitacao;
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
 * <li>situacao -
 * DetalheMateria/Materia/SituacaoAtual/Autuacoes/Autuacao/Situacao
 * /SiglaSituacao</li>
 * <li>comissao -
 * DetalheMateria/Materia/SituacaoAtual/Autuacoes/Autuacao/Local/SiglaLocal</li>
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
		//
		// Collection<Proposicao> searchProps = parser.searchProposicao("PLC",
		// "30", 2015);
		// System.out.println("Busca retornou " + searchProps.size() +
		// " proposicoes");
		// Proposicao propLista = searchProps.iterator().next();
		Proposicao propGet = parser.getProposicao(106062l);
		System.out.println(propGet.toString());
		// System.out.println(propLista.toString());
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
		String tramitacao = getTramitacao(idProposicao);
		proposicao.setTramitacao(tramitacao);
		return proposicao;
	}

	private String getTramitacao(Long idProposicao) throws IOException {

		try {
			String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/movimentacoes/" + idProposicao + "?v=4";

			XStream xstream = new XStream();
			xstream.ignoreUnknownElements();
			MovimentacaoMateria detalhaMateria = new MovimentacaoMateria();
			MovimentacaoMateria.configXstream(xstream);
			ParserFetcher.fetchXStream(wsURL, xstream, detalhaMateria);
			Materia m = detalhaMateria.getMateria();
			if (m != null) {
				List<Tramitacao> tramitacoes = m.getTramitacoes();
				if (tramitacoes != null) {
//
//					for (Iterator iterator = tramitacoes.iterator(); iterator.hasNext();) {
//						Tramitacao tramitacao = (Tramitacao) iterator.next();
//						System.out.println(tramitacao.getIdentificacaoTramitacao().getOrigemTramitacao());
//						System.out.println("txt: " + tramitacao.getIdentificacaoTramitacao().getTextoTramitacao());
//					}
					Tramitacao t = null;
					for (int i = tramitacoes.size() - 1; i >= 0; i--) {
						Tramitacao tramitacao = tramitacoes.get(i);
						if(tramitacao.getIdentificacaoTramitacao().getTextoTramitacao().toLowerCase().contains("à comissão ")){
							return tramitacao.getIdentificacaoTramitacao().getTextoTramitacao();
						}
					}
					
//					System.out.println(idProposicao+":"+t.getIdentificacaoTramitacao().getTextoTramitacao());
				}
//				System.out.println("-----");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
	public Collection<Proposicao> searchProposicao(String sigla, String numero, Integer ano) throws IOException {
		StringBuilder wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/materia/pesquisa/lista?");
		wsURL.append("v=4");
		wsURL.append("&sigla=").append(URLEncoder.encode(sigla, "UTF-8"));
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
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Descricao do data set retornado:  '" + pesquisaMateria.getDescricaoResposta() + "'");

			List<Materia> listMaterias = pesquisaMateria.getMaterias();
			Collection<Proposicao> listProposicao = new ListMateriaClass(listMaterias);

			return listProposicao;
		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao procurar por proposicao. Params " + sigla + "," + numero + "," + ano + ". URL " + wsURL.toString(), e);
			return null;
		}
	}
}
