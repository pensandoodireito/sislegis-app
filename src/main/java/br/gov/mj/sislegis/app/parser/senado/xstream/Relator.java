package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Relatoria")
public class Relator {
	@XStreamAlias("Nome")
	String nome;
	@XStreamAlias("Tratamento")
	String tratamento;
	@XStreamAlias("Foto")
	String foto;

}
