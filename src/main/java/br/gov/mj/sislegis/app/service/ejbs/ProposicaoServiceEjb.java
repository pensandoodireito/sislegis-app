package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.json.EncaminhamentoProposicaoJSON;
import br.gov.mj.sislegis.app.model.*;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.service.*;
import br.gov.mj.sislegis.app.util.Conversores;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ProposicaoServiceEjb extends AbstractPersistence<Proposicao, Long> implements ProposicaoService {

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
		// TODO Auto-generated method stub
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

	@Override
	public void salvarListaProposicao(List<Proposicao> listaProposicao) {
		Reuniao reuniao = null;

		if (!listaProposicao.isEmpty()) {
			Proposicao proposicao = listaProposicao.get(0); // uma forma de
															// obter a data da
															// reuniao é através
															// do objeto
															// proposicao
			reuniao = reuniaoService.buscaReuniaoPorData(proposicao.getReuniao().getData());

			// Caso a reunião não exista, salva pela primeira vez
			if (Objects.isNull(reuniao)) {
				reuniao = new Reuniao();
				reuniao.setData(proposicao.getReuniao().getData());
				reuniao = reuniaoService.save(reuniao);
			}
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

	private ReuniaoProposicao getReuniaoProposicao(Reuniao reuniao, Proposicao proposicaoFromBusca,	Proposicao proposicao) {
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

//		List<Proposicao> lista = listAll();
//
//		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
//		for (Proposicao proposicao : lista) {
//			ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
//			listaProposicaoJSON.add(proposicaoJSON);
//		}
//
//		return listaProposicaoJSON;
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

//		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
//		for (Proposicao proposicao : lista) {
//			ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
//			listaProposicaoJSON.add(proposicaoJSON);
//		}
//
//		return listaProposicaoJSON;
	}

//	private ProposicaoJSON populaProposicaoJSON(Proposicao proposicao) {
//		ProposicaoJSON proposicaoJSON = new ProposicaoJSON(proposicao.getId(), proposicao.getIdProposicao(),
//				proposicao.getTipo(), proposicao.getAno(), proposicao.getNumero(), proposicao.getAutor(),
//				proposicao.getEmenta(), proposicao.getOrigem(), proposicao.getSigla(), proposicao.getComissao(),
//				proposicao.getSeqOrdemPauta(), proposicao.getLinkProposicao(), proposicao.getLinkPauta(),
//				proposicao.getResultadoASPAR(), proposicao.isFavorita(), proposicao.getReuniao() == null ? null
//						: proposicao.getReuniao().getId(), comentarioService.findByProposicao(proposicao.getId()),
//				encaminhamentoProposicaoService.findByProposicao(proposicao.getId()), proposicao.getPosicionamento(),
//				tagService.populaListaTagsProposicaoJSON(proposicao.getTags()), proposicao.getResponsavel(),
//				populaProposicoesFilhasJSON(proposicao.getProposicoesFilha()), proposicao.getElaboracoesNormativas());
//
//		return proposicaoJSON;
//	}
//
//	private Set<ProposicaoJSON> populaProposicoesFilhasJSON(Set<Proposicao> proposicaoList) {
//		Set<ProposicaoJSON> proposicaoJsonList = new HashSet<ProposicaoJSON>();
//
//		for (Proposicao proposicao : proposicaoList) {
//			ProposicaoJSON proposicaoJSON = new ProposicaoJSON();
//			proposicaoJSON.setId(proposicao.getId());
//			proposicaoJSON.setIdProposicao(proposicao.getIdProposicao());
//			proposicaoJSON.setSigla(proposicao.getSigla());
//
//			proposicaoJsonList.add(proposicaoJSON);
//		}
//
//		return proposicaoJsonList;
//	}

	@Override
	public Proposicao buscarPorId(Integer id) {
		Proposicao proposicao = findById(id.longValue());
		if (proposicao != null) {
			popularTotalComentariosEncaminhamentos(proposicao);
		}
		return proposicao;

//		Proposicao proposicao = findById(id);
//		ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
//		populaComentarioProposicao(proposicao, proposicaoJSON);
//		return proposicaoJSON;
	}

	@Override
	public List<Proposicao> buscarProposicoesPorDataReuniao(Date dataReuniao) {
		List<Proposicao> proposicoes = new ArrayList<>();
		Reuniao reuniao = reuniaoService.buscaReuniaoPorData(dataReuniao);

		if (!Objects.isNull(reuniao)) {
			Set<ReuniaoProposicao> listaReuniaoProposicoes = reuniao.getListaReuniaoProposicoes();
			// Copiamos alguns valores de ReuniaoProposicao para Proposicao,
			// afim de retornar somente uma entidade com alguns valores
			// transientes
			for (ReuniaoProposicao reuniaoProposicao : listaReuniaoProposicoes) {
				Proposicao proposicao = reuniaoProposicao.getProposicao();
				proposicao.setComissao(reuniaoProposicao.getSiglaComissao());
				proposicao.setSeqOrdemPauta(reuniaoProposicao.getSeqOrdemPauta());
				proposicao.setLinkPauta(reuniaoProposicao.getLinkPauta());
				proposicao.setReuniao(reuniaoProposicao.getReuniao());

				popularTotalComentariosEncaminhamentos(proposicao);

				proposicoes.add(proposicao);
			}
		}

		return proposicoes;
	}


//		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
//		Reuniao reuniao = reuniaoService.buscaReuniaoPorData(dataReuniao);
//
//		if (!Objects.isNull(reuniao)) {
//			Set<ReuniaoProposicao> listaReuniaoProposicoes = reuniao.getListaReuniaoProposicoes();
//			// Copiamos alguns valores de ReuniaoProposicao para Proposicao,
//			// afim de retornar somente uma entidade com alguns valores
//			// transientes
//			for (ReuniaoProposicao reuniaoProposicao : listaReuniaoProposicoes) {
//				Proposicao proposicao = reuniaoProposicao.getProposicao();
//				proposicao.setComissao(reuniaoProposicao.getSiglaComissao());
//				proposicao.setSeqOrdemPauta(reuniaoProposicao.getSeqOrdemPauta());
//				proposicao.setLinkPauta(reuniaoProposicao.getLinkPauta());
//				proposicao.setReuniao(reuniaoProposicao.getReuniao());
//
//				ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
//				populaComentarioProposicao(proposicao, proposicaoJSON);
//				listaProposicaoJSON.add(proposicaoJSON);
//			}
//		}
//
//		return listaProposicaoJSON;


//	private void populaComentarioProposicao(Proposicao proposicao, ProposicaoJSON proposicaoJSON) {
//		proposicaoJSON.setListaComentario(comentarioService.findByProposicao(proposicao.getId()));
//	}

	@Override
	public void atualizarProposicaoJSON(Proposicao proposicao) {

		save(proposicao);
		// TODO Verificar se ainda ha necessidade de se remover as tags, ja que o mapeamento esta como cascade

//		processaExclusaoTagProposicao(proposicaoJSON);
//		Proposicao proposicao = proposicaoJsonToProposicao(proposicaoJSON);
//
//		save(proposicao);
	}

	@Override
	public Proposicao save(Proposicao entity) {
		if (entity.getResponsavel() != null && entity.getResponsavel().getId() == null) {

			// criar usuario antes
			Usuario usuarioExistente = usuarioService.findByEmail(entity.getResponsavel().getEmail());
			if (usuarioExistente == null){
				usuarioService.save(entity.getResponsavel());
			} else{
				entity.setResponsavel(usuarioExistente);
			}

		}
		return super.save(entity);
	}

//	private void processaExclusaoTagProposicao(Proposicao proposicao) {
//		if (!Objects.isNull(proposicaoJSON.getId())) {
//			Query query = em.createNativeQuery(
//					"SELECT tp.* FROM TagProposicao tp WHERE tp.proposicao_id = :idProposicao", TagProposicao.class);
//			query.setParameter("idProposicao", proposicaoJSON.getId());
//			List<TagProposicao> listaTagsProposicao = query.getResultList();
//
//			c: for (TagProposicao tagProposicao : listaTagsProposicao) {
//				for (TagJSON tagJSON : proposicaoJSON.getTags()) {
//					if (tagJSON.getText().equals(tagProposicao.getTag().getTag()))
//						continue c;
//				}
//				em.createNativeQuery(
//						"delete FROM TagProposicao tp WHERE " + "tp.proposicao_id = :idProposicao "
//								+ "and tp.tag_id = :tag", TagProposicao.class)
//						.setParameter("idProposicao", tagProposicao.getProposicao().getId())
//						.setParameter("tag", tagProposicao.getTag()).executeUpdate();
//			}
//		}
//	}

//	private Proposicao proposicaoJsonToProposicao(ProposicaoJSON proposicaoJSON) {
//		Proposicao proposicao = findById(proposicaoJSON.getId());
//		proposicao.setAno(proposicaoJSON.getAno());
//		proposicao.setIdProposicao(proposicaoJSON.getIdProposicao());
//		proposicao.setNumero(proposicaoJSON.getNumero());
//		proposicao.setSigla(proposicaoJSON.getSigla());
//		proposicao.setAutor(proposicaoJSON.getAutor());
//		proposicao.setOrigem(proposicaoJSON.getOrigem());
//		proposicao.setComissao(proposicaoJSON.getComissao());
//		proposicao.setSeqOrdemPauta(proposicaoJSON.getSeqOrdemPauta());
//		proposicao.setPosicionamento(proposicaoJSON.getPosicionamento());
//		proposicao.setResponsavel(proposicaoJSON.getResponsavel());
//		proposicao.setResultadoASPAR(proposicaoJSON.getResultadoASPAR());
//		proposicao.setFavorita(proposicaoJSON.isFavorita());
//		proposicao.setProposicoesFilha(populaProposicoesFilha(proposicaoJSON, proposicao));
//		proposicao.setElaboracoesNormativas(proposicaoJSON.getElaboracoesNormativas());
//		Set<TagProposicao> tags = populaTagsProposicao(proposicaoJSON, proposicao);
//		proposicao.setTags(tags);
//		return proposicao;
//	}

//	private Set<TagProposicao> populaTagsProposicao(ProposicaoJSON proposicaoJSON, Proposicao proposicao) {
//		Set<TagProposicao> tagsProposicao = new HashSet<TagProposicao>();
//		for (TagJSON tagJSON : proposicaoJSON.getTags()) {
//			TagProposicaoPK tagProposicaoPK = new TagProposicaoPK();
//			TagProposicao tagProposicao = new TagProposicao();
//			Tag tag = new Tag();
//			tagProposicaoPK.setIdProposicao(proposicaoJSON.getId());
//			tagProposicaoPK.setTag(tagJSON.getText());
//			tagProposicao.setTagProposicaoPK(tagProposicaoPK);
//			tagProposicao.setProposicao(proposicao);
//			tag.setTag(tagJSON.getText());
//			tagProposicao.setTag(tag);
//			tagsProposicao.add(tagProposicao);
//		}
//		return tagsProposicao;
//	}

//	private Set<Proposicao> populaProposicoesFilha(ProposicaoJSON proposicaoJSON, Proposicao proposicao) {
//		Set<Proposicao> proposicoesFilhas = new HashSet<Proposicao>();
//
//		for (ProposicaoJSON proposicaoFilha : proposicaoJSON.getProposicoesFilha()) {
//			Proposicao proposicaoFilhaTemp = new Proposicao();
//			proposicaoFilhaTemp.setId(proposicaoFilha.getId());
//			proposicaoFilhaTemp.setIdProposicao(proposicaoFilha.getIdProposicao());
//
//			if (proposicaoFilhaTemp.getProposicoesPai() == null) {
//				proposicaoFilhaTemp.setProposicoesPai(new HashSet<Proposicao>());
//			}
//			proposicaoFilhaTemp.getProposicoesPai().add(proposicao);
//
//			proposicoesFilhas.add(proposicaoFilhaTemp);
//		}
//
//		return proposicoesFilhas;
//	}

	@Override
	public void deleteById(Long id) {
		List<EncaminhamentoProposicaoJSON> listaEnc = encaminhamentoProposicaoService.findByProposicao(id);
		for (Iterator<EncaminhamentoProposicaoJSON> iterator = listaEnc.iterator(); iterator.hasNext();) {
			EncaminhamentoProposicaoJSON ep = iterator.next();
			encaminhamentoProposicaoService.deleteById(ep.getId());
		}

		List<ComentarioJSON> listaCom = comentarioService.findByProposicao(id);
		for (Iterator<ComentarioJSON> iterator = listaCom.iterator(); iterator.hasNext();) {
			ComentarioJSON c = iterator.next();
			comentarioService.deleteById(c.getId());
		}

		super.deleteById(id);
	}

	@Override
	public List<Proposicao> buscarPorSufixo(String sufixo) {
		TypedQuery<Proposicao> query = getEntityManager().createQuery(
				"SELECT p FROM Proposicao p WHERE upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)",	Proposicao.class);
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

	private void popularTotalComentariosEncaminhamentos(Proposicao proposicao) {
		if (proposicao != null) {
			proposicao.setTotalComentarios(comentarioService.totalByProposicao(proposicao.getId()));
			proposicao.setTotalEncaminhamentos(encaminhamentoProposicaoService.totalByProposicao(proposicao.getId()));
		}
	}

	private void popularTotalComentariosEncaminhamentos(List<Proposicao> proposicoes){
		for (Proposicao proposicao : proposicoes){
			popularTotalComentariosEncaminhamentos(proposicao);
		}
	}
}
