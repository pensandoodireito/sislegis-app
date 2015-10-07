package br.gov.mj.sislegis.app.parser.senado.proposicao;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("AutoresPrincipais=")
class AutoresPrincipais {
	@XStreamImplicit(itemFieldName = "AutorPrincipal")
	List<AutorPrincipal> autores;
}
