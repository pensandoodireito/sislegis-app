package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AutorPrincipal")
class AutorPrincipal {
	Long CodigoAutor;
	String NomeAutor;
	String SiglaTipoAutor;
}
