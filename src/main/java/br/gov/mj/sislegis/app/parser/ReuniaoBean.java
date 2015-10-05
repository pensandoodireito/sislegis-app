package br.gov.mj.sislegis.app.parser;

import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;

public abstract class ReuniaoBean {
	protected Integer codigo;
	protected String titulo;
	protected String data;
	protected String hora;

	protected String situacao;
	protected String tipo;

	public String getTitulo() {
		return titulo;
	}

	public String getData() {
		return data;
	}

	public String getHora() {
		return hora;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public String getSituacao() {
		return situacao;
	}

	public abstract Sessao getSessao();

	@Override
	public String toString() {

		return titulo + ":" + tipo + "|" + situacao + "@" + data + " " + hora;
	}
}
