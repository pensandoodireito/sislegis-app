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
		String wsURL = "http://legis.senado.leg.br/dadosabertos/materia/" + idProposicao + "?v=3";
		URL url = new URL(wsURL);

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		DetalheMateria detalheMateria = new DetalheMateria();

		config(xstream);

		xstream.fromXML(url, detalheMateria);

		Proposicao proposicao = new Proposicao();

		proposicao = detalheMateria.getProposicoes().isEmpty() ? proposicao : detalheMateria.getProposicoes().get(0).converterParaProposicao();
		proposicao.setOrigem(Origem.SENADO);
		proposicao.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=" + proposicao.getIdProposicao());

		return proposicao;
	}

	private static void config(XStream xstream) {
		xstream.alias("DetalheMateria", DetalheMateria.class);
		xstream.alias("Materia", ProposicaoSenadoDTO.class);
		xstream.alias("Autor", Autor.class);

		xstream.aliasField("Materias", DetalheMateria.class, "proposicoes");
		xstream.aliasField("Autoria", ProposicaoSenadoDTO.class, "autores");

		xstream.aliasField("Codigo", ProposicaoSenadoDTO.class, "idProposicao");
		xstream.aliasField("Subtipo", ProposicaoSenadoDTO.class, "tipo");
		xstream.aliasField("Numero", ProposicaoSenadoDTO.class, "numero");
		xstream.aliasField("Ano", ProposicaoSenadoDTO.class, "ano");
		xstream.aliasField("Ementa", ProposicaoSenadoDTO.class, "ementa");
		xstream.aliasField("Nome", Autor.class, "nome");
	}

}

class DetalheMateria {

	protected List<ProposicaoSenadoDTO> proposicoes;

	protected List<ProposicaoSenadoDTO> getProposicoes() {
		return proposicoes;
	}
}

class ProposicaoSenadoDTO {
	protected Integer idProposicao;
	protected String tipo;
	protected String numero;
	protected String ano;
	protected String ementa;
	protected List<Autor> autores;

	protected Proposicao converterParaProposicao() {
		Proposicao proposicao = new Proposicao();
		proposicao.setIdProposicao(idProposicao);
		proposicao.setTipo(tipo);
		proposicao.setNumero(numero);
		proposicao.setAno(ano);
		proposicao.setEmenta(ementa);

		proposicao.setAutor(converterAutorUnico());

		return proposicao;
	}

	private String converterAutorUnico() {
		if (Objects.isNull(autores) || autores.isEmpty()) {
			return null;

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