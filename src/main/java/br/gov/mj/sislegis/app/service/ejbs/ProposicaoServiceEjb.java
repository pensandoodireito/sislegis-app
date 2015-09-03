package br.gov.mj.sislegis.app.service.ejbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.json.EncaminhamentoProposicaoJSON;
import br.gov.mj.sislegis.app.json.ProposicaoJSON;
import br.gov.mj.sislegis.app.json.TagJSON;
import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicao;
import br.gov.mj.sislegis.app.model.ReuniaoProposicaoPK;
import br.gov.mj.sislegis.app.model.Tag;
import br.gov.mj.sislegis.app.model.TagProposicao;
import br.gov.mj.sislegis.app.model.TagProposicaoPK;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.PosicionamentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoService;
import br.gov.mj.sislegis.app.service.TagService;
import br.gov.mj.sislegis.app.util.Conversores;
import br.gov.mj.sislegis.app.util.SislegisUtil;

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
	private PosicionamentoService posicionamentoService;
	

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
	public void salvarListaProposicao(List<Proposicao> listaProposicao) {
		Reuniao reuniao = null;

		if (! listaProposicao.isEmpty()) {
			Proposicao proposicao = listaProposicao.get(0); // uma forma de obter a data da reuniao é através do objeto proposicao
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
				Proposicao proposicao = buscarPorIdProposicao(proposicaoFromBusca.getIdProposicao());
				
				// Caso a proposição não exista, salvamos ela e associamos a reunião
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
					ReuniaoProposicao reuniaoProposicao = reuniaoProposicaoService.findById(reuniao.getId(), proposicao.getId());
					
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

	private ReuniaoProposicao getReuniaoProposicao(Reuniao reuniao,
			Proposicao proposicaoFromBusca, Proposicao proposicao) {
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
	public List<ProposicaoJSON> listarTodos() {
		List<Proposicao> lista = listAll();

		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
		for (Proposicao proposicao : lista) {
			ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
			listaProposicaoJSON.add(proposicaoJSON);
		}

		return listaProposicaoJSON;
	}

	@Override
	public List<ProposicaoJSON> consultar(String posicionamento, String sigla, String autor, String ementa, String origem, String isFavorita, Integer offset, Integer limit) {
		StringBuilder query = new StringBuilder("SELECT p FROM Proposicao p WHERE 1=1");
		Posicionamento posicionamentoObj = null;

		if(Objects.nonNull(posicionamento) && !posicionamento.equals("")){
			if("-1".equals(posicionamento)){
				query.append(" AND p.posicionamento is null");
			}else{
				try {
					posicionamentoObj= posicionamentoService.findById(Long.parseLong(posicionamento));
					
					query.append(" AND p.posicionamento=:posicionamento");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(Objects.nonNull(sigla) && !sigla.equals("")){
			query.append(" AND upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)");
		}
		if(Objects.nonNull(ementa) && !ementa.equals("")){
			query.append(" AND upper(p.ementa) like upper(:ementa)");
		}
		if(Objects.nonNull(autor) && !autor.equals("")){
			query.append(" AND upper(p.autor) like upper(:autor)");
		}
		if(Objects.nonNull(origem) && !origem.equals("")){
			query.append(" AND p.origem = :origem");
		}
		if(Objects.nonNull(isFavorita) && !isFavorita.equals("")){
			query.append(" AND p.isFavorita = :isFavorita");
		}
		
		TypedQuery<Proposicao> findByIdQuery = getEntityManager().createQuery(query.toString(),	Proposicao.class);
		if(Objects.nonNull(posicionamento) && !posicionamento.equals("")){
			if(!"-1".equals(posicionamento)){
				findByIdQuery.setParameter("posicionamento",posicionamentoObj);
			}
		}
		if(Objects.nonNull(sigla) && !sigla.equals("")){
			findByIdQuery.setParameter("sigla", "%"+sigla+"%");
		}
		if(Objects.nonNull(ementa) && !ementa.equals("")){
			findByIdQuery.setParameter("ementa", "%"+ementa+"%");
		}
		if(Objects.nonNull(autor) && !autor.equals("")){
			findByIdQuery.setParameter("autor", "%"+autor+"%");
		}
		if(Objects.nonNull(origem) && !origem.equals("")){
			findByIdQuery.setParameter("origem", Origem.valueOf(origem));
		}
		if(Objects.nonNull(isFavorita) && !isFavorita.equals("")){
			findByIdQuery.setParameter("isFavorita", new Boolean(isFavorita));
		}
		List<Proposicao> lista = findByIdQuery
		         .setFirstResult(offset) // offset
		         .setMaxResults(limit) // limit
		         .getResultList();

		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
		for (Proposicao proposicao : lista) {
			ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
			listaProposicaoJSON.add(proposicaoJSON);
		}

		return listaProposicaoJSON;
	}

	public ProposicaoJSON populaProposicaoJSON(Proposicao proposicao) {	
		ProposicaoJSON proposicaoJSON = new ProposicaoJSON(proposicao.getId(), 
				proposicao.getIdProposicao(), 
				proposicao.getTipo(), 
				proposicao.getAno(),
				proposicao.getNumero(), 
				proposicao.getAutor(), 
				proposicao.getEmenta(), 
				proposicao.getOrigem(), 
				proposicao.getSigla(),
				proposicao.getComissao(), 
				proposicao.getSeqOrdemPauta(), 
				proposicao.getLinkProposicao(), 
				proposicao.getLinkPauta(),
				proposicao.getResultadoASPAR(),
				proposicao.isFavorita(),
				proposicao.getReuniao() == null ? null : proposicao.getReuniao().getId(),
				comentarioService.findByProposicao(proposicao.getId()),
				encaminhamentoProposicaoService.findByProposicao(proposicao.getId()), 
				proposicao.getPosicionamento(), 
				tagService.populaListaTagsProposicaoJSON(proposicao.getTags()),
				proposicao.getResponsavel(),
				populaProposicoesFilhasJSON(proposicao.getProposicoesFilha()),
				proposicao.getElaboracoesNormativas());

		return proposicaoJSON;
	}
	
	public Set<ProposicaoJSON> populaProposicoesFilhasJSON(Set<Proposicao> proposicaoList) {	
		Set<ProposicaoJSON> proposicaoJsonList = new HashSet<ProposicaoJSON>();
		
		for (Proposicao proposicao : proposicaoList) {
			ProposicaoJSON proposicaoJSON = new ProposicaoJSON();
			proposicaoJSON.setId(proposicao.getId());
			proposicaoJSON.setIdProposicao(proposicao.getIdProposicao());
			proposicaoJSON.setSigla(proposicao.getSigla());
			
			proposicaoJsonList.add(proposicaoJSON);
		}
		
		return proposicaoJsonList;
	}

	@Override
	public ProposicaoJSON buscarPorId(Long id) {
		Proposicao proposicao = findById(id);
		ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
		populaComentarioProposicao(proposicao, proposicaoJSON);
		return proposicaoJSON;
	}

	@Override
	public List<ProposicaoJSON> buscarProposicoesPorDataReuniao(Date dataReuniao) {
		List<ProposicaoJSON> listaProposicaoJSON = new ArrayList<ProposicaoJSON>();
		Reuniao reuniao = reuniaoService.buscaReuniaoPorData(dataReuniao);
		
		if (!Objects.isNull(reuniao)) {
			Set<ReuniaoProposicao> listaReuniaoProposicoes = reuniao.getListaReuniaoProposicoes();
			// Copiamos alguns valores de ReuniaoProposicao para Proposicao, afim de retornar somente uma entidade com alguns valores transientes
			for (ReuniaoProposicao reuniaoProposicao : listaReuniaoProposicoes) {
				Proposicao proposicao = reuniaoProposicao.getProposicao();
				proposicao.setComissao(reuniaoProposicao.getSiglaComissao());
				proposicao.setSeqOrdemPauta(reuniaoProposicao.getSeqOrdemPauta());
				proposicao.setLinkPauta(reuniaoProposicao.getLinkPauta());
				proposicao.setReuniao(reuniaoProposicao.getReuniao());
				
				ProposicaoJSON proposicaoJSON = populaProposicaoJSON(proposicao);
				populaComentarioProposicao(proposicao, proposicaoJSON);
				listaProposicaoJSON.add(proposicaoJSON);
			}
		}


		return listaProposicaoJSON;

	}

	private void populaComentarioProposicao(Proposicao proposicao, ProposicaoJSON proposicaoJSON) {
		proposicaoJSON.setListaComentario(comentarioService.findByProposicao(proposicao.getId()));
	}

	@Override
	public void atualizarProposicaoJSON(ProposicaoJSON proposicaoJSON) {
		processaExclusaoTagProposicao(proposicaoJSON);
		Proposicao proposicao = proposicaoJsonToProposicao(proposicaoJSON);
		
		save(proposicao);
	}
	
	private void processaExclusaoTagProposicao(ProposicaoJSON proposicaoJSON) {
		if(!Objects.isNull(proposicaoJSON.getId())){
			Query query = em.createNativeQuery("SELECT tp.* FROM TagProposicao tp WHERE tp.proposicao_id = :idProposicao",
					TagProposicao.class);
			query.setParameter("idProposicao", proposicaoJSON.getId());
			List<TagProposicao> listaTagsProposicao = query.getResultList();

			c:for(TagProposicao tagProposicao:listaTagsProposicao){
				for(TagJSON tagJSON:proposicaoJSON.getTags()){
					if(tagJSON.getText().equals(tagProposicao.getTag().getTag()))
						continue c;
				}
				 em.createNativeQuery("delete FROM TagProposicao tp WHERE "
				 		+ "tp.proposicao_id = :idProposicao "
				 		+ "and tp.tag_id = :tag",
							TagProposicao.class)
							.setParameter("idProposicao", tagProposicao.getProposicao().getId())
							.setParameter("tag", tagProposicao.getTag())
							.executeUpdate();
			}
		}
	}

	private Proposicao proposicaoJsonToProposicao(ProposicaoJSON proposicaoJSON) {
		Proposicao proposicao = findById(proposicaoJSON.getId());
		proposicao.setAno(proposicaoJSON.getAno());
		proposicao.setIdProposicao(proposicaoJSON.getIdProposicao());
		proposicao.setNumero(proposicaoJSON.getNumero());
		proposicao.setSigla(proposicaoJSON.getSigla());
		proposicao.setAutor(proposicaoJSON.getAutor());
		proposicao.setOrigem(proposicaoJSON.getOrigem());
		proposicao.setComissao(proposicaoJSON.getComissao());
		proposicao.setSeqOrdemPauta(proposicaoJSON.getSeqOrdemPauta());
		proposicao.setPosicionamento(proposicaoJSON.getPosicionamento());
		proposicao.setResponsavel(proposicaoJSON.getResponsavel());
		proposicao.setResultadoASPAR(proposicaoJSON.getResultadoASPAR());
		proposicao.setFavorita(proposicaoJSON.isFavorita());
		proposicao.setProposicoesFilha(populaProposicoesFilha(proposicaoJSON, proposicao));
		proposicao.setElaboracoesNormativas(proposicaoJSON.getElaboracoesNormativas());
		Set<TagProposicao> tags = populaTagsProposicao(proposicaoJSON, proposicao);
		proposicao.setTags(tags);
		return proposicao;
	}

	private Set<TagProposicao> populaTagsProposicao(ProposicaoJSON proposicaoJSON, Proposicao proposicao) {
		Set<TagProposicao> tagsProposicao = new HashSet<TagProposicao>();
		for (TagJSON tagJSON : proposicaoJSON.getTags()) {
			TagProposicaoPK tagProposicaoPK = new TagProposicaoPK();
			TagProposicao tagProposicao = new TagProposicao();
			Tag tag = new Tag();
			tagProposicaoPK.setIdProposicao(proposicaoJSON.getId());
			tagProposicaoPK.setTag(tagJSON.getText());
			tagProposicao.setTagProposicaoPK(tagProposicaoPK);
			tagProposicao.setProposicao(proposicao);
			tag.setTag(tagJSON.getText());
			tagProposicao.setTag(tag);
			tagsProposicao.add(tagProposicao);
		}
		return tagsProposicao;
	}
	
	private Set<Proposicao> populaProposicoesFilha(ProposicaoJSON proposicaoJSON, Proposicao proposicao) {
		Set<Proposicao> proposicoesFilhas = new HashSet<Proposicao>();

		for (ProposicaoJSON proposicaoFilha : proposicaoJSON.getProposicoesFilha()) {	
			Proposicao proposicaoFilhaTemp = new Proposicao();
			proposicaoFilhaTemp.setId(proposicaoFilha.getId());
			proposicaoFilhaTemp.setIdProposicao(proposicaoFilha.getIdProposicao());
			
			if (proposicaoFilhaTemp.getProposicoesPai() == null) {
				proposicaoFilhaTemp.setProposicoesPai(new HashSet<Proposicao>()) ;
			}
			proposicaoFilhaTemp.getProposicoesPai().add(proposicao);
			

			proposicoesFilhas.add(proposicaoFilhaTemp);	
		}
		
		return proposicoesFilhas;		
	}

	@Override
	public Proposicao buscarPorIdProposicao(Integer idProposicao) {
		TypedQuery<Proposicao> findByIdQuery = em.createQuery("SELECT p FROM Proposicao p WHERE p.idProposicao = :idProposicao",
				Proposicao.class);
		findByIdQuery.setParameter("idProposicao", idProposicao);
		final List<Proposicao> results = findByIdQuery.getResultList();
		if(!Objects.isNull(results) && !results.isEmpty()){
			return results.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void deleteById(Long id){
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
		TypedQuery<Proposicao> findByIdQuery = getEntityManager().createQuery(
				"SELECT p FROM Proposicao p WHERE upper(CONCAT(p.tipo,' ',p.numero,'/',p.ano)) like upper(:sigla)",
				Proposicao.class);
		findByIdQuery.setParameter("sigla", "%"+sufixo+"%");
		return findByIdQuery.getResultList();
	}
}

