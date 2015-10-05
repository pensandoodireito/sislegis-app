package br.gov.mj.sislegis.app.parser.senado;

import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;

import com.thoughtworks.xstream.XStream;

public class ParserPautaSenado {

	public static void main(String[] args) throws Exception {
		ParserPautaSenado parser = new ParserPautaSenado();

		// TODO: Informação que vem do filtro
		String siglaComissao = "CDH";
		String datIni = "20141201";
		System.out.println(parser.getReunioes(siglaComissao, datIni).toString());
		System.out.println(parser.getProposicoes(siglaComissao, datIni).toString());
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

			proposicoes.addAll(reuniao.getProposicoes());
		}

		return proposicoes;
	}

	public List<ReuniaoBeanSenado> getReunioes(String siglaComissao, String datIni) throws Exception {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/agenda/" + datIni + "?colegiado=" + siglaComissao;
		
		XStream xstreamAgenda = new XStream();
		xstreamAgenda.ignoreUnknownElements();

		ListaReunioes reunioes = new ListaReunioes();

		configAgenda(xstreamAgenda);
		ParserFetcher.fetchXStream(wsURL, xstreamAgenda, reunioes);

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

class ListaReunioes {
	protected List<ReuniaoBeanSenado> reunioes = new ArrayList<ReuniaoBeanSenado>();

	protected List<ReuniaoBeanSenado> getReunioes() {
		return reunioes;
	}
}

class ComissaoBean {
	protected String sigla;
	protected String nome;

	protected String getSigla() {
		return sigla;
	}

	protected String getNome() {
		return nome;
	}
}

class ParteBean {
	protected List<ItemBean> itens = new ArrayList<ItemBean>();
	protected List<EventoBean> eventos = new ArrayList<EventoBean>();

	protected List<ItemBean> getItens() {
		return itens;
	}

	protected void setItens(List<ItemBean> itens) {
		this.itens = itens;
	}

	public List<EventoBean> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoBean> eventos) {
		this.eventos = eventos;
	}
}

class ItemBean {
	protected Integer seqOrdemPauta;
	protected Proposicao proposicao;
	protected String tipo;

	protected Proposicao getProposicao() {
		proposicao.setSeqOrdemPauta(seqOrdemPauta);
		return proposicao;
	}

	protected Integer getSeqOrdemPauta() {
		return seqOrdemPauta;
	}
}

class EventoBean {
	protected Integer seqOrdemPauta;
	protected List<Proposicao> proposicoes;

	public List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	public void setProposicoes(List<Proposicao> proposicoes) {
		this.proposicoes = proposicoes;
	}
}

class ListaProposicoes {
	protected Proposicao proposicao;

	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}
}