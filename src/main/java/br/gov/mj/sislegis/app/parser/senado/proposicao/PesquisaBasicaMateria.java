package br.gov.mj.sislegis.app.parser.senado.proposicao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PesquisaBasicaMateria")
class PesquisaBasicaMateria {
	@XStreamAlias("Materias")
	Materias materias;
	@XStreamAlias("DescricaoDataSet")
	String descricaoDataSet;

	static void configXstream(XStream xstream) {
		xstream.processAnnotations(PesquisaBasicaMateria.class);
		Materias.configXstream(xstream);

	}

}