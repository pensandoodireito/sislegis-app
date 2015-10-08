package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;

/**
 * Interface para parser que buscam proposições.
 * 
 * @author coutinho
 *
 */
public interface ProposicaoSearcher {
	/**
	 * Retorna lista de proposicoes encontradas com os filtros tipo, numero e
	 * ano
	 * 
	 * @param tipo
	 * @param numero
	 * @param ano
	 * @return
	 * @throws IOException
	 */
	public Collection<Proposicao> searchProposicao(String tipo, Integer numero, Integer ano) throws IOException;

	/**
	 * Retorna todos os tipos de possíveis para um filtro de proposicoes
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<TipoProposicao> listaTipos() throws IOException;
}
