package br.gov.mj.sislegis.app.service.ejbs;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.rpc.ServiceException;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AlteracaoProposicao;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.model.ProcessoSei;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicaoPK;
import br.gov.mj.sislegis.app.model.RoadmapComissao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.Votacao;
import br.gov.mj.sislegis.app.model.documentos.Briefing;
import br.gov.mj.sislegis.app.model.documentos.DocRelated;
import br.gov.mj.sislegis.app.model.documentos.Emenda;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.PropostaPautaPK;
import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcherFactory;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserVotacaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserVotacaoSenado;
import br.gov.mj.sislegis.app.seiws.RetornoConsultaProcedimento;
import br.gov.mj.sislegis.app.seiws.SeiServiceLocator;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.PosicionamentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.Conversores;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class ProposicaoServiceEjb extends AbstractPersistence<Proposicao, Long> implements ProposicaoService, EJBUnitTestable {

	@Inject
	private ParserPautaCamara parserPautaCamara;

	@Inject
	private ParserPautaSenado parserPautaSenado;

	@Inject
	private ParserProposicaoCamara parserProposicaoCamara;

	@Inject
	private ParserProposicaoSenado parserProposicaoSenado;

	@Inject
	private ParserPlenarioSenado parserPlenarioSenado;

	@Inject
	private ParserVotacaoCamara parserVotacaoCamara;

	@Inject
	private ParserVotacaoSenado parserVotacaoSenado;

	@Inject
	private ComentarioService comentarioService;

	@Inject
	private ReuniaoService reuniaoService;

	@Inject
	private AreaDeMeritoService areaMeritoService;

	@Inject
	private ReuniaoProposicaoService reuniaoProposicaoService;

	@Inject
	private EncaminhamentoProposicaoService encaminhamentoProposicaoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private ComissaoService comissaoService;

	@Inject
	private PosicionamentoService posicionamentoService;

	@PersistenceContext
	private EntityManager em;

	public ProposicaoServiceEjb() {
		super(Proposicao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	private static Date getNextWeek(Date ref) {
		Calendar c = Calendar.getInstance();
		c.setTime(ref);
		c.add(Calendar.WEEK_OF_YEAR, 1);
		return c.getTime();
	}

	private static Date getClosestMonday(Date ref) {
		Calendar c = Calendar.getInstance();
		c.setTime(ref);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return c.getTime();
	}

	@Override
	public Set<PautaReuniaoComissao> buscarProposicoesPautaCamaraWS(Map parametros) throws Exception {
		Long idComissao = (Long) parametros.get("idComissao");
		Date dataInicial = (Date) parametros.get("data");
		Date dataFinal = getNextWeek(dataInicial);
		return buscarProposicoesPautaCamaraWS(idComissao, dataInicial, dataFinal);
	}

	@Override
	public Set<PautaReuniaoComissao> buscarProposicoesPautaCamaraWS(Long idComissao, Date dataInicial, Date dataFinal) throws Exception {

		String dataIni = Conversores.dateToString(dataInicial, "yyyyMMdd");
		String dataFim = Conversores.dateToString(dataFinal, "yyyyMMdd");
		return populaUltimoComentarioDePauta(parserPautaCamara.getPautaComissao(comissaoService.getComissaoCamara(idComissao), idComissao, dataIni, dataFim));
	}

	@Override
	public Set<PautaReuniaoComissao> buscarProposicoesPautaSenadoWS(Map parametros) throws Exception {
		String siglaComissao = (String) parametros.get("siglaComissao");
		Date dataInicial = (Date) parametros.get("data");
		Date dataFinal = getNextWeek(dataInicial);
		return buscarProposicoesPautaSenadoWS(siglaComissao, dataInicial, dataFinal);
	}

	@Override
	public Set<PautaReuniaoComissao> buscarProposicoesPautaSenadoWS(String siglaComissao, Date dataInicial, Date dataFinal) throws Exception {

		String dataIni = Conversores.dateToString(dataInicial, "yyyyMMdd");
		String dataFim = Conversores.dateToString(dataFinal, "yyyyMMdd");
		if (siglaComissao.equals("PLEN")) {
			return parserPlenarioSenado.getProposicoes(dataIni);
		}

		return populaUltimoComentarioDePauta(parserPautaSenado.getPautaComissao(siglaComissao, dataIni, dataFim));
	}

	private Set<PautaReuniaoComissao> populaUltimoComentarioDePauta(Set<PautaReuniaoComissao> pautas) {
		for (Iterator<PautaReuniaoComissao> iterator = pautas.iterator(); iterator.hasNext();) {
			PautaReuniaoComissao pautaReuniaoComissao = (PautaReuniaoComissao) iterator.next();
			SortedSet<ProposicaoPautaComissao> ppcs = pautaReuniaoComissao.getProposicoesDaPauta();
			for (Iterator<ProposicaoPautaComissao> iterator2 = ppcs.iterator(); iterator2.hasNext();) {
				ProposicaoPautaComissao proposicaoPautaComissao = (ProposicaoPautaComissao) iterator2.next();
				Proposicao proposicaoPauta = proposicaoPautaComissao.getProposicao();

				List<Comentario> comentarios = comentarioService.findByIdProposicao(proposicaoPauta.getIdProposicao());
				if (comentarios != null && !comentarios.isEmpty()) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Proposicao " + proposicaoPauta.getIdProposicao() + " já existente no siselgis, atualizando comentarios " + comentarios.size());
					proposicaoPauta.setListaComentario(comentarios);
				}
			}
		}
		return pautas;
	}

	@Override
	public Proposicao detalharProposicaoSenadoWS(Long id) throws IOException {
		return parserProposicaoSenado.getProposicao(id);
	}

	@Override
	public Proposicao detalharProposicaoCamaraWS(Long id) throws IOException {
		return parserProposicaoCamara.getProposicao(id);
	}

	@Override
	public List<Proposicao> listProposicoesSeguidas() {
		List<Proposicao> seguidas = em.createNamedQuery("getAllProposicoesSeguidas", Proposicao.class).getResultList();
		return seguidas;
	}

	@Override
	public int salvarProposicaoIndependente(Proposicao proposicaoFromBusca) {
		// Agora vamos salvar/associar as proposições na reunião
		try {
			Proposicao proposicao = buscarPorId(proposicaoFromBusca.getIdProposicao(), false);

			// Caso a proposição não exista, salvamos ela e associamos a
			// reunião
			if (Objects.isNull(proposicao)) {
				if (proposicaoFromBusca.getOrigem().equals(Origem.CAMARA)) {
					proposicao = detalharProposicaoCamaraWS(Long.valueOf(proposicaoFromBusca.getIdProposicao()));
				} else if (proposicaoFromBusca.getOrigem().equals(Origem.SENADO)) {
					proposicao = detalharProposicaoSenadoWS(Long.valueOf(proposicaoFromBusca.getIdProposicao()));
				}

				save(proposicao);
				return 1;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public Proposicao buscarPorIdProposicao(Integer idProposicao) {
		TypedQuery<Proposicao> findByIdQuery = em.createQuery("SELECT p FROM Proposicao p WHERE p.idProposicao = :idProposicao", Proposicao.class);
		findByIdQuery.setParameter("idProposicao", idProposicao);
		final List<Proposicao> results = findByIdQuery.getResultList();
		if (!Objects.isNull(results) && !results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public boolean isNull(Object obj) {
		return obj == null ? true : false;
	}

	@Override
	public List<Proposicao> listarTodos() {
		List<Proposicao> proposicoes = listAll();
		popularDadosTransientes(proposicoes);
		return proposicoes;
	}

	@Override
	public Proposicao buscarPorId(Integer id, boolean fetchall) {
		Proposicao proposicao = findById(id.longValue());
		if (proposicao != null) {
			popularDadosTransientes(proposicao);
			if (fetchall) {
				populaTodosDados(proposicao);
			}
		}
		return proposicao;
	}

	@Override
	public List<Proposicao> consultar(Map<String, Object> filtros, Integer offset, Integer limit) {
		StringBuilder query = new StringBuilder("SELECT p FROM Proposicao p WHERE 1=1");
		String sigla = (String) filtros.get("sigla");
		String autor = (String) filtros.get("autor");
		String ementa = (String) filtros.get("ementa");
		String origem = (String) filtros.get("origem");
		String isFavorita = (String) filtros.get("isFavorita");
		String estado = (String) filtros.get("estado");
		String macrotema = (String) filtros.get("macrotema");
		Boolean somentePautadas = (Boolean) filtros.get("somentePautadas");
		Long idEquipe = (Long) filtros.get("idEquipe");

		query.append(createWhereClause(sigla, null, autor, ementa, origem, isFavorita, estado, null, idEquipe, null, macrotema, somentePautadas, null));
		query.append(" order by tipo,ano,numero");
		TypedQuery<Proposicao> findByIdQuery = getEntityManager().createQuery(query.toString(), Proposicao.class);

		setParams(sigla, null, autor, ementa, origem, isFavorita, estado, null, idEquipe, null, macrotema, somentePautadas, null, findByIdQuery);
		if (offset != null) {
			findByIdQuery.setFirstResult(offset);
		}
		if (limit != null) {
			findByIdQuery.setMaxResults(limit);
		}
		List<Proposicao> proposicoes = findByIdQuery.getResultList();
		popularDadosTransientes(proposicoes);

		return proposicoes;
	}

	@Override
	public List<Proposicao> consultar(String sigla, String autor, String ementa, String origem, String isFavorita, Integer offset, Integer limit) {
		StringBuilder query = new StringBuilder("SELECT p FROM Proposicao p WHERE 1=1");
		query.append(createWhereClause(sigla, null, autor, ementa, origem, isFavorita, null, null, null));
		query.append(" order by tipo,ano,numero");
		TypedQuery<Proposicao> findByIdQuery = getEntityManager().createQuery(query.toString(), Proposicao.class);

		setParams(sigla, null, autor, ementa, origem, isFavorita, null, null, null, findByIdQuery);

		List<Proposicao> proposicoes = findByIdQuery.setFirstResult(offset).setMaxResults(limit).getResultList();
		popularDadosTransientes(proposicoes);

		return proposicoes;
	}

	private void setParams(String sigla, String comissao, String autor, String ementa, String origem, String isFavorita, Long idResponsavel, Long idPosicionamento, Integer[] idProposicoes, TypedQuery findByIdQuery) {
		setParams(sigla, comissao, autor, ementa, origem, isFavorita, null, idResponsavel, null, idPosicionamento, null, null, null, findByIdQuery);
	}

	private void setParams(String sigla, String comissao, String autor, String ementa, String origem, String isFavorita, String estado, Long idResponsavel, Long idEquipe, Long idPosicionamento, String macrotema, Boolean somentePautadas, Integer[] idProposicoes, TypedQuery findByIdQuery) {
		if (Objects.nonNull(sigla) && !sigla.equals("")) {
			findByIdQuery.setParameter("sigla", "%" + sigla + "%");
		}
		if (Objects.nonNull(somentePautadas) && Boolean.TRUE.equals(somentePautadas)) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			findByIdQuery.setParameter("dataReuniao", c.getTime());
		}
		if (Objects.nonNull(comissao) && !comissao.equals("")) {
			findByIdQuery.setParameter("comissao", comissao + "%");
		}
		if (Objects.nonNull(ementa) && !ementa.equals("")) {
			findByIdQuery.setParameter("ementa", "%" + ementa + "%");
		}
		if (Objects.nonNull(autor) && !autor.equals("")) {
			findByIdQuery.setParameter("autor", "%" + autor + "%");
		}
		if (Objects.nonNull(origem) && !origem.equals("")) {
			findByIdQuery.setParameter("origem", Origem.valueOf(origem));
		}
		if (Objects.nonNull(isFavorita) && !isFavorita.equals("")) {
			findByIdQuery.setParameter("isFavorita", new Boolean(isFavorita));
		}
		if (Objects.nonNull(idPosicionamento) && idPosicionamento != -1) {
			findByIdQuery.setParameter("idPosicionamento", idPosicionamento);
		}
		if (Objects.nonNull(idResponsavel)) {
			findByIdQuery.setParameter("idResponsavel", idResponsavel);
		}
		if (Objects.nonNull(macrotema)) {

			findByIdQuery.setParameter("tag", macrotema);
		}
		if (Objects.nonNull(idEquipe)) {

			findByIdQuery.setParameter("idEquipe", idEquipe);
		}
		if (Objects.nonNull(estado)) {
			findByIdQuery.setParameter("estado", EstadoProposicao.valueOf(estado));
		}

		if (Objects.nonNull(idProposicoes) && idProposicoes.length > 0) {
			List<Integer> idProps = new ArrayList<Integer>(idProposicoes.length);
			for (int i = 0; i < idProposicoes.length; i++) {
				idProps.add(idProposicoes[i]);
			}
			findByIdQuery.setParameter("idProposicao", idProps);
		}
	}

	private StringBuilder createWhereClause(String sigla, String comissao, String autor, String ementa, String origem, String isFavorita, Long idResponsavel, Long idPosicionamento, Integer[] idProposicao) {
		return createWhereClause(sigla, comissao, autor, ementa, origem, isFavorita, null, idResponsavel, null, idPosicionamento, null, null, idProposicao);
	}

	private StringBuilder createWhereClause(String sigla, String comissao, String autor, String ementa, String origem, String isFavorita, String estado, Long idResponsavel, Long idEquipe, Long idPosicionamento, String macroTema, Boolean somentePautadas, Integer[] idProposicao) {
		StringBuilder query = new StringBuilder();
		if (Objects.nonNull(sigla) && !sigla.equals("")) {
			query.append(" AND upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)");
		}
		if (Objects.nonNull(comissao) && !comissao.equals("")) {
			query.append(" AND upper(p.comissao) like upper(:comissao)");
		}
		if (Objects.nonNull(ementa) && !ementa.equals("")) {
			query.append(" AND upper(p.ementa) like upper(:ementa)");
		}
		if (Objects.nonNull(autor) && !autor.equals("")) {
			query.append(" AND upper(p.autor) like upper(:autor)");
		}
		if (Objects.nonNull(origem) && !origem.equals("")) {
			query.append(" AND p.origem = :origem");
		}
		if (Objects.nonNull(estado) && !estado.equals("")) {
			query.append(" AND p.estado = :estado");
		}
		if (Objects.nonNull(isFavorita) && !isFavorita.equals("")) {
			query.append(" AND p.isFavorita = :isFavorita");
		}
		if (Objects.nonNull(somentePautadas) && Boolean.TRUE.equals(somentePautadas)) {
			query.append(" AND  p.ultima.pautaReuniaoComissao.data>:dataReuniao");
		}
		if (Objects.nonNull(idResponsavel)) {
			query.append(" AND p.responsavel.id = :idResponsavel");
		}
		if (Objects.nonNull(macroTema)) {
			query.append(" AND :tag in elements(p.tags)");
		}

		if (Objects.nonNull(idEquipe)) {
			query.append(" AND  p.equipe.id = :idEquipe ");
		}
		if (Objects.nonNull(idPosicionamento)) {
			if (idPosicionamento == -1) {
				query.append(" AND p.posicionamentoAtual is null");
			} else {
				query.append(" AND (p.posicionamentoAtual is not null and p.posicionamentoAtual.posicionamento.id = :idPosicionamento)");
			}
		}
		if (Objects.nonNull(idProposicao) && idProposicao.length > 0) {
			query.append(" AND p.idProposicao in :idProposicao");
		}

		return query;
	}

	@Override
	public Collection<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao) {
		return buscarProposicoesPorDataReuniao(dataReuniao, false);
	}

	@Override
	public Collection<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao, boolean fetchAll) {
		return buscarProposicoesPorDataReuniao(dataReuniao, null, null, null, null, null, null, null, null, fetchAll);
	}

	@Override
	public Collection<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao, String comissao, Long idResponsavel, String origem, String isFavorita, Long idPosicionamento, Integer limit, Integer offset, Integer[] idProposicoes, boolean fetchAll) {
		List<Proposicao> proposicoes = new ArrayList<>();
		Reuniao reuniao = reuniaoService.buscaReuniaoPorData(dataReuniao);
		if (!Objects.isNull(reuniao)) {
			// Para evitar ter que migrar muitos dados, interceptamos requests
			// mais antigos do que a criacao dessas entidade, e convertemos na
			// hora.
			if (dataReuniao.getTime() < 1449100800000l) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Reuniao mais antiga que refactoring, utilizando metodo alternativo");
				StringBuilder queryBuffer = new StringBuilder("select r from Proposicao p, ReuniaoProposicao r where r.reuniao.id=:rid and r.proposicao=p");
				queryBuffer.append(createWhereClause(null, comissao, null, null, origem, isFavorita, idResponsavel, idPosicionamento, idProposicoes));
				queryBuffer.append(" order by p.comissao, p.tipo, p.ano, p.numero");
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINEST, "Query '" + queryBuffer.toString());
				TypedQuery<ReuniaoProposicao> query = em.createQuery(queryBuffer.toString(), ReuniaoProposicao.class);
				query.setParameter("rid", reuniao.getId());
				setParams(null, comissao, null, null, origem, isFavorita, idResponsavel, idPosicionamento, idProposicoes, query);
				if (limit != null) {
					query.setMaxResults(limit);
				}
				if (offset != null) {
					query.setFirstResult(offset);
				}

				List<ReuniaoProposicao> proposicoesReuniao = query.getResultList();
				for (Iterator<ReuniaoProposicao> iterator = proposicoesReuniao.iterator(); iterator.hasNext();) {
					ReuniaoProposicao rp = (ReuniaoProposicao) iterator.next();
					Proposicao p = rp.getProposicao();
					if (p.getPautaComissaoAtual() == null) {
						PautaReuniaoComissao pr = new PautaReuniaoComissao(dataReuniao, rp.getSiglaComissao(), rp.getCodigoReuniao());
						pr.setManual(true);
						ProposicaoPautaComissao ppc = new ProposicaoPautaComissao(pr, p);
						ppc.setOrdemPauta(rp.getSeqOrdemPauta());
						p.getPautasComissoes().add(ppc);
					}

					popularDadosTransientes(p);

					proposicoes.add(p);
				}

			} else {
				StringBuilder queryBuffer = new StringBuilder("SELECT p FROM Proposicao p where p.id in (select r.proposicao.id from ReuniaoProposicao r where  r.reuniao.id=:rid)");

				queryBuffer.append(createWhereClause(null, comissao, null, null, origem, isFavorita, idResponsavel, idPosicionamento, idProposicoes));
				queryBuffer.append(" order by p.comissao, p.tipo, p.ano, p.numero");
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINEST, "Query '" + queryBuffer.toString());

				TypedQuery<Proposicao> query = em.createQuery(queryBuffer.toString(), Proposicao.class);
				query.setParameter("rid", reuniao.getId());
				setParams(null, comissao, null, null, origem, isFavorita, idResponsavel, idPosicionamento, idProposicoes, query);
				if (limit != null) {
					query.setMaxResults(limit);
				}
				if (offset != null) {
					query.setFirstResult(offset);
				}

				query.setParameter("rid", reuniao.getId());
				List<Proposicao> proposicoesReuniao = query.getResultList();
				proposicoes.addAll(proposicoesReuniao);

				popularDadosTransientes(proposicoes);

			}

		}
		if (fetchAll) {
			popularTodosDados(proposicoes);
		}
		return proposicoes;
	}

	private void popularTodosDados(List<Proposicao> proposicoesReuniao) {
		for (Iterator iterator = proposicoesReuniao.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			populaTodosDados(proposicao);
		}

	}

	private void populaTodosDados(Proposicao proposicao) {
		proposicao.setListaComentario(comentarioService.findByProposicaoId(proposicao.getId()));
		proposicao.setTotalComentarios(proposicao.getListaComentario().size());
		proposicao.setListaEncaminhamentoProposicao(new HashSet<EncaminhamentoProposicao>(encaminhamentoProposicaoService.findByProposicao(proposicao.getId())));
		proposicao.setTotalEncaminhamentos(proposicao.getListaEncaminhamentoProposicao().size());
	}

	@Override
	public void deleteById(Long id) {
		List<EncaminhamentoProposicao> listaEnc = encaminhamentoProposicaoService.findByProposicao(id);
		for (Iterator<EncaminhamentoProposicao> iterator = listaEnc.iterator(); iterator.hasNext();) {
			EncaminhamentoProposicao ep = iterator.next();
			encaminhamentoProposicaoService.deleteById(ep.getId());
		}

		List<Comentario> listaCom = comentarioService.findByProposicaoId(id);
		for (Iterator<Comentario> iterator = listaCom.iterator(); iterator.hasNext();) {
			Comentario c = iterator.next();
			comentarioService.deleteById(c.getId());
		}

		super.deleteById(id);
	}

	@Override
	public List<Proposicao> buscarPorSufixo(String sufixo) {
		TypedQuery<Proposicao> query = getEntityManager().createQuery("SELECT p FROM Proposicao p WHERE upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)", Proposicao.class);
		query.setParameter("sigla", "%" + sufixo + "%");
		List<Proposicao> proposicoes = query.getResultList();
		popularDadosTransientes(proposicoes);
		return proposicoes;
	}

	@Override
	public Collection<Proposicao> buscaProposicaoIndependentePor(Origem origem, String tipo, String numero, Integer ano) throws IOException {
		switch (origem) {
		case CAMARA:
			return parserProposicaoCamara.searchProposicao(tipo, numero, ano);
		case SENADO:
			return parserProposicaoSenado.searchProposicao(tipo, numero, ano);

		default:
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Origem não informada");
			throw new IllegalArgumentException("Origem não informada");
		}

	}

	@Override
	public Collection<TipoProposicao> listTipos(Origem origem) throws IOException {
		switch (origem) {
		case CAMARA:
			return parserProposicaoCamara.listaTipos();
		case SENADO:
			return parserProposicaoSenado.listaTipos();

		default:
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Origem não informada");
			throw new IllegalArgumentException("Origem não informada");
		}

	}

	/**
	 * Este comparador checa por alterações na proposição.
	 */
	class ChecaAlteracoesProposicao implements Comparator<Proposicao> {

		StringBuilder descricaoAlteracao;

		@Override
		public int compare(Proposicao local, Proposicao remote) {
			descricaoAlteracao = new StringBuilder();
			if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Comparando Proposicoes ");
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Local:  " + local);
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Remota: " + remote);
			}
			if ((local.getAno() == null && remote.getAno() != null) || (remote.getAno() != null && !local.getAno().equals(remote.getAno()))) {
				descricaoAlteracao.append("Alterado ano: ").append(local.getAno()).append("=>").append(remote.getAno()).append("\n");
				local.setAno(remote.getAno());
			}
			if (((local.getComissao() == null || "AVULSA".equals(local.getComissao())) && remote.getComissao() != null && !remote.getComissao().equals(local.getComissao()))) {
				descricaoAlteracao.append("Alterada comissão: '").append(local.getComissao()).append("' => '").append(remote.getComissao()).append("'\n");
				local.setComissao(remote.getComissao());
			}

			if ((local.getAutor() == null && remote.getAutor() != null) || (remote.getAutor() != null && !local.getAutor().equals(remote.getAutor()))) {
				descricaoAlteracao.append("Alterado autor: '").append(local.getAutor()).append("' => '").append(remote.getAutor()).append("'\n");
				local.setAutor(remote.getAutor());
			}

			if ((local.getEmenta() == null && remote.getEmenta() != null) || !local.getEmenta().equals(remote.getEmenta())) {

				String ementaLocal = null;
				if (local.getEmenta() != null && local.getEmenta().length() > 100) {
					ementaLocal = local.getEmenta().substring(0, 100);
				} else {
					ementaLocal = local.getEmenta();
				}

				String ementaRemota = null;
				if (remote.getEmenta() != null && remote.getEmenta().length() > 100) {
					ementaRemota = remote.getEmenta().substring(0, 100);
				} else {
					ementaRemota = remote.getEmenta();
				}
				descricaoAlteracao.append("Alterada ementa: '").append(ementaLocal).append("' => '").append(ementaRemota).append("'\n");
				local.setEmenta(remote.getEmenta());
			}

			if ((local.getNumero() == null && remote.getNumero() != null) || !local.getNumero().equals(remote.getNumero())) {
				descricaoAlteracao.append("Alterado número: '").append(local.getNumero()).append("' => '").append(remote.getNumero()).append("'\n");
				local.setNumero(remote.getNumero());
			}

			if ((local.getTipo() == null && remote.getTipo() != null) || !local.getTipo().equals(remote.getTipo())) {
				descricaoAlteracao.append("Alterado tipo: '").append(local.getTipo()).append("' => '").append(remote.getTipo()).append("'\n");
				local.setTipo(remote.getTipo());
			}

			if ((local.getSituacao() == null && remote.getSituacao() != null) || !local.getSituacao().equals(remote.getSituacao())) {
				descricaoAlteracao.append("Alterado situação: '").append(local.getSituacao()).append("' => '").append(remote.getSituacao()).append("' \n");
				local.setSituacao(remote.getSituacao());
			}

			return descricaoAlteracao.length();
		}

		public String getDescricaoAlteracao() {
			return descricaoAlteracao.toString();
		}
	}

	private ChecaAlteracoesProposicao checadorAlteracoes = new ChecaAlteracoesProposicao();

	@Override
	public boolean syncDadosProposicao(Long proposicaoLocalId) throws IOException {
		return syncDadosProposicao(findById(proposicaoLocalId));
	}

	@Override
	public boolean syncDadosProposicao(Proposicao proposicaoLocal) throws IOException {
		try {
			ProposicaoSearcher parser = ProposicaoSearcherFactory.getInstance(proposicaoLocal);
			Proposicao proposicaoRemota = parser.getProposicao(proposicaoLocal.getIdProposicao().longValue());

			if (checadorAlteracoes.compare(proposicaoLocal, proposicaoRemota) != 0) {
				AlteracaoProposicao altera = new AlteracaoProposicao(proposicaoLocal, checadorAlteracoes.getDescricaoAlteracao(), new Date());
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Houve alteração da proposicao " + proposicaoLocal.getSigla() + ". A alteração foi " + altera.getDescricaoAlteracao());
				proposicaoLocal.addAlteracao(altera);
				save(proposicaoLocal);
				return true;

			}
		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Falhou ao sincronizar proposicao " + proposicaoLocal, e);
		}
		return false;
	}

	@Override
	public boolean syncDadosPautaProposicao(Long idProposicaoLocal) throws IOException {
		return syncDadosPautaProposicao(findById(idProposicaoLocal));
	}

	@Override
	public boolean syncDadosPautaProposicao(Proposicao proposicaoLocal) throws IOException {
		try {
			Date initialMonday = getClosestMonday(new Date());
			Date nextMonday = getNextWeek(initialMonday);
			Set<PautaReuniaoComissao> props = new HashSet<PautaReuniaoComissao>();
			switch (proposicaoLocal.getOrigem()) {
			case SENADO:
				props = buscarProposicoesPautaSenadoWS(proposicaoLocal.getComissao(), initialMonday, nextMonday);
				break;
			case CAMARA:
				Comissao comissao = comissaoService.getBySigla(proposicaoLocal.getComissao());
				if (comissao == null) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao sincronizar pauta pois nao encontrou comissao com id " + proposicaoLocal.getComissao() + " " + proposicaoLocal);
					return false;
				}
				props = buscarProposicoesPautaCamaraWS(comissao.getId(), initialMonday, nextMonday);
				break;

			default:
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao sincronizar pauta pois origem é desconhecida  " + proposicaoLocal);
				return false;
			}
			for (Iterator<PautaReuniaoComissao> iterator = props.iterator(); iterator.hasNext();) {
				PautaReuniaoComissao pautaReuniaoComissao = (PautaReuniaoComissao) iterator.next();
				for (Iterator<ProposicaoPautaComissao> iterator2 = pautaReuniaoComissao.getProposicoesDaPauta().iterator(); iterator2.hasNext();) {
					ProposicaoPautaComissao ppComissao = (ProposicaoPautaComissao) iterator2.next();
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "buscando proposicao na pauta " + ppComissao.getProposicao().getIdProposicao() + " == " + proposicaoLocal.getIdProposicao());
					if (ppComissao.getProposicao().getIdProposicao().equals(proposicaoLocal.getIdProposicao())) {
						ppComissao.setProposicao(proposicaoLocal);
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "encontrou proposicao na pauta " + ppComissao);
						PautaReuniaoComissao prc = retrievePautaReuniao(pautaReuniaoComissao.getCodigoReuniao());
						if (prc != null) {
							ppComissao.setPautaReuniaoComissao(prc);
							for (Iterator<ProposicaoPautaComissao> iterator3 = prc.getProposicoesDaPauta().iterator(); iterator3.hasNext();) {
								ProposicaoPautaComissao localPPC = (ProposicaoPautaComissao) iterator3.next();
								if (localPPC.getProposicao().getIdProposicao().equals(proposicaoLocal.getIdProposicao())) {
									if (checadorAlteracoesPauta.compare(localPPC, ppComissao) > 0) {
										Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "encontrou diferencas " + ppComissao + " e " + localPPC);
										savePautaReuniaoComissao(prc);
										return true;
									} else {
										Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "nenhuma diff encontrada");
										return false;
									}
								}
							}

						} else {
							Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "pautaReuniaoComissao nova " + pautaReuniaoComissao);
							pautaReuniaoComissao.setProposicoesDaPauta(new TreeSet<ProposicaoPautaComissao>());
							pautaReuniaoComissao.addProposicaoPauta(ppComissao);
							savePautaReuniaoComissao(pautaReuniaoComissao);
							return true;
						}

					}
				}
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Não foi encontrada proposicao na pauta da comissao nesta semana, nada a atualizar");

			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Falhou ao sincronizar proposicao " + proposicaoLocal, e);
		}
		return false;
	}

	@Override
	public boolean syncDadosPautaReuniaoComissao(PautaReuniaoComissao prcLocal) throws IOException {

		try {
			Set<PautaReuniaoComissao> prcRemotoList = null;
			Comissao comissao = comissaoService.getBySigla(prcLocal.getComissao());
			boolean retorno = false;

			switch (prcLocal.getOrigem()) {

			case CAMARA:
				prcRemotoList = buscarProposicoesPautaCamaraWS(comissao.getId(), prcLocal.getData(), prcLocal.getData());
				break;

			case SENADO:
				prcRemotoList = buscarProposicoesPautaSenadoWS(prcLocal.getComissao(), prcLocal.getData(), prcLocal.getData());
				break;

			default:
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao sincronizar pauta da reuniao pois origem e desconhecida  " + prcLocal);
				return false;

			}

			for (PautaReuniaoComissao prcRemoto : prcRemotoList) {
				if (Objects.equals(prcRemoto.getCodigoReuniao(), prcLocal.getCodigoReuniao())) {
					if (checadorAlteracoesPautaReuniao.compare(prcLocal, prcRemoto) > 0) {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "encontrou diferencas nas pautas " + prcLocal + " e " + prcRemoto);
						savePautaReuniaoComissao(prcLocal);
						retorno = true;

					} else {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.ALL, "nenhuma diferenca encontrada entre pautas " + prcLocal + " e " + prcRemoto);
					}

					for (ProposicaoPautaComissao ppcLocal : prcLocal.getProposicoesDaPauta()) {
						for (ProposicaoPautaComissao ppcRemoto : prcRemoto.getProposicoesDaPauta()) {
							if (Objects.equals(ppcLocal.getProposicao().getIdProposicao(), ppcRemoto.getProposicao().getIdProposicao())) {

								if (checadorAlteracoesPauta.compare(ppcLocal, ppcRemoto) > 0) {
									Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "encontrou diferencas em proposicao da pauta " + ppcLocal.getProposicaoId());
									em.merge(ppcLocal);
									retorno = true;
								} else {
									Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.ALL, "nenhuma diferenca encontrada entre " + ppcLocal + " e " + ppcRemoto);
								}
								break;
							}
						}
					}
				}
			}

			return retorno;

		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Falhou ao sincronizar pautaReuniaoComissao " + prcLocal, e);
		}

		return false;
	}

	@Override
	public List<PautaReuniaoComissao> findPautaReuniaoPendentes() {

		List<SituacaoSessao> situacoesEmAberto = new ArrayList<>();
		situacoesEmAberto.add(SituacaoSessao.Agendada);
		situacoesEmAberto.add(SituacaoSessao.Desconhecido);

		Query q = em.createNamedQuery("findPendentes", PautaReuniaoComissao.class);
		q.setParameter("situacoesEmAberto", situacoesEmAberto);
		q.setParameter("date", new Date());
		q.setMaxResults(200);

		List<PautaReuniaoComissao> prcList = q.getResultList();

		return prcList;

	}

	@Override
	public Proposicao save(Proposicao instanciaNova, Usuario user) {
		if (user != null) {
			if (instanciaNova.getId() != null) {
				Proposicao proposicao = findById(instanciaNova.getId());
				if (instanciaNova.getPosicionamentoAtual() == null || instanciaNova.getPosicionamentoAtual().getPosicionamento() == null) {

					instanciaNova.setPosicionamentoAtual(null);
				} else if 
//@formatter:off
				(
					(instanciaNova.getPosicionamentoAtual() != null && proposicao.getPosicionamentoAtual() == null)
					|| (
							(instanciaNova.getPosicionamentoAtual() != null	&& proposicao.getPosicionamentoAtual() != null) 
							&& 
							(
								(proposicao.getPosicionamentoAtual().getPosicionamento()==null && instanciaNova.getPosicionamentoAtual().getPosicionamento()!=null)
								||
								(
										proposicao.getPosicionamentoAtual().getPosicionamento()!=null && instanciaNova.getPosicionamentoAtual().getPosicionamento()!=null &&
									
									!proposicao.getPosicionamentoAtual().equals(instanciaNova.getPosicionamentoAtual().getPosicionamento())
									
									)
							)
						)
				){
				//@formatter:on

					PosicionamentoProposicao posicionamentoProposicao = new PosicionamentoProposicao();
					posicionamentoProposicao.setPosicionamento(instanciaNova.getPosicionamentoAtual().getPosicionamento());
					posicionamentoProposicao.setProposicao(proposicao);
					posicionamentoProposicao.setPreliminar(instanciaNova.getPosicionamentoAtual().isPreliminar());
					posicionamentoProposicao.setUsuario(user);
					em.persist(posicionamentoProposicao);
					instanciaNova.setPosicionamentoAtual(posicionamentoProposicao);
				}
			}
		}

		return super.save(instanciaNova);
	}

	@Override
	public PosicionamentoProposicao alterarPosicionamento(Long id, Long idPosicionamento, boolean preliminar, Usuario usuario) {
		Proposicao proposicao = findById(id);

		// somente executa se o posicionamento for alterado
		if (proposicao.getPosicionamentoAtual() == null || !proposicao.getPosicionamentoAtual().getPosicionamento().getId().equals(idPosicionamento)) {
			Posicionamento posicionamento = null;

			if (idPosicionamento != null) {
				posicionamento = posicionamentoService.findById(idPosicionamento);
				PosicionamentoProposicao posicionamentoProposicao = new PosicionamentoProposicao();
				posicionamentoProposicao.setPosicionamento(posicionamento);
				posicionamentoProposicao.setProposicao(proposicao);
				posicionamentoProposicao.setPreliminar(preliminar);
				posicionamentoProposicao.setUsuario(usuario);
				em.persist(posicionamentoProposicao);
				proposicao.setPosicionamentoAtual(posicionamentoProposicao);
			} else {
				proposicao.setPosicionamentoAtual(null);
			}
			save(proposicao, null);
			return proposicao.getPosicionamentoAtual();
		}
		return null;
	}

	@Override
	public List<PosicionamentoProposicao> listarHistoricoPosicionamentos(Long id) {
		TypedQuery<PosicionamentoProposicao> query = em.createQuery("FROM PosicionamentoProposicao pp WHERE pp.proposicao.id = :id ORDER BY pp.dataCriacao ", PosicionamentoProposicao.class);

		query.setParameter("id", id);

		List<PosicionamentoProposicao> posicionamentosProposicao = query.getResultList();
		return posicionamentosProposicao;
	}

	@Override
	public void setRoadmapComissoes(Long idProposicao, List<String> comissoes) {
		Proposicao proposicao = findById(idProposicao);

		if (proposicao == null) {
			throw new IllegalArgumentException("Tentativa de inserir roadmap para proposicao nao encontrada. Id: " + idProposicao);
		}

		for (RoadmapComissao roadmapComissao : proposicao.getRoadmapComissoes()) {
			em.remove(roadmapComissao);
		}

		RoadmapComissao roadmapComissao;
		int ordem = 1;
		for (String comissao : comissoes) {
			roadmapComissao = new RoadmapComissao();
			roadmapComissao.setProposicao(proposicao);
			roadmapComissao.setComissao(comissao);
			roadmapComissao.setOrdem(ordem);
			roadmapComissao.setProposicaoId(idProposicao);

			em.persist(roadmapComissao);
			ordem++;
		}
	}

	@Override
	public ProcessoSei vincularProcessoSei(Long id, String protocolo) throws ServiceException, RemoteException {
		Proposicao proposicao = findById(id);

		ProcessoSei processoSei = new ProcessoSei();
		processoSei.setProtocolo(protocolo);
		processoSei.setProposicao(proposicao);

		SeiServiceLocator seiServiceLocator = new SeiServiceLocator();
		RetornoConsultaProcedimento retornoConsultaProcedimento = seiServiceLocator.getSeiPortService().consultarProcedimento("sislegis", "sislegis", null, protocolo, null, null, null, null, null, null, null, null, null);

		if (retornoConsultaProcedimento != null) {
			processoSei.setLinkSei(retornoConsultaProcedimento.getLinkAcesso());
			em.persist(processoSei);
			return processoSei;
		} else {
			throw new IllegalArgumentException("Processo nao encontrado no SEI. Protocolo: " + protocolo);
		}
	}

	@Override
	public void excluirProcessoSei(Long idProcesso) {
		ProcessoSei processoSei = em.find(ProcessoSei.class, idProcesso);
		em.remove(processoSei);
	}

	/**
	 * Este comparador checa por alterações na proposição.
	 */
	class ChecaAlteracoesPautaProposicao implements Comparator<ProposicaoPautaComissao> {

		StringBuilder descricaoAlteracao;

		@Override
		public int compare(ProposicaoPautaComissao local, ProposicaoPautaComissao remote) {
			descricaoAlteracao = new StringBuilder();
			if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Comparando ProposicaoPautaComissao ");
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Local:  " + local);
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Remota: " + remote);
			}
			if ((local.getRelator() == null && remote.getRelator() != null) || (remote.getRelator() != null && !local.getRelator().equals(remote.getRelator()))) {
				descricaoAlteracao.append("Alterado Relator: '").append(local.getRelator()).append("' => '").append(remote.getRelator()).append("'\n");
				local.setRelator(remote.getRelator());
			}
			if ((local.getOrdemPauta() == null && remote.getOrdemPauta() != null) || (remote.getOrdemPauta() != null && !local.getOrdemPauta().equals(remote.getOrdemPauta()))) {
				descricaoAlteracao.append("Alterado Ordem pauta: '").append(local.getOrdemPauta()).append("' => '").append(remote.getOrdemPauta()).append("'\n");
				local.setOrdemPauta(remote.getOrdemPauta());
			}
			if ((local.getResultado() == null && remote.getResultado() != null) || (remote.getResultado() != null && !local.getResultado().equals(remote.getResultado()))) {
				descricaoAlteracao.append("Alterado Resultado: '").append(local.getResultado()).append("' => '").append(remote.getResultado()).append("'\n");
				local.setResultado(remote.getResultado());
			}
			return descricaoAlteracao.length();
		}

		public String getDescricaoAlteracao() {
			return descricaoAlteracao.toString();
		}
	}

	/**
	 * Este comparador checa por alterações na pauta reuniao.
	 */
	class ChecaAlteracoesPautaReuniao implements Comparator<PautaReuniaoComissao> {

		StringBuilder descricaoAlteracao;

		@Override
		public int compare(PautaReuniaoComissao local, PautaReuniaoComissao remote) {
			descricaoAlteracao = new StringBuilder();
			if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Comparando PautaReuniaoComissao ");
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Local:  " + local);
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Remota: " + remote);
			}
			if ((local.getSituacao() == null && remote.getSituacao() != null) || (remote.getSituacao() != null && !local.getSituacao().equals(remote.getSituacao()))) {
				descricaoAlteracao.append("Alterada Situacao: '").append(local.getSituacao()).append("' => '").append(remote.getSituacao()).append("'\n");
				local.setSituacao(remote.getSituacao());
			}

			return descricaoAlteracao.length();
		}

		public String getDescricaoAlteracao() {
			return descricaoAlteracao.toString();
		}
	}

	private ChecaAlteracoesPautaProposicao checadorAlteracoesPauta = new ChecaAlteracoesPautaProposicao();

	private ChecaAlteracoesPautaReuniao checadorAlteracoesPautaReuniao = new ChecaAlteracoesPautaReuniao();

	@Override
	public void followProposicao(Usuario user, Long idProposicao) {
		user = usuarioService.findById(user.getId());
		Proposicao prop = findById(idProposicao);
		user.getProposicoesSeguidas().add(prop);
		usuarioService.save(user);

	}

	@Override
	public void unfollowProposicao(Usuario user, Long idProposicao) {
		user = usuarioService.findById(user.getId());
		Proposicao prop = findById(idProposicao);
		user.getProposicoesSeguidas().remove(prop);
		usuarioService.save(user);

	}

	private Integer totalProposicaoPautaComissaoByProposicao(Long idProposicao) {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM proposicao_pautacomissao WHERE proposicaoid = :idProposicao ");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

	private void popularDadosTransientes(Proposicao proposicao) {
		if (proposicao != null) {
			proposicao.setTotalComentarios(comentarioService.totalByProposicao(proposicao.getId()));
			proposicao.setTotalEncaminhamentos(encaminhamentoProposicaoService.totalByProposicao(proposicao.getId()));
			proposicao.setTotalPautasComissao(totalProposicaoPautaComissaoByProposicao(proposicao.getId()));
			proposicao.setTotalNotasTecnicas(getNotaTecnicas(proposicao.getId()).size());
			proposicao.setTotalBriefings(getBriefings(proposicao.getId()).size());
			proposicao.setTotalEmendas(getEmendas(proposicao.getId()).size());
			proposicao.setTotalParecerAreaMerito(areaMeritoService.listRevisoesProposicao(proposicao.getId()).size());

			// PosicionamentoProposicao posicionamentoProposicao;
			// try {
			// TypedQuery<PosicionamentoProposicao> query = em.createQuery(
			// "FROM PosicionamentoProposicao WHERE proposicao.id = :id ORDER BY dataCriacao DESC ",
			// PosicionamentoProposicao.class);
			//
			// query.setParameter("id", proposicao.getId());
			// query.setMaxResults(1);
			//
			// posicionamentoProposicao = query.getSingleResult();
			// } catch (NoResultException e) {
			// Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE,
			// "Nenhum posicionamento encontrado no historico, para a proposicao id: "
			// + proposicao.getId());
			// posicionamentoProposicao = null;
			// }
			//
			// if (posicionamentoProposicao != null) {
			// proposicao.setPosicionamentoAtual(posicionamentoProposicao);
			// proposicao.setPosicionamentoPreliminar(posicionamentoProposicao.isPreliminar());
			// }

		}
	}

	private void popularDadosTransientes(List<Proposicao> proposicoes) {
		for (Proposicao proposicao : proposicoes) {
			popularDadosTransientes(proposicao);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public PautaReuniaoComissao savePautaReuniaoComissao(PautaReuniaoComissao pautaReuniaoComissao) throws IOException {
		// check se proposicoes existem

		Set<ProposicaoPautaComissao> additionalProposicoes = new HashSet<ProposicaoPautaComissao>(pautaReuniaoComissao.getProposicoesDaPauta());
		PautaReuniaoComissao prc = findPautaReuniao(pautaReuniaoComissao.getComissao(), pautaReuniaoComissao.getData(), pautaReuniaoComissao.getCodigoReuniao());
		boolean existing = false;
		if (prc != null) {
			existing = true;
			pautaReuniaoComissao = prc;
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Pauta reuniao ja existia " + pautaReuniaoComissao.getProposicoesDaPauta().size());
		} else {
			getEntityManager().persist(pautaReuniaoComissao);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Criou pauta reuniao " + pautaReuniaoComissao.getId());
		}

		for (Iterator<ProposicaoPautaComissao> iterator = additionalProposicoes.iterator(); iterator.hasNext();) {
			ProposicaoPautaComissao proposicaoPautaComissao = (ProposicaoPautaComissao) iterator.next();
			if (existing) {
				if (pautaReuniaoComissao.getProposicoesDaPauta().contains(proposicaoPautaComissao)) {

					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Ja continha " + proposicaoPautaComissao.getProposicao());
					continue;
				}
			}

			proposicaoPautaComissao.setPautaReuniaoComissao(prc);

			Proposicao proposicao = proposicaoPautaComissao.getProposicao();
			if (proposicaoPautaComissao.getProposicao().getId() == null) {
				Proposicao proposicaoDb = findProposicaoBy(proposicao.getOrigem(), proposicao.getIdProposicao());
				if (proposicaoDb == null) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Proposicao nao existia no banco " + proposicao.getIdProposicao());
					// TEM Que buscar dos WS
					switch (proposicao.getOrigem()) {
					case CAMARA:
						proposicaoDb = detalharProposicaoCamaraWS((long) proposicao.getIdProposicao());
						if (proposicaoDb == null) {
							throw new IllegalArgumentException("Não foi possível encontrar proposicao da " + proposicao.getIdProposicao() + " " + proposicao.getOrigem() + " " + proposicao.getComissao());
						}
						break;
					case SENADO:
						proposicaoDb = detalharProposicaoSenadoWS((long) proposicao.getIdProposicao());
						break;
					}
					if (proposicaoDb.getComissao() == null || !proposicaoDb.getComissao().trim().equals(pautaReuniaoComissao.getComissao())) {
						proposicaoDb.setComissao(pautaReuniaoComissao.getComissao());
					}

					proposicaoDb = save(proposicaoDb);
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Proposicao criada " + proposicaoDb.getId());
					proposicaoPautaComissao.setProposicao(proposicaoDb);

				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Proposicao ja existia no banco, " + proposicaoDb.getComissao() + ": " + pautaReuniaoComissao.getComissao());
					proposicaoDb.setComissao(pautaReuniaoComissao.getComissao());
					if (proposicaoDb.getComissao() == null || !proposicaoDb.getComissao().trim().equals(pautaReuniaoComissao.getComissao())) {
						proposicaoDb.setComissao(pautaReuniaoComissao.getComissao());
					}
					proposicaoPautaComissao.setProposicao(proposicaoDb);

				}
			} else {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Proposicao ja persistida " + proposicaoPautaComissao);

				proposicaoPautaComissao.getProposicao();
			}
			proposicaoPautaComissao.setPautaReuniaoComissao(pautaReuniaoComissao);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Vai persistir proposicaoPautaComissao " + proposicaoPautaComissao + "  == " + proposicaoPautaComissao.getPautaReuniaoComissaoId() + " -- " + pautaReuniaoComissao);

			getEntityManager().persist(proposicaoPautaComissao);
			pautaReuniaoComissao.addProposicaoPauta(proposicaoPautaComissao);
		}
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Agora salvar " + pautaReuniaoComissao);
		getEntityManager().merge(pautaReuniaoComissao);
		return pautaReuniaoComissao;

	}

	@Override
	public PautaReuniaoComissao retrievePautaReuniao(Integer codigoReuniao) {
		Query q = em.createNamedQuery("findByCodigoReuniao", PautaReuniaoComissao.class);
		q.setParameter("codigoReuniao", codigoReuniao);

		List<PautaReuniaoComissao> props = q.getResultList();
		if (props.isEmpty()) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "nenhuma pauta encontrada " + codigoReuniao);

			return null;
		} else {
			if (props.size() > 1) {
				throw new IllegalArgumentException("Mais de uma PautaReuniaoComissao com codigoReuniao=" + codigoReuniao);
			}
			return props.get(0);

		}
	}

	@Override
	public Proposicao findProposicaoBy(Origem origem, Integer idProposicao) {
		Query q = em.createNamedQuery("findByUniques", Proposicao.class);
		q.setParameter("origem", origem);
		q.setParameter("idProposicao", idProposicao);

		List<Proposicao> props = q.getResultList();
		if (props.isEmpty()) {
			return null;
		} else {
			if (props.size() > 1) {
				throw new IllegalArgumentException("Mais de uma proposicao com id=" + idProposicao + " e origem " + origem.name());
			}
			return props.get(0);

		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void adicionaProposicoesReuniao(Set<PautaReuniaoComissao> pautaReunioes, Reuniao reuniao) throws IOException {
		if (reuniao.getId() == null) {
			reuniao = reuniaoService.save(reuniao);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Criou reuniao para o dia " + reuniao.getData());
		}

		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Salvando " + pautaReunioes.size() + " pautas e suas proposicoes");
		for (Iterator<PautaReuniaoComissao> iterator = pautaReunioes.iterator(); iterator.hasNext();) {

			PautaReuniaoComissao prc = iterator.next();
			Set<ProposicaoPautaComissao> proposicoesParaEstaReuniao = new HashSet<ProposicaoPautaComissao>(prc.getProposicoesDaPauta());
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Criando pautareuniao na base");

			prc = savePautaReuniaoComissao(prc);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Associando suas proposicoes a reuniao, ha: " + prc.getProposicoesDaPauta().size() + " mas para essa reuniao temos " + pautaReunioes.size());
			for (Iterator<ProposicaoPautaComissao> proposicoesIterator = proposicoesParaEstaReuniao.iterator(); proposicoesIterator.hasNext();) {
				ProposicaoPautaComissao ppc = proposicoesIterator.next();
				Proposicao prop = findProposicaoBy(ppc.getProposicao().getOrigem(), ppc.getProposicao().getIdProposicao());
				if (prop == null) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Erro, não persistiu a proposicao " + ppc.getProposicao());
					continue;
				}

				if (reuniaoProposicaoService.findById(reuniao.getId(), prop.getId()) == null) {

					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Associando " + prop + " " + reuniao);
					associateReuniaoProposicao(reuniao, prop);
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Ja estava associada " + ppc.getProposicaoId() + " " + reuniao);
				}
			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void associateReuniaoProposicao(Reuniao reuniao, Proposicao proposicao) {
		ReuniaoProposicao rp = new ReuniaoProposicao();
		ReuniaoProposicaoPK reuniaoProposicaoPK = new ReuniaoProposicaoPK();
		reuniaoProposicaoPK.setIdReuniao(reuniao.getId());
		reuniaoProposicaoPK.setIdProposicao(proposicao.getId());
		rp.setReuniaoProposicaoPK(reuniaoProposicaoPK);
		rp.setReuniao(reuniao);
		rp.setProposicao(proposicao);
		reuniaoProposicaoService.save(rp);
	}

	@Override
	public PautaReuniaoComissao findPautaReuniao(String comissao, Date date, Integer codigoReuniao) {
		Query q = em.createNamedQuery("findByComissaoDataOrigem").setParameter("comissao", comissao).setParameter("data", date).setParameter("codigoReuniao", codigoReuniao);
		List<PautaReuniaoComissao> res = q.getResultList();
		if (!res.isEmpty()) {
			return res.get(0);
		}

		return null;

	}

	@Override
	public List<Votacao> listarVotacoes(Integer idProposicao, String tipo, String numero, String ano, Origem origem) throws Exception {

		if (Origem.CAMARA.equals(origem)) {
			return parserVotacaoCamara.votacoesPorProposicao(numero, ano, tipo);
		} else if (Origem.SENADO.equals(origem)) {
			return parserVotacaoSenado.votacoesPorProposicao(idProposicao);
		}

		return null;
	}

	@Override
	public List<NotaTecnica> getNotaTecnicas(Long proposicaoId) {
		Query q = em.createNamedQuery("listNotatecnicaProposicao").setParameter("idProposicao", proposicaoId);
		List<NotaTecnica> res = q.getResultList();

		return res;
	}

	@Override
	public List<Emenda> getEmendas(Long proposicaoId) {
		Query q = em.createNamedQuery("listEmendasProposicao", Emenda.class).setParameter("idProposicao", proposicaoId);
		List<Emenda> res = q.getResultList();

		return res;
	}

	@Override
	public List<Briefing> getBriefings(Long proposicaoId) {
		Query q = em.createNamedQuery("listBriefingProposicao").setParameter("idProposicao", proposicaoId);
		List<Briefing> res = q.getResultList();

		return res;
	}

	@Override
	public void saveNotaTecnica(NotaTecnica nt) {
		if (nt.getId() != null) {
			em.merge(nt);
		} else {
			em.persist(nt);
		}

	}

	@Override
	public void saveDocRelated(DocRelated nt) {
		if (nt.getId() != null) {
			em.merge(nt);
		} else {
			em.persist(nt);
		}

	}

	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		this.parserProposicaoCamara = (ParserProposicaoCamara) injections[1];
		this.reuniaoService = (ReuniaoService) injections[2];
		this.reuniaoProposicaoService = (ReuniaoProposicaoService) injections[3];
		this.comissaoService = (ComissaoService) injections[4];
		comentarioService = (ComentarioService) injections[5];
		encaminhamentoProposicaoService = (EncaminhamentoProposicaoService) injections[6];
		areaMeritoService = (AreaDeMeritoService) injections[7];
		parserPautaCamara = new ParserPautaCamara();
		parserPautaSenado = new ParserPautaSenado();

	}

	@Override
	public void deleteDocRelated(Long idNota, Class c) {
		getEntityManager().remove(getEntityManager().find(c, idNota));

	}

	@Override
	public Proposicao persistProposicaoAndPauta(Proposicao proposicao, PautaReuniaoComissao prc) throws Exception {
		Proposicao proposicaoLocal = buscarPorIdProposicao(proposicao.getIdProposicao());
		int result = -3;
		if (proposicaoLocal == null) {
			result = salvarProposicaoIndependente(proposicao);
		} else {
			proposicao = proposicaoLocal;
		}

		if (prc != null) {
			try {
				SortedSet<ProposicaoPautaComissao> ppcs = new TreeSet<ProposicaoPautaComissao>();
				for (Iterator<ProposicaoPautaComissao> iterator2 = prc.getProposicoesDaPauta().iterator(); iterator2.hasNext();) {
					ProposicaoPautaComissao type = (ProposicaoPautaComissao) iterator2.next();
					if (type.getProposicao().getIdProposicao().equals(proposicao.getIdProposicao())) {
						ppcs.add(type);
						break;
					}
				}
				prc.setProposicoesDaPauta(ppcs);
				savePautaReuniaoComissao(prc);
				syncDadosPautaReuniaoComissao(prc);
				return buscarPorIdProposicao(proposicao.getIdProposicao());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (proposicao.getComissao() != null && !proposicao.getComissao().isEmpty()) {
				Comissao comissao = new Comissao();
				if (Origem.CAMARA.equals(proposicao.getOrigem())) {
					comissao = comissaoService.getBySigla(proposicao.getComissao());
				} else {
					comissao.setSigla(proposicao.getComissao());
				}

				syncPautaAtualComissao(proposicao.getOrigem(), comissao);
			}
		}
		return proposicao;
	}

	@Override
	public void syncPautaAtualComissao(Origem origem, Comissao comissao) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.add(Calendar.DAY_OF_YEAR, -1);
		Calendar dataFinal = (Calendar) dataInicial.clone();
		dataFinal.add(Calendar.WEEK_OF_YEAR, 1);

		try {
			Set<PautaReuniaoComissao> pss = null;
			if (Origem.SENADO.equals(origem)) {
				ParserPautaSenado parserSenado = new ParserPautaSenado();
				ParserPlenarioSenado parserPlenarioSenado = new ParserPlenarioSenado();

				if (comissao.getSigla().equals("PLEN")) {
					pss = parserPlenarioSenado.getProposicoes(Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"));
				} else {
					pss = parserSenado.getPautaComissao(comissao.getSigla(), Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"), Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
				}

			} else {
				ParserPautaCamara parserPautaCamara = new ParserPautaCamara();

				pss = parserPautaCamara.getPautaComissao(comissao.getSigla(), comissao.getId(), Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"), Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
			}
			persistePautaReuniaoComissao(pss, origem);
		} catch (IOException e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao process comissao " + comissao + " : " + e.getMessage());
			if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao process comissao " + comissao + " : " + e.getMessage());
			} else {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao process comissao " + comissao + " : " + e.getMessage());
			}
		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao process comissao " + comissao, e);
		}

	}

	/**
	 * Persiste pautareuniao e associa as proposições SOMENTE se elas já
	 * existirem no SISLEGIS
	 * 
	 * @param pss
	 * @param origem
	 * @throws IOException
	 */
	private void persistePautaReuniaoComissao(Set<PautaReuniaoComissao> pss, Origem origem) throws IOException {

		for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
			PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
			SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
			for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3.hasNext();) {
				ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
				Proposicao p = findProposicaoBy(origem, propPauta.getProposicao().getIdProposicao());
				if (p != null) {
					propPauta.setProposicao(p);
					paraSalvar.add(propPauta);
				}

			}
			if (!paraSalvar.isEmpty()) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Salvando " + object);
				object = savePautaReuniaoComissaoDeProposicao(object);
				for (Iterator<ProposicaoPautaComissao> iterator = paraSalvar.iterator(); iterator.hasNext();) {
					ProposicaoPautaComissao proposicaoPautaComissao = (ProposicaoPautaComissao) iterator.next();
					proposicaoPautaComissao.setPautaReuniaoComissao(object);
					PropostaPautaPK pk = new PropostaPautaPK();
					pk.setPautaReuniaoComissaoId(object.getId());
					Long idProposicao = proposicaoPautaComissao.getProposicao().getId();
					pk.setProposicaoId(idProposicao);
					ProposicaoPautaComissao dbPautada = getEntityManager().find(ProposicaoPautaComissao.class, pk);
					if (dbPautada == null) {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "Salvanado proposicao pauta reunia inexistente " + proposicaoPautaComissao);
						getEntityManager().persist(proposicaoPautaComissao);
					} else {
						if (checadorAlteracoesPauta.compare(dbPautada, proposicaoPautaComissao) > 0) {
							Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "encontrou diferencas em proposicao da pauta " + dbPautada.getProposicaoId());
							getEntityManager().merge(dbPautada);
						} else {
							Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "nenhuma diferenca encontrada entre " + dbPautada + " e " + proposicaoPautaComissao);
						}

					}

				}

				// object.setProposicoesDaPauta(paraSalvar);
			}

		}
	}

	public PautaReuniaoComissao savePautaReuniaoComissaoDeProposicao(PautaReuniaoComissao pautaReuniaoComissao) throws IOException {
		PautaReuniaoComissao prc = findPautaReuniao(pautaReuniaoComissao.getComissao(), pautaReuniaoComissao.getData(), pautaReuniaoComissao.getCodigoReuniao());
		if (prc != null) {
			pautaReuniaoComissao = prc;
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "Pauta reuniao ja existia " + pautaReuniaoComissao.getProposicoesDaPauta().size());
		} else {
			getEntityManager().persist(pautaReuniaoComissao);
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "Criou pauta reuniao " + pautaReuniaoComissao.getId());
		}
		return pautaReuniaoComissao;
	}
}
