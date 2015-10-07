package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.List;

import br.gov.mj.sislegis.app.parser.TipoProposicao;

import com.thoughtworks.xstream.XStream;

public class ListaSubtiposMateria {
	SubtiposMateria SubtiposMateria;

	public static void configXstream(XStream xstream) {
		xstream.alias("ListaSubtiposMateria", ListaSubtiposMateria.class);
		xstream.alias("SubtipoMateria", TipoProposicao.class);
		xstream.addImplicitCollection(SubtiposMateria.class, "subtipos");
		xstream.aliasField("SiglaMateria", TipoProposicao.class, "sigla");
		xstream.aliasField("DescricaoSubtipoMateria", TipoProposicao.class, "nome");

	}

	public List<TipoProposicao> getTiposProposicao() {

		return SubtiposMateria.subtipos;
	}

}