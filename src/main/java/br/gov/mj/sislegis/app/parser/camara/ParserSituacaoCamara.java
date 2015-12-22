package br.gov.mj.sislegis.app.parser.camara;

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
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ParserSituacaoCamara implements SituacaoParser {
	public static void main(String[] args) {
		try {
			Collection<SituacaoLegislativa> c = new ParserSituacaoCamara().listSituacoes();
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
		String wsURL = new StringBuilder(
				"http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarSituacoesProposicao").toString();

		XStream xstream = new XStream();
		// xstream.ignoreUnknownElements();

		ListSituacaoProposcao list = new ListSituacaoProposcao();
		xstream.alias("situacaoProposicao", ListSituacaoProposcao.class);
		xstream.processAnnotations(ListSituacaoProposcao.class);
		xstream.aliasAttribute(SituacaoLegislativa.class, "idExterno", "id");
		xstream.aliasAttribute(SituacaoLegislativa.class, "descricao", "descricao");
		xstream.aliasAttribute(SituacaoLegislativa.class, "obsoleta", "ativa");

		ParserFetcher.fetchXStream(wsURL, xstream, list);
		Collection<SituacaoLegislativa> situacoes = list.situacaoProposicao;

		for (Iterator iterator = situacoes.iterator(); iterator.hasNext();) {
			SituacaoLegislativa situacaoLegislativa = (SituacaoLegislativa) iterator.next();
			situacaoLegislativa.setOrigem(Origem.CAMARA);
			situacaoLegislativa.setObsoleta(!situacaoLegislativa.getObsoleta());//é o inverso de ativa
			situacaoLegislativa.setTerminativa(false);

		}

		return situacoes;
	}

}

class ListSituacaoProposcao {
	@XStreamImplicit(itemFieldName = "situacaoProposicao")
	List<SituacaoLegislativa> situacaoProposicao = new ArrayList<SituacaoLegislativa>();
}