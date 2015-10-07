package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Autuacao")
class Autuacao {
	@XStreamAlias("NumeroAutuacao")
	Integer numeroAutuacao;

	@XStreamAlias("Situacao")
	Situacao Situacao;
	@XStreamAlias("Local")
	Local Local;

	static void configXstream(XStream xstream) {
		xstream.processAnnotations(Autuacao.class);
		xstream.processAnnotations(SituacaoAtual.class);
		xstream.processAnnotations(Situacao.class);
		xstream.processAnnotations(Local.class);

	}
}
