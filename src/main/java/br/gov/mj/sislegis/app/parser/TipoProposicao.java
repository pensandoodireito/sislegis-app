package br.gov.mj.sislegis.app.parser;

public class TipoProposicao {
	private String sigla;
	private String nome;

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {

		return sigla + ":{" + nome + "}@" + hashCode();
	}
}