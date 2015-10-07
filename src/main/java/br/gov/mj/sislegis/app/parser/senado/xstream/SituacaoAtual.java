package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SituacaoAtual")
class SituacaoAtual {
	@XStreamAlias("Autuacoes")
	Autuacoes autuacoes;

	static void configXstream(XStream xstream) {
		xstream.processAnnotations(SituacaoAtual.class);
		Autuacoes.configXstream(xstream);

	}
}
