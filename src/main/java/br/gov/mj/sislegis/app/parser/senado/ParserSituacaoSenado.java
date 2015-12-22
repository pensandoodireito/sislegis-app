package br.gov.mj.sislegis.app.parser.senado;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.SituacaoLegislativa;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.SituacaoParser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ParserSituacaoSenado implements SituacaoParser {
	public static void main(String[] args) {
		try {
			Collection<SituacaoLegislativa> c = new ParserSituacaoSenado().listSituacoes();
			for (Iterator<SituacaoLegislativa> iterator = c.iterator(); iterator.hasNext();) {
				SituacaoLegislativa object = (SituacaoLegislativa) iterator.next();
				System.out.println(object.getDescricao() + " " + object.getObsoleta() + " " + object.getIdExterno());

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Collection<SituacaoLegislativa> listSituacoes() throws IOException {
		String wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/materia/situacoes").toString();

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		ListaSituacoes list = new ListaSituacoes();

		xstream.processAnnotations(ListaSituacoes.class);
		xstream.alias("Situacao", SituacaoLegislativa.class);

		xstream.aliasField("Codigo", SituacaoLegislativa.class, "idExterno");
		xstream.aliasField("Sigla", SituacaoLegislativa.class, "sigla");
		xstream.aliasField("Descricao", SituacaoLegislativa.class, "descricao");

		ParserFetcher.fetchXStream(wsURL, xstream, list);
		Collection<SituacaoLegislativa> situacoes = list.Situacoes;

		for (Iterator iterator = situacoes.iterator(); iterator.hasNext();) {
			SituacaoLegislativa situacaoLegislativa = (SituacaoLegislativa) iterator.next();
			situacaoLegislativa.setOrigem(Origem.SENADO);
			situacaoLegislativa.setObsoleta(false);
			situacaoLegislativa.setTerminativa(false);

		}

		return situacoes;
	}

}

@XStreamAlias("ListaSituacoes")
class ListaSituacoes {
	@XStreamAlias("Situacoes")
	List<SituacaoLegislativa> Situacoes = new ArrayList<SituacaoLegislativa>();
}