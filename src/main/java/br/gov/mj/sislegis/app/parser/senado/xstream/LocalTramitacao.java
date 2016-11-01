package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class LocalTramitacao {
	@XStreamAlias("Local")
	Local local;

	@Override
	public String toString() {
		if (local != null) {
			return local.toString();
		} else {
			return "null";
		}

	}
}
