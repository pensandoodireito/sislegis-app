package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AutorPrincipal")
class AutorPrincipal {
	Long CodigoAutor;
	String NomeAutor;
	String SiglaTipoAutor;
	String Tratamento;

	public String getDescricao() {
		String desc = "";
		if (Tratamento != null && Tratamento.length() > 0) {
			desc += Tratamento + " ";
		}
		if (NomeAutor != null && NomeAutor.length() > 0) {
			desc += NomeAutor;
		}
		return desc;
	}
}
