package br.gov.mj.sislegis.app.parser.senado.proposicao;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Autuacoes")
class Autuacoes {
	@XStreamImplicit(itemFieldName = "Autuacao")
	List<Autuacao> autuacoes;

	static void configXstream(XStream xstream) {
		xstream.processAnnotations(Autuacoes.class);
		Autuacao.configXstream(xstream);

	}
}
