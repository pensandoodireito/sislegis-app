package br.gov.mj.sislegis.app.parser.senado.proposicao;

import br.gov.mj.sislegis.app.parser.TipoProposicao;

import com.thoughtworks.xstream.XStream;

class ListaSubtiposMateria {
	SubtiposMateria SubtiposMateria;

	static void configXstream(XStream xstream) {
		xstream.alias("ListaSubtiposMateria", ListaSubtiposMateria.class);
		xstream.alias("SubtipoMateria", TipoProposicao.class);
		xstream.addImplicitCollection(SubtiposMateria.class, "subtipos");
		xstream.aliasField("SiglaMateria", TipoProposicao.class, "sigla");
		xstream.aliasField("DescricaoSubtipoMateria", TipoProposicao.class, "nome");

	}

}