package br.gov.mj.sislegis.app.parser.senado;

import java.util.List;

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.camara.ParserComissoesCamara;

import com.thoughtworks.xstream.XStream;

public class ParserComissoesSenado {

	public static void main(String[] args) throws Exception {
		ParserComissoesCamara parser = new ParserComissoesCamara();
		System.out.println(parser.getComissoes().toString());
	}

	public List<Comissao> getComissoes() throws Exception {
		String wsURl = "http://legis.senado.leg.br/dadosabertos/comissao/lista/colegiados";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		ListaColegiados comissoes = new ListaColegiados();

		config(xstream);
		ParserFetcher.fetchXStream(wsURl,xstream,comissoes);
		
		

		// Adiciona manualmente uma comissao para o plenario
		Comissao comissaoPlenario = new Comissao();
		comissaoPlenario.setId(999l);
		comissaoPlenario.setSigla("PLEN");
		comissoes.getComissoes().add(comissaoPlenario);

		return comissoes.getComissoes();
	}

	private void config(XStream xstream) {
		xstream.alias("ListaColegiados", ListaColegiados.class);
		xstream.alias("Colegiado", Comissao.class);

		xstream.aliasField("Colegiados", ListaColegiados.class, "comissoes");
		xstream.aliasField("Codigo", Comissao.class, "id");
		xstream.aliasField("Sigla", Comissao.class, "sigla");
	}
}

class ListaColegiados {

	private List<Comissao> comissoes;

	public List<Comissao> getComissoes() {
		return comissoes;
	}

	public void setComissoes(List<Comissao> comissoes) {
		this.comissoes = comissoes;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Comissao orgao : this.getComissoes()) {
			sb.append(orgao.getSigla() + "\n");
		}
		return sb.toString();
	}
}
