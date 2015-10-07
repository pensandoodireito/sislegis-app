package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;

public interface ProposicaoSearcher {
	public Collection<Proposicao> searchProposicao(String tipo, Integer numero, Integer ano) throws IOException;

	public List<TipoProposicao> listaTipos() throws IOException;
}
