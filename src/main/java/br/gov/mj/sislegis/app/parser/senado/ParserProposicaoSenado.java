package br.gov.mj.sislegis.app.parser.senado;

import java.net.URL;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.autoria.Autor;
import br.gov.mj.sislegis.app.model.autoria.Autoria;

public class ParserProposicaoSenado {
	
	public static void main(String[] args) throws Exception {
		ParserProposicaoSenado parser = new ParserProposicaoSenado();
		Long idProposicao = 24257L; // TODO: Informação que vem do filtro
		System.out.println(parser.getProposicao(idProposicao).toString());
	}
	
	public Proposicao getProposicao(Long idProposicao) throws Exception {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/"+idProposicao+"?v=3";
		URL url = new URL(wsURL);
		
		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		
		DetalheMateria detalheMateria = new DetalheMateria();
		
		config(xstream);

		xstream.fromXML(url, detalheMateria);
		Proposicao proposicao = new Proposicao();
		
		proposicao = detalheMateria.getProposicoes().isEmpty() ? proposicao : detalheMateria.getProposicoes().get(0);
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="+proposicao.getIdProposicao());
		proposicao.setAutor(proposicao.getAutoria().getAutor().getNome());
		
		return proposicao;
	}
	
	private static void config(XStream xstream) {
		xstream.alias("DetalheMateria", DetalheMateria.class);
		xstream.alias("Materia", Proposicao.class);
		
		xstream.aliasField("Materias", DetalheMateria.class, "proposicoes");
		
		xstream.aliasField("Codigo", Proposicao.class, "idProposicao");
		xstream.aliasField("Subtipo", Proposicao.class, "tipo");
		xstream.aliasField("Numero", Proposicao.class, "numero");
		xstream.aliasField("Ano", Proposicao.class, "ano");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
		
		// Obtendo autor da proposição do Senado
		xstream.alias("Autoria", Autoria.class);
		xstream.alias("Autor", Autor.class);

		xstream.aliasField("Autoria", Proposicao.class, "autoria");
		xstream.aliasField("Autoria", DetalheMateria.class, "autoria");
		
		xstream.aliasField("Autor", Autoria.class, "autor");
		xstream.aliasField("Nome", Autor.class, "nome");
	}
}

class DetalheMateria {

	protected List<Proposicao> proposicoes;
	
	protected List<Proposicao> getProposicoes() {
		return proposicoes;
	}
}
