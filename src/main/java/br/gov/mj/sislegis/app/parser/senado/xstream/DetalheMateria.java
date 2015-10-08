package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.thoughtworks.xstream.XStream;

/**
 * XStream mapper para o serviço
 * http://legis.senado.leg.br/dadosabertos/dados/DetalheMateriav3.xsd
 * 
 * @author coutinho
 *
 * @deprecated deve-se usar a versão 4 do serviço.
 */
public class DetalheMateria {

	List<Proposicao> proposicoes;

	List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	public static void configXstream(XStream xstream) {
		xstream.alias("DetalheMateria", DetalheMateria.class);
		xstream.alias("Materia", Proposicao.class);
		// Conversao de tipos Autoria e Autor
		xstream.alias("Autoria", Autoria.class);
		xstream.alias("Autor", Autor.class);

		xstream.aliasField("Materias", DetalheMateria.class, "proposicoes");

		xstream.aliasField("Codigo", Proposicao.class, "idProposicao");
		xstream.aliasField("Subtipo", Proposicao.class, "tipo");
		xstream.aliasField("Numero", Proposicao.class, "numero");
		xstream.aliasField("Ano", Proposicao.class, "ano");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
		// Forcar o tratamento de autoria como string
		xstream.aliasField("Autoria", Proposicao.class, "autor");
		xstream.registerLocalConverter(Proposicao.class, "autor", new AuthorConverter());

	}

	public Proposicao getProposicao() {
		return proposicoes.isEmpty() ? null : getProposicoes().get(0);

	}
}