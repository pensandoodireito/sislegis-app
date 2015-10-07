package br.gov.mj.sislegis.app.parser.senado.proposicao;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AutorPrincipal")
class AutorPrincipal {
	Long CodigoAutor;
	String NomeAutor;
	String SiglaTipoAutor;
}
