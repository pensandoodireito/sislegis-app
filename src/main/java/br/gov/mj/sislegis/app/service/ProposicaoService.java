package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.TipoProposicao;

import javax.ejb.Local;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Local
public interface ProposicaoService extends Service<Proposicao> {

	List<Proposicao> buscarProposicoesPautaCamaraWS(Map parametros) throws Exception;

	List<Proposicao> buscarProposicoesPautaSenadoWS(Map parametros) throws Exception;

	Proposicao detalharProposicaoCamaraWS(Long id) throws Exception;

	Proposicao detalharProposicaoSenadoWS(Long id) throws Exception;

	void salvarListaProposicao(List<Proposicao> lista);

	List<Proposicao> listarTodos();

	Proposicao buscarPorId(Integer id);

	List<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao);

	List<Proposicao> buscarPorSufixo(String sufixo);

	List<Proposicao> consultar(String sigla, String autor, String ementa, String origem, String isFavorita,	Integer offset, Integer limit);

	/**
	 * Faz buscas por proposições diretamente dos webservices da origem, não
	 * tendo a necessidade delas estarem no banco do sislegis.
	 * 
	 * @param tipo
	 * @param numer
	 * @param ano
	 * @return Lista de proposicoes encontradas na origem
	 */
	Collection<Proposicao> buscaProposicaoIndependentePor(Origem origem, String tipo, Integer numero, Integer ano)
		throws IOException;

	Collection<TipoProposicao> listTipos(Origem valueOf) throws IOException;

	/**
	 * Salva uma proposicao não relacionada a uma reuniao
	 * 
	 * @param proposicaoFromBusca
	 * @return 1 se criou, 0 se já existia, -1 se houve erro.
	 */
	int salvarProposicaoIndependente(Proposicao proposicaoFromBusca);
}
