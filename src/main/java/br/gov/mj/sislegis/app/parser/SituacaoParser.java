package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.util.Collection;

import br.gov.mj.sislegis.app.model.SituacaoLegislativa;

/**
 * Interface para parser que buscam situacoes.
 * 
 * @author coutinho
 *
 */
public interface SituacaoParser {

	/**
	 * Retorna lista de situacoes encontradas
	 * 
	 * @throws IOException
	 */
	public Collection<SituacaoLegislativa> listSituacoes() throws IOException;
}
