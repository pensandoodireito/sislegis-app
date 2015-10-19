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

	public List<Proposicao> buscarProposicoesPautaCamaraWS(Map parametros) throws Exception;

	public List<Proposicao> buscarProposicoesPautaSenadoWS(Map parametros) throws Exception;

	public Proposicao detalharProposicaoCamaraWS(Long id) throws Exception;

	public Proposicao detalharProposicaoSenadoWS(Long id) throws Exception;

	public void salvarListaProposicao(List<Proposicao> lista);

	public List<Proposicao> listarTodos();

	public Proposicao buscarPorId(Integer id);

	public List<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao);

	public void atualizarProposicaoJSON(Proposicao proposicao);

	public List<Proposicao> buscarPorSufixo(String sufixo);

	public List<Proposicao> consultar(String sigla, String autor, String ementa, String origem, String isFavorita,
			Integer offset, Integer limit);

	/**
	 * Faz buscas por proposições diretamente dos webservices da origem, não
	 * tendo a necessidade delas estarem no banco do sislegis.
	 * 
	 * @param tipo
	 * @param numer
	 * @param ano
	 * @return Lista de proposicoes encontradas na origem
	 */
	public Collection<Proposicao> buscaProposicaoIndependentePor(Origem origem, String tipo, Integer numero, Integer ano)
			throws IOException;

	public Collection<TipoProposicao> listTipos(Origem valueOf) throws IOException;

	/**
	 * Salva uma proposicao não relacionada a uma reuniao
	 * 
	 * @param proposicaoFromBusca
	 * @return 1 se criou, 0 se já existia, -1 se houve erro.
	 */
	int salvarProposicaoIndependente(Proposicao proposicaoFromBusca);
}
