package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Tramitacao")
public class Tramitacao {
	IdentificacaoTramitacao IdentificacaoTramitacao;

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Tramitacao.class);
		xstream.processAnnotations(IdentificacaoTramitacao.class);	
		xstream.processAnnotations(Local.class);
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return IdentificacaoTramitacao!=null?IdentificacaoTramitacao.toString():super.toString();
	}

	public IdentificacaoTramitacao getIdentificacaoTramitacao() {
		return IdentificacaoTramitacao;
	}
	
}
