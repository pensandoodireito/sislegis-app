package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.ArrayList;
import java.util.List;

public class ParteBean {
	protected List<ItemBean> itens = new ArrayList<ItemBean>();
	protected List<EventoBean> eventos = new ArrayList<EventoBean>();
	public List<ItemBean> getItens() {
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