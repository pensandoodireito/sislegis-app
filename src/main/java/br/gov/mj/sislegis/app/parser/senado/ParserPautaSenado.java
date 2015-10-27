package br.gov.mj.sislegis.app.parser.senado;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.senado.xstream.ComissaoBean;
import br.gov.mj.sislegis.app.parser.senado.xstream.EventoBean;
import br.gov.mj.sislegis.app.parser.senado.xstream.ItemBean;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListaProposicoes;
import br.gov.mj.sislegis.app.parser.senado.xstream.ListaReunioes;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materia;
import br.gov.mj.sislegis.app.parser.senado.xstream.Materias;
import br.gov.mj.sislegis.app.parser.senado.xstream.ParteBean;
import br.gov.mj.sislegis.app.parser.senado.xstream.ReuniaoBeanSenado;

import com.thoughtworks.xstream.XStream;

public class ParserPautaSenado {

	public static void main(String[] args) throws Exception {
		ParserPautaSenado parser = new ParserPautaSenado();

		// TODO: Informação que vem do filtro
		String siglaComissao = "CAE";
		String datIni = "20151001";
		Set<PautaReuniaoComissao> pautas = parser.getPautaComissao(siglaComissao, datIni);

		for (Iterator iterator = pautas.iterator(); iterator.hasNext();) {
			PautaReuniaoComissao pautaReuniaoComissao = (PautaReuniaoComissao) iterator.next();
			System.out.println(pautaReuniaoComissao);
			for (Iterator iterator2 = pautaReuniaoComissao.getProposicoes().iterator(); iterator2.hasNext();) {
				ProposicaoPautaComissao ppc = (ProposicaoPautaComissao) iterator2.next();
				System.out.println("\t" + ppc);

			}

		}
		// System.out.println(parser.getPautaComissao(siglaComissao,
		// datIni).toString());
		// System.out.println(parser.getProposicoes(siglaComissao,
		// datIni).toString());
	}

	public Set<PautaReuniaoComissao> getPautaComissao(String siglaComissao, String datIni) throws Exception {

		Set<PautaReuniaoComissao> pautas = new HashSet<PautaReuniaoComissao>();

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		xstream.alias("Reuniao", ReuniaoBeanSenado.class);
		xstream.alias("Comissao", ComissaoBean.class);
		xstream.alias("Parte", ParteBean.class);
		xstream.alias("Item", ItemBean.class);
		xstream.alias("Evento", EventoBean.class);
		Materias.configXstream(xstream);
		xstream.aliasField("Partes", ReuniaoBeanSenado.class, "partes");
		xstream.aliasField("Comissoes", ReuniaoBeanSenado.class, "comissoes");
		xstream.aliasField("Codigo", ReuniaoBeanSenado.class, "codigo");
		xstream.aliasField("Titulo", ReuniaoBeanSenado.class, "titulo");
		xstream.aliasField("Data", ReuniaoBeanSenado.class, "data");
		xstream.aliasField("Hora", ReuniaoBeanSenado.class, "hora");
		xstream.aliasField("Tipo", ReuniaoBeanSenado.class, "tipo");

		xstream.aliasField("Itens", ParteBean.class, "itens");
		xstream.aliasField("Eventos", ParteBean.class, "eventos");
		xstream.aliasField("Materia", ItemBean.class, "materia");
		xstream.aliasAttribute(ItemBean.class, "tipo", "tipo");

		xstream.aliasField("MateriasRelacionadas", EventoBean.class, "materiasRelacionadas");

		xstream.aliasField("SeqOrdemPauta", ItemBean.class, "seqOrdemPauta");

		StringBuilder wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/agenda/");
		wsURL.append(datIni);
		wsURL.append("/detalhe?colegiado=").append(URLEncoder.encode(siglaComissao, "UTF-8"));
		ListaReunioes reunioes = new ListaReunioes();

		configAgenda(xstream);
		ParserFetcher.fetchXStream(wsURL.toString(), xstream, reunioes);
		for (Iterator iterator = reunioes.getReunioes().iterator(); iterator.hasNext();) {
			ReuniaoBeanSenado pautaReuniaoComissao = (ReuniaoBeanSenado) iterator.next();
			Comissao comissao = new Comissao();
			comissao.setSigla(siglaComissao);
			PautaReuniaoComissao prc = new PautaReuniaoComissao(pautaReuniaoComissao.getDate(), comissao,
					pautaReuniaoComissao.getCodigo());
			prc.setTipo(pautaReuniaoComissao.getTipo());
			prc.setTitulo(pautaReuniaoComissao.getTitulo());

			Set<ProposicaoPautaComissao> ps = pautaReuniaoComissao.getProposioesPauta(prc);

			if (ps.size() > 0) {
				pautas.add(prc);
			}

		}

		return pautas;
	}

	public List<Proposicao> getProposicoes(String siglaComissao, String datIni) throws Exception {
		List<Proposicao> proposicoes = new ArrayList<Proposicao>();

		XStream xstreamReuniao = new XStream();
		xstreamReuniao.ignoreUnknownElements();

		configReuniao(xstreamReuniao);

		for (ReuniaoBeanSenado bean : getReunioes(siglaComissao, datIni)) {

			String wsURLReuniao = "http://legis.senado.leg.br/dadosabertos/reuniao/" + bean.getCodigo();
			ReuniaoBeanSenado reuniao = new ReuniaoBeanSenado();
			ParserFetcher.fetchXStream(wsURLReuniao, xstreamReuniao, reuniao);

			// proposicoes.addAll(reuniao.getProposicoes());
		}

		return proposicoes;
	}

	public List<ReuniaoBeanSenado> getReunioes(String siglaComissao, String datIni) throws Exception {
		return getReunioes(siglaComissao, datIni, null);
	}

	public List<ReuniaoBeanSenado> getReunioes(String siglaComissao, String datIni, String dataFim) throws Exception {

		StringBuilder wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/agenda/");
		wsURL.append(datIni);
		if (dataFim != null) {
			wsURL.append("/");
			wsURL.append(datIni);
		}
		wsURL.append("?colegiado=").append(URLEncoder.encode(siglaComissao, "UTF-8"));

		XStream xstreamAgenda = new XStream();
		xstreamAgenda.ignoreUnknownElements();

		ListaReunioes reunioes = new ListaReunioes();

		configAgenda(xstreamAgenda);
		ParserFetcher.fetchXStream(wsURL.toString(), xstreamAgenda, reunioes);

		return reunioes.getReunioes();
	}

	private void configAgenda(XStream xstream) {
		xstream.alias("Reunioes", ListaReunioes.class);
		xstream.alias("Reuniao", ReuniaoBeanSenado.class);
		xstream.aliasField("Hora", ReuniaoBeanSenado.class, "hora");
		xstream.aliasField("Data", ReuniaoBeanSenado.class, "data");
		xstream.aliasField("Tipo", ReuniaoBeanSenado.class, "tipo");
		xstream.aliasField("Situacao", ReuniaoBeanSenado.class, "situacao");
		xstream.aliasField("TituloDaReuniao", ReuniaoBeanSenado.class, "titulo");

		xstream.addImplicitCollection(ListaReunioes.class, "reunioes");

		xstream.aliasField("Codigo", ReuniaoBeanSenado.class, "codigo");
	}

	private void configReuniao(XStream xstream) {
		xstream.alias("Reuniao", ReuniaoBeanSenado.class);
		xstream.alias("Comissao", ComissaoBean.class);
		xstream.alias("Parte", ParteBean.class);
		xstream.alias("Item", ItemBean.class);
		xstream.alias("Evento", EventoBean.class);
		xstream.alias("Materia", Proposicao.class);

		xstream.aliasField("Partes", ReuniaoBeanSenado.class, "partes");
		xstream.aliasField("Comissoes", ReuniaoBeanSenado.class, "comissoes");
		xstream.aliasField("Codigo", ReuniaoBeanSenado.class, "codigo");
		xstream.aliasField("Sigla", ComissaoBean.class, "sigla");
		xstream.aliasField("Nome", ComissaoBean.class, "nome");
		xstream.aliasField("Itens", ParteBean.class, "itens");
		xstream.aliasField("Eventos", ParteBean.class, "eventos");
		xstream.aliasField("Materia", ItemBean.class, "proposicao");
		xstream.aliasAttribute(ItemBean.class, "tipo", "tipo");
		xstream.aliasField("MateriasRelacionadas", EventoBean.class, "proposicoes");
		xstream.aliasField("Materia", ListaProposicoes.class, "proposicao");
		xstream.aliasField("SeqOrdemPauta", ItemBean.class, "seqOrdemPauta");
		xstream.aliasField("Codigo", Proposicao.class, "idProposicao");
		xstream.aliasField("Subtipo", Proposicao.class, "tipo");
		xstream.aliasField("Numero", Proposicao.class, "numero");
		xstream.aliasField("Ano", Proposicao.class, "ano");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
	}
}