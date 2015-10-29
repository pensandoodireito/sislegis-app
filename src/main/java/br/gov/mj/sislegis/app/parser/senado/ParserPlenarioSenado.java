package br.gov.mj.sislegis.app.parser.senado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materia;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materias;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ParserPlenarioSenado {

	public static void main(String[] args) throws Exception {
		ParserPlenarioSenado parser = new ParserPlenarioSenado();

		// TODO: Informação que vem do filtro
		String datIni = "20140801";
		System.out.println(parser.getProposicoes(datIni).toString());
	}

	// FIXME tem q converter
	public Set<PautaReuniaoComissao> getProposicoes(String datIni) throws Exception {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Buscando dados da sessão do plenario");
		Set<PautaReuniaoComissao> pautas = new HashSet<PautaReuniaoComissao>();

		XStream xstreamSessao = new XStream();
		xstreamSessao.ignoreUnknownElements();

		AgendaPlenario agendaPlenario = new AgendaPlenario();

		configAgendaPlenario(xstreamSessao);
		String wsURLPlenario = "http://legis.senado.leg.br/dadosabertos/plenario/agenda/mes/" + datIni;
		ParserFetcher.fetchXStream(wsURLPlenario, xstreamSessao, agendaPlenario);
		Comissao plenario = new Comissao();
		plenario.setSigla("PLEN");
		List<Sessao> sessoes = agendaPlenario.getSessoes();
		for (Sessao sessao : sessoes) {
			PautaReuniaoComissao pauta = new PautaReuniaoComissao(sessao.getData(), plenario, sessao.getCodigoSessao());

			pauta.setLinkPauta("https://www25.senado.leg.br/web/atividade/sessao-plenaria/-/pauta/"
					+ sessao.getCodigoSessao());
			pauta.setOrigem(Origem.SENADO);
			pauta.setTipo(sessao.tipo);
			pauta.setSituacao(sessao.situacaoSessao);
			pauta.setTitulo(sessao.numeroSessao);

			if (sessao.materias != null) {

				for (Materia mat : sessao.materias.materias) {
					Proposicao proposicao = mat.toProposicao();
					// TODO: qual comissao?
					proposicao.setComissao("PLEN");
					proposicao.setOrigem(Origem.SENADO);
					proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
							+ proposicao.getIdProposicao());

					ProposicaoPautaComissao propPauta = new ProposicaoPautaComissao(pauta, proposicao);

					propPauta.setOrdemPauta(mat.getSequenciaOrdem());
					propPauta.setRelator(mat.getRelator());

					pauta.addProposicaoPauta(propPauta);

				}
				if (!pauta.getProposicoesDaPauta().isEmpty()) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE,
							"pauta " + pauta + " com " + pauta.getProposicoesDaPauta().size());
					pautas.add(pauta);
				}
			}
		}

		return pautas;
	}

	private void configAgendaPlenario(XStream xstream) {
		xstream.alias("AgendaPlenario", AgendaPlenario.class);
		xstream.processAnnotations(Sessao.class);
		Materias.configXstream(xstream);
		xstream.alias("Materia", Proposicao.class);

		xstream.aliasField("Sessoes", AgendaPlenario.class, "sessoes");
		// xstream.aliasField("Materias", Sessao.class, "materias");

		xstream.aliasField("CodigoMateria", Proposicao.class, "idProposicao");
		xstream.aliasField("SiglaMateria", Proposicao.class, "tipo");
		xstream.aliasField("NumeroMateria", Proposicao.class, "numero");
		xstream.aliasField("AnoMateria", Proposicao.class, "ano");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
		xstream.aliasField("SequenciaOrdem", Proposicao.class, "seqOrdemPauta");
	}

}

class AgendaPlenario {
	protected List<Sessao> sessoes = new ArrayList<Sessao>();

	protected List<Sessao> getSessoes() {
		return sessoes;
	}
}

@XStreamAlias("Sessao")
class Sessao {
	@XStreamAlias("Hora")
	String hora;
	@XStreamAlias("Data")
	String data;
	@XStreamAlias("TipoSessao")
	String tipo;
	@XStreamAlias("CodigoSessao")
	Integer codigoSessao;
	@XStreamAlias("SituacaoSessao")
	String situacaoSessao;
	@XStreamAlias("NumeroSessao")
	String numeroSessao;

	Date getData() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		return sdf.parse(data + " " + hora);

	}

	public Integer getCodigoSessao() {
		return codigoSessao;
	}

	@XStreamAlias("Materias")
	Materias materias = new Materias();

	// protected List<Materia> materias = new ArrayList<Materia>();

}
