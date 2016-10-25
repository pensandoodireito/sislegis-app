package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("IdentificacaoTramitacao")
public class IdentificacaoTramitacao {
	String CodigoTramitacao;
	Integer NumeroAutuacao;
	String TextoTramitacao;
	@XStreamAlias("OrigemTramitacao")
	LocalTramitacao OrigemTramitacao;
	LocalTramitacao DestinoTramitacao;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return CodigoTramitacao + " " + NumeroAutuacao;
	}

	public String getCodigoTramitacao() {
		return CodigoTramitacao;
	}

	public Integer getNumeroAutuacao() {
		return NumeroAutuacao;
	}

	public String getTextoTramitacao() {
		return TextoTramitacao;
	}

	public LocalTramitacao getOrigemTramitacao() {
		return OrigemTramitacao;
	}

	public LocalTramitacao getDestinoTramitacao() {
		return DestinoTramitacao;
	}
}
