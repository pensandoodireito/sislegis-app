package br.gov.mj.sislegis.app.parser.camara.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("situacao")
public class Situacao {
	String id;
	String descricao;
	@XStreamAlias("orgao")
	Orgao orgao;

	static void configXstream(XStream xStream) {
		xStream.processAnnotations(Situacao.class);
		xStream.processAnnotations(Orgao.class);
	}

	public String getDescricao() {
		return descricao;
	}
}

@XStreamAlias("orgao")
class Orgao {
	String codOrgaoEstado;
	String siglaOrgaoEstado;
}
