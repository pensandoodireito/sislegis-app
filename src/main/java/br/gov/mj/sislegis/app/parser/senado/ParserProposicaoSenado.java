package br.gov.mj.sislegis.app.parser.senado;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import com.thoughtworks.xstream.XStream;

import java.net.URL;
import java.util.List;
import java.util.Objects;

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
		
		Proposicao proposicao = new ProposicaoSenado();
		
		proposicao = detalheMateria.getProposicoes().isEmpty() ? proposicao : detalheMateria.getProposicoes().get(0);
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="+proposicao.getIdProposicao());
		
		return proposicao;
	}
	
	private static void config(XStream xstream) {
		xstream.alias("DetalheMateria", DetalheMateria.class);
		xstream.alias("Materia", ProposicaoSenado.class);
		xstream.alias("Autor", Autor.class);

		xstream.aliasField("Materias", DetalheMateria.class, "proposicoes");
		xstream.aliasField("Autoria", ProposicaoSenado.class, "autores");

		xstream.aliasField("Codigo", ProposicaoSenado.class, "idProposicao");
		xstream.aliasField("Subtipo", ProposicaoSenado.class, "tipo");
		xstream.aliasField("Numero", ProposicaoSenado.class, "numero");
		xstream.aliasField("Ano", ProposicaoSenado.class, "ano");
		xstream.aliasField("Ementa", ProposicaoSenado.class, "ementa");
		xstream.aliasField("Nome", Autor.class, "nome");

	}
}

class DetalheMateria {

	protected List<ProposicaoSenado> proposicoes;

	protected List<ProposicaoSenado> getProposicoes() {
		return proposicoes;
	}
}

class ProposicaoSenado extends Proposicao {
	protected List<Autor> autores;

	protected List<Autor> getAutores() {
		return autores;
	}

	@Override
	public String getAutor() {
		if (Objects.isNull(autores) || autores.size() == 0) {
			return super.getAutor();

		} else {
			return autores.get(0).getNome();
		}

	}

}

class Autor {
	protected String nome;

	protected String getNome() {
		return nome;
	}
}