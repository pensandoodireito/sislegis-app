package br.gov.mj.sislegis.app.parser.senado;

import java.util.List;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ParserProposicaoSenado {

	public static void main(String[] args) throws Exception {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		Long idProposicao = 24257L; // TODO: Informação que vem do filtro
		System.out.println(parser.getProposicao(idProposicao).toString());
	}

	public Proposicao getProposicao(Long idProposicao) throws Exception {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/" + idProposicao + "?v=3";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		DetalheMateria detalheMateria = new DetalheMateria();

		config(xstream);
		ParserFetcher.fetchXStream(wsURL, xstream, detalheMateria);

		Proposicao proposicao = new Proposicao();

		proposicao = detalheMateria.getProposicoes().isEmpty() ? proposicao : detalheMateria.getProposicoes().get(0);
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=" + proposicao.getIdProposicao());

		return proposicao;
	}

	private static void config(XStream xstream) {
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
}

class AuthorConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return String.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// Desnecessario, somente parseia XML->Objetos

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if ("Autoria".equals(reader.getNodeName())) {
			Autoria au = (Autoria) context.convertAnother(reader, Autoria.class);
			if (au != null && au.Autor != null && au.Autor.Nome != null) {
				return au.Autor.Nome;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}

/*
 * wrapper para o nó /Autoria/Autor
 */
class Autor {
	String Nome;
}

/*
 * wrapper para o nó /Autoria
 */
class Autoria {
	Autor Autor;

}

class DetalheMateria {

	protected List<Proposicao> proposicoes;

	protected List<Proposicao> getProposicoes() {
		return proposicoes;
	}
}
