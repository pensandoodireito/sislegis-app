package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Metadados")
public class Metadados {
	@XStreamAlias("Versao")
	String versao;
	@XStreamAlias("VersaoServico")
	String versaoServico;

	@XStreamAlias("DescricaoDataSet")
	String descricaoDataSet;

	@Override
	public String toString() {

		return versao + ":{" + descricaoDataSet + "}";
	}

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Metadados.class);

	}
}
