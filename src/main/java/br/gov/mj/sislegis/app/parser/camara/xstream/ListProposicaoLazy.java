package br.gov.mj.sislegis.app.parser.camara.xstream;

import java.util.List;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.CollectionLazyConverter;

/**
 * Conversor Lazy para atualizar as proposicoes somente quando usadas
 * 
 * @author coutinho
 *
 */
public class ListProposicaoLazy extends CollectionLazyConverter<Proposicao, Proposicao> {

	public ListProposicaoLazy(List<Proposicao> materias) {
		super(materias);
	}

	@Override
	protected Proposicao convertKtoE(Proposicao proposicao) {
		proposicao.setOrigem(Origem.CAMARA);
		proposicao.setLinkProposicao("http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao="
				+ proposicao.getIdProposicao());
		return proposicao;
	}

}