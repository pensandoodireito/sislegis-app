package br.gov.mj.sislegis.app.parser;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;

/**
 * 
 * Factory simples para reduzir código repetido
 * 
 * @author rafael.coutinho
 *
 */
public class ProposicaoSearcherFactory {

	public static ProposicaoSearcher getInstance(Proposicao proposicao) {
		switch (proposicao.getOrigem()) {
		case SENADO:
			return new ParserProposicaoSenado();
		case CAMARA:
			return new ParserProposicaoSenado();

		}
		return null;
	}

}
