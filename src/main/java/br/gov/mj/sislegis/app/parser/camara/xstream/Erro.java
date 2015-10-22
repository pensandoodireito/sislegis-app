package br.gov.mj.sislegis.app.parser.camara.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("erro")
public class Erro {
	@XStreamAlias("descricao")
	String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
