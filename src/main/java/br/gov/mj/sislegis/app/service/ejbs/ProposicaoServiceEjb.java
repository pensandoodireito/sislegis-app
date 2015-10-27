package br.gov.mj.sislegis.app.service.ejbs;

import java.io.IOException;
import java.util.ArrayList;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AlteracaoProposicao;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicaoPK;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcherFactory;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoService;
import br.gov.mj.sislegis.app.service.TagService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.Conversores;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class ProposicaoServiceEjb extends AbstractPersistence<Proposicao, Long> implements ProposicaoService,
		EJBUnitTestable {

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
	private ComentarioService comentarioService;

	@Inject
	private ReuniaoService reuniaoService;

	@Inject
	private ReuniaoProposicaoService reuniaoProposicaoService;

	@Inject
	private EncaminhamentoProposicaoService encaminhamentoProposicaoService;

	@Inject
	private TagService tagService;

	@Inject
	private UsuarioService usuarioService;

	@PersistenceContext
	private EntityManager em;

	public ProposicaoServiceEjb() {
		super(Proposicao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Proposicao> buscarProposicoesPautaCamaraWS(Map parametros) throws Exception {
		Long idComissao = (Long) parametros.get("idComissao");
		String dataIni = Conversores.dateToString((Date) parametros.get("data"), "yyyyMMdd");
		String dataFim = Conversores.dateToString(SislegisUtil.getFutureDate(), "yyyyMMdd");
		return parserPautaCamara.getProposicoes(idComissao, dataIni, dataFim);
	}

	@Override
	public List<Proposicao> buscarProposicoesPautaSenadoWS(Map parametros) throws Exception {
		String siglaComissao = (String) parametros.get("siglaComissao");
		String dataIni = Conversores.dateToString((Date) parametros.get("data"), "yyyyMMdd");

		if (siglaComissao.equals("PLEN")) {
			return parserPlenarioSenado.getProposicoes(dataIni);
		}

		return parserPautaSenado.getProposicoes(siglaComissao, dataIni);
	}

	@Override
	public Proposicao detalharProposicaoSenadoWS(Long id) throws Exception {
		return parserProposicaoSenado.getProposicao(id);
	}

	@Override
	public Proposicao detalharProposicaoCamaraWS(Long id) throws Exception {
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
			Proposicao proposicao = buscarPorId(proposicaoFromBusca.getIdProposicao());

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

	public void salvarListaProposicao(Reuniao reuniao, List<Proposicao> listaProposicao) {

		// Caso a reunião não exista, salva pela primeira vez
		if (Objects.isNull(reuniao.getId())) {
			reuniao = reuniaoService.save(reuniao);
		}

		// Agora vamos salvar/associar as proposições na reunião
		for (Proposicao proposicaoFromBusca : listaProposicao) {
			try {
				Proposicao proposicao = buscarPorId(proposicaoFromBusca.getIdProposicao());

				// Caso a proposição não exista, salvamos ela e associamos a
				// reunião
				if (Objects.isNull(proposicao)) {
					if (proposicaoFromBusca.getOrigem().equals(Origem.CAMARA)) {
						proposicao = detalharProposicaoCamaraWS(Long.valueOf(proposicaoFromBusca.getIdProposicao()));
					} else if (proposicaoFromBusca.getOrigem().equals(Origem.SENADO)) {
						proposicao = detalharProposicaoSenadoWS(Long.valueOf(proposicaoFromBusca.getIdProposicao()));
					}

					save(proposicao);

					ReuniaoProposicao rp = getReuniaoProposicao(reuniao, proposicaoFromBusca, proposicao);

					reuniaoProposicaoService.save(rp);
				} else { // proposição já existe
					ReuniaoProposicao reuniaoProposicao = reuniaoProposicaoService.findById(reuniao.getId(),
							proposicao.getId());

					// Se a proposição não existe na reunião, associamos ela
					if (reuniaoProposicao == null) {
						ReuniaoProposicao rp = getReuniaoProposicao(reuniao, proposicaoFromBusca, proposicao);

						reuniaoProposicaoService.save(rp);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private ReuniaoProposicao getReuniaoProposicao(Reuniao reuniao, Proposicao proposicaoFromBusca,
			Proposicao proposicao) {
		ReuniaoProposicao rp = new ReuniaoProposicao();
		ReuniaoProposicaoPK reuniaoProposicaoPK = new ReuniaoProposicaoPK();
		reuniaoProposicaoPK.setIdReuniao(reuniao.getId());
		reuniaoProposicaoPK.setIdProposicao(proposicao.getId());

		rp.setReuniaoProposicaoPK(reuniaoProposicaoPK);
		rp.setSiglaComissao(proposicaoFromBusca.getComissao());
		rp.setSeqOrdemPauta(proposicaoFromBusca.getSeqOrdemPauta());
		rp.setLinkPauta(proposicaoFromBusca.getLinkPauta());
		rp.setReuniao(reuniao);
		rp.setProposicao(proposicao);
		return rp;
	}

	public boolean isNull(Object obj) {
		return obj == null ? true : false;
	}

	@Override
	public List<Proposicao> listarTodos() {
		List<Proposicao> proposicoes = listAll();
		popularTotalComentariosEncaminhamentos(proposicoes);
		return proposicoes;
	}

	@Override
	public List<Proposicao> consultar(String sigla, String autor, String ementa, String origem, String isFavorita,
			Integer offset, Integer limit) {
		StringBuilder query = new StringBuilder("SELECT p FROM Proposicao p WHERE 1=1");
		if (Objects.nonNull(sigla) && !sigla.equals("")) {
			query.append(" AND upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)");
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
		if (Objects.nonNull(isFavorita) && !isFavorita.equals("")) {
			query.append(" AND p.isFavorita = :isFavorita");
		}

		TypedQuery<Proposicao> findByIdQuery = getEntityManager().createQuery(query.toString(), Proposicao.class);

		if (Objects.nonNull(sigla) && !sigla.equals("")) {
			findByIdQuery.setParameter("sigla", "%" + sigla + "%");
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

		List<Proposicao> proposicoes = findByIdQuery.setFirstResult(offset).setMaxResults(limit).getResultList();
		popularTotalComentariosEncaminhamentos(proposicoes);

		return proposicoes;
	}

	@Override
	public Proposicao buscarPorId(Integer id) {
		Proposicao proposicao = findById(id.longValue());
		if (proposicao != null) {
			popularTotalComentariosEncaminhamentos(proposicao);
		}
		return proposicao;
	}

	@Override
	public Collection<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao) {
		List<Proposicao> proposicoes = new ArrayList<>();
		Reuniao reuniao = reuniaoService.buscaReuniaoPorData(dataReuniao);
		if (!Objects.isNull(reuniao)) {
			proposicoes.addAll(reuniao.getProposicoes());
		}
		// if (!Objects.isNull(reuniao)) {
		// Set<ReuniaoProposicao> listaReuniaoProposicoes =
		// reuniao.getListaReuniaoProposicoes();
		// // Copiamos alguns valores de ReuniaoProposicao para Proposicao,
		// // afim de retornar somente uma entidade com alguns valores
		// // transientes
		// for (ReuniaoProposicao reuniaoProposicao : listaReuniaoProposicoes) {
		// Proposicao proposicao = reuniaoProposicao.getProposicao();
		// proposicao.setComissao(reuniaoProposicao.getSiglaComissao());
		// proposicao.setSeqOrdemPauta(reuniaoProposicao.getSeqOrdemPauta());
		// proposicao.setLinkPauta(reuniaoProposicao.getLinkPauta());
		// proposicao.setReuniao(reuniaoProposicao.getReuniao());
		//
		// popularTotalComentariosEncaminhamentos(proposicao);
		//
		// proposicoes.add(proposicao);
		// }
		// }

		return proposicoes;
	}

	@Override
	public Proposicao save(Proposicao entity) {
		return super.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		List<EncaminhamentoProposicao> listaEnc = encaminhamentoProposicaoService.findByProposicao(id);
		for (Iterator<EncaminhamentoProposicao> iterator = listaEnc.iterator(); iterator.hasNext();) {
			EncaminhamentoProposicao ep = iterator.next();
			encaminhamentoProposicaoService.deleteById(ep.getId());
		}

		List<Comentario> listaCom = comentarioService.findByIdProposicao(id);
		for (Iterator<Comentario> iterator = listaCom.iterator(); iterator.hasNext();) {
			Comentario c = iterator.next();
			comentarioService.deleteById(c.getId());
		}

		super.deleteById(id);
	}

	@Override
	public List<Proposicao> buscarPorSufixo(String sufixo) {
		TypedQuery<Proposicao> query = getEntityManager().createQuery(
				"SELECT p FROM Proposicao p WHERE upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)",
				Proposicao.class);
		query.setParameter("sigla", "%" + sufixo + "%");
		List<Proposicao> proposicoes = query.getResultList();
		popularTotalComentariosEncaminhamentos(proposicoes);
		return proposicoes;
	}

	@Override
	public Collection<Proposicao> buscaProposicaoIndependentePor(Origem origem, String tipo, Integer numero, Integer ano)
			throws IOException {
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
			if ((local.getAno() == null && remote.getAno() != null)
					|| (remote.getAno() != null && !local.getAno().equals(remote.getAno()))) {
				descricaoAlteracao.append("Alterado ano: ").append(local.getAno()).append("=>").append(remote.getAno())
						.append("\n");
				local.setAno(remote.getAno());
			}

			if ((local.getAutor() == null && remote.getAutor() != null)
					|| (remote.getAutor() != null && !local.getAutor().equals(remote.getAutor()))) {
				descricaoAlteracao.append("Alterado autor: '").append(local.getAutor()).append("' => '")
						.append(remote.getAutor()).append("'\n");
				local.setAutor(remote.getAutor());
			}

			if ((local.getEmenta() == null && remote.getEmenta() != null)
					|| !local.getEmenta().equals(remote.getEmenta())) {

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
				descricaoAlteracao.append("Alterada ementa: '").append(ementaLocal).append("' => '")
						.append(ementaRemota).append("'\n");
				local.setEmenta(remote.getEmenta());
			}

			if ((local.getNumero() == null && remote.getNumero() != null)
					|| !local.getNumero().equals(remote.getNumero())) {
				descricaoAlteracao.append("Alterado número: '").append(local.getNumero()).append("' => '")
						.append(remote.getNumero()).append("'\n");
				local.setNumero(remote.getNumero());
			}

			if ((local.getTipo() == null && remote.getTipo() != null) || !local.getTipo().equals(remote.getTipo())) {
				descricaoAlteracao.append("Alterado tipo: '").append(local.getTipo()).append("' => '")
						.append(remote.getTipo()).append("'\n");
				local.setTipo(remote.getTipo());
			}

			if ((local.getSituacao() == null && remote.getSituacao() != null)
					|| !local.getSituacao().equals(remote.getSituacao())) {
				descricaoAlteracao.append("Alterado situação: '").append(local.getSituacao()).append("' => '")
						.append(remote.getSituacao()).append("' \n");
				local.setSituacao(remote.getSituacao());
			}

			return descricaoAlteracao.length();
		}

		public String getDescricaoAlteracao() {
			return descricaoAlteracao.toString();
		}
	};

	private ChecaAlteracoesProposicao checadorAlteracoes = new ChecaAlteracoesProposicao();

	@Override
	public boolean syncDadosProposicao(Proposicao proposicaoLocal) throws IOException {
		try {
			ProposicaoSearcher parser = ProposicaoSearcherFactory.getInstance(proposicaoLocal);
			Proposicao proposicaoRemota = parser.getProposicao(proposicaoLocal.getIdProposicao().longValue());

			if (checadorAlteracoes.compare(proposicaoLocal, proposicaoRemota) != 0) {
				AlteracaoProposicao altera = new AlteracaoProposicao(proposicaoLocal,
						checadorAlteracoes.getDescricaoAlteracao(), new Date());
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
						"Houve alteração da proposicao " + proposicaoLocal.getSigla() + ". A alteração foi "
								+ altera.getDescricaoAlteracao());
				proposicaoLocal.addAlteracao(altera);
				save(proposicaoLocal);
				return true;

			}
		} catch (Exception e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE,
					"Falhou ao sincronizar proposicao " + proposicaoLocal, e);
		}
		return false;
	}

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

	private void popularTotalComentariosEncaminhamentos(Proposicao proposicao) {
		if (proposicao != null) {
			proposicao.setTotalComentarios(comentarioService.totalByProposicao(proposicao.getId()));
			proposicao.setTotalEncaminhamentos(encaminhamentoProposicaoService.totalByProposicao(proposicao.getId()));

			// TODO Remover o carregamento da lista quando tiver a nova
			// implementacao do carregamento por demanda
			Set<EncaminhamentoProposicao> encaminhamentos = new HashSet<>(
					encaminhamentoProposicaoService.findByProposicao(proposicao.getId()));
			proposicao.setListaEncaminhamentoProposicao(encaminhamentos);
		}
	}

	private void popularTotalComentariosEncaminhamentos(List<Proposicao> proposicoes) {
		for (Proposicao proposicao : proposicoes) {
			popularTotalComentariosEncaminhamentos(proposicao);
		}
	}

	@Override
	public PautaReuniaoComissao savePautaReuniaoComissao(PautaReuniaoComissao pautaReuniaoComissao) throws Exception {
		// check se proposicoes existem
		SortedSet<ProposicaoPautaComissao> sortedSet = pautaReuniaoComissao.getProposicoes();
		getEntityManager().persist(pautaReuniaoComissao);
		for (Iterator<ProposicaoPautaComissao> iterator = sortedSet.iterator(); iterator.hasNext();) {
			ProposicaoPautaComissao proposicaoPautaComissao = (ProposicaoPautaComissao) iterator.next();
			Proposicao proposicao = proposicaoPautaComissao.getProposicao();
			if (proposicaoPautaComissao.getProposicao().getId() == null) {
				Proposicao proposicaoDb = findProposicaoBy(proposicao.getOrigem(), proposicao.getIdProposicao());
				if (proposicaoDb == null) {
					// TEM Que buscar dos WS
					switch (proposicao.getOrigem()) {
					case CAMARA:
						proposicaoDb = detalharProposicaoCamaraWS((long) proposicao.getIdProposicao());
						if (proposicaoDb == null) {
							throw new IllegalArgumentException("Não foi possível encontrar proposicao da "
									+ proposicao.getIdProposicao() + " " + proposicao.getOrigem() + " "
									+ proposicao.getComissao());
						}
						break;
					case SENADO:
						proposicaoDb = detalharProposicaoSenadoWS((long) proposicao.getIdProposicao());
						break;
					}
					save(proposicaoDb);
					proposicaoPautaComissao.setProposicao(proposicaoDb);
				} else {
					proposicaoPautaComissao.setProposicao(proposicaoDb);
				}
			}
			proposicaoPautaComissao.setPautaReuniaoComissao(pautaReuniaoComissao);

			System.out.println(proposicaoPautaComissao.getPautaReuniaoComissao() + " "
					+ proposicaoPautaComissao.getProposicao());
			getEntityManager().persist(proposicaoPautaComissao);
		}

		return pautaReuniaoComissao;

	}

	private Proposicao findProposicaoBy(Origem origem, Integer idProposicao) {
		Query q = em.createNamedQuery("findByUniques", Proposicao.class);
		q.setParameter("origem", origem);
		q.setParameter("idProposicao", idProposicao);

		List<Proposicao> props = q.getResultList();
		if (props.isEmpty()) {
			return null;
		} else {
			if (props.size() > 0) {
				throw new IllegalArgumentException("Mais de uma proposicao com id=" + idProposicao + " e origem "
						+ origem.name());
			}
			return props.get(0);

		}
	}

	@Override
	public PautaReuniaoComissao findPautaReuniao(String comissao, Date date, Integer codigoReuniao) {

		return null;

	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		this.parserProposicaoCamara = (ParserProposicaoCamara) injections[1];

	}

}
