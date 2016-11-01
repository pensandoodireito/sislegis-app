package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AreaDeMerito;
import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.NotificacaoService;
import br.gov.mj.sislegis.app.service.PosicionamentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.TagService;
import br.gov.mj.sislegis.app.service.TarefaService;
import br.gov.mj.sislegis.app.service.TipoEncaminhamentoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.service.ejbs.AreaDeMeritoServiceEJB;
import br.gov.mj.sislegis.app.service.ejbs.ComentarioServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ComissaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.EJBDataCacherImpl;
import br.gov.mj.sislegis.app.service.ejbs.EJBUnitTestable;
import br.gov.mj.sislegis.app.service.ejbs.EncaminhamentoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.NotificacaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.PosicionamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TagServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TarefaServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.TipoEncaminhamentoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.UsuarioServiceEjb;

public class TestAreaMetrito {
	private static final String EMAIL_USUARIO_PADRAO = "rafael.coutinho@gmail.com";
	PosicionamentoService posicionamentoSvc;
	ProposicaoService proposicaoService;
	ComissaoService comissaoService;
	TipoEncaminhamentoService tipoEncaminhamentoService;
	EncaminhamentoProposicaoService encaminhamentoService;
	TarefaService tarefaService;
	NotificacaoService notiService;
	ComentarioService comentarioService;
	private UsuarioService userSvc;
	EntityManager entityManager;
	TagService tagService;
	private static EntityManagerFactory emf = null;
	EntityManager em;
	private ReuniaoServiceEjb reuniaoEJB;
	private ReuniaoProposicaoServiceEjb reuniaoProposicaoEJB;
	private AreaDeMeritoServiceEJB amSvc;
	private TipoEncaminhamento tipoEnc;

	public static void closeEntityManager() {
		emf.close();
	}

	@After
	public void tearDown() {
		deInitEJBS();
		closeEntityManager();
	}

	@Before
	public void setUp() {
		try {
			emf = Persistence.createEntityManagerFactory("sislegis-persistence-unit-test");
			initEJBS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deInitEJBS() {
		if (em != null && em.isOpen()) {
			em.close();
		}
	}

	private void initEJBS() {
		em = emf.createEntityManager();
		proposicaoService = new ProposicaoServiceEjb();
		userSvc = new UsuarioServiceEjb();
		posicionamentoSvc = new PosicionamentoServiceEjb();
		reuniaoEJB = new ReuniaoServiceEjb();
		reuniaoEJB.setInjectedEntities(em);
		tagService = new TagServiceEjb();
		encaminhamentoService = new EncaminhamentoProposicaoServiceEjb();
		tarefaService = new TarefaServiceEjb();
		notiService = new NotificacaoServiceEjb();
		amSvc = new AreaDeMeritoServiceEJB();

		reuniaoProposicaoEJB = new ReuniaoProposicaoServiceEjb();
		reuniaoProposicaoEJB.setInjectedEntities(em);
		comissaoService = new ComissaoServiceEjb();
		EJBDataCacherImpl ejbCacher = new EJBDataCacherImpl();
		ejbCacher.initialize();
		((EJBUnitTestable) comissaoService).setInjectedEntities(em, ejbCacher);
		((EJBUnitTestable) notiService).setInjectedEntities(em);
		comentarioService = new ComentarioServiceEjb();
		((EJBUnitTestable) comentarioService).setInjectedEntities(em);
		((EJBUnitTestable) tarefaService).setInjectedEntities(em, notiService);
		((EJBUnitTestable) encaminhamentoService).setInjectedEntities(em, tarefaService);
		((EJBUnitTestable) proposicaoService).setInjectedEntities(em, new ParserProposicaoCamara(), reuniaoEJB, reuniaoProposicaoEJB, comissaoService, comentarioService, encaminhamentoService, amSvc);
		((EJBUnitTestable) userSvc).setInjectedEntities(em);
		((EJBUnitTestable) posicionamentoSvc).setInjectedEntities(em);
		((EJBUnitTestable) tagService).setInjectedEntities(em);
		tipoEncaminhamentoService = new TipoEncaminhamentoServiceEjb();
		((EJBUnitTestable) tipoEncaminhamentoService).setInjectedEntities(em);
		((EJBUnitTestable) amSvc).setInjectedEntities(em);

	}

	@Test
	public void testQuery() {
		List<Proposicao> futuro = em.createNamedQuery("findPautadas", Proposicao.class).setParameter("data", new Date()).getResultList();
		// List<Proposicao> passado =
		// em.createNamedQuery("findNaoPautadas",Proposicao.class).getResultList();
		List<Proposicao> passado = em.createQuery("select p from Proposicao p left join p.ultima pp where p.ultima IS NULL").getResultList();

		List<Proposicao> todas = proposicaoService.listAll();// em.createNamedQuery("findPautadas",Proposicao.class).setParameter("data",new
																// Date(0)).getResultList();
		System.out.println(futuro.size() + " " + passado.size());
		System.out.println(todas.size());
		for (Iterator iterator = todas.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			System.out.println(proposicao.getSigla() + " " + proposicao.getUltima());

		}

	}
	@Test
	public void test2() throws IOException {
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("estado", EstadoProposicao.FORADEPAUTA.name());
		filtros.put("origem", Origem.SENADO.name());
		
		List<Proposicao> props = proposicaoService.consultar(filtros, 0, 2);
		Proposicao p =  props.get(0);
	}

	@Test
	public void testPautadas() throws IOException {
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("origem", Origem.CAMARA.name());
		filtros.put("somentePautadas",true);
//		filtros.put("origem", Origem.SENADO.name());
//
//		ParserProposicaoSenado pps = new ParserProposicaoSenado();
//
		
//		for (Iterator iterator = props.iterator(); iterator.hasNext();) {
//			Proposicao proposicao = (Proposicao) iterator.next();
//			Proposicao p = pps.getProposicao(proposicao.getIdProposicao().longValue());
//			System.out.println("http://legis.senado.leg.br/dadosabertos/materia/movimentacoes/"+p.getIdProposicao()+"?v=4");
//			System.out.println("\t" + p.getTramitacao());
//		}
//		
		filtros.put("origem", Origem.CAMARA.name());
		filtros.put("somentePautadas",true);
		List<Proposicao> props = proposicaoService.consultar(filtros, 0, 100);
		ParserProposicaoCamara ppc = new ParserProposicaoCamara();

		for (Iterator iterator = props.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			Proposicao p = ppc.getProposicao(proposicao.getIdProposicao().longValue());
			System.out.println("http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ObterProposicaoPorID?idProp="+p.getIdProposicao());
			System.out.println("\t" + p.getTramitacao());
		}
	}

	@Test
	public void testUltima() {
		Proposicao pp = proposicaoService.findById(840l);
		System.out.println(pp.getPautasComissoes().size());

		// System.out.println(" "+pp.getPautasComissoes().get(0).equals(pp.getPautasComissoes().get(1)));

		System.out.println(pp.getUltima().getPautaReuniaoComissao().getData());
		System.out.println(pp.getPautaComissaoAtual().getPautaReuniaoComissao().getData());

		List<Proposicao> props = proposicaoService.listAll();
		for (Iterator iterator = props.iterator(); iterator.hasNext();) {
			Proposicao p = (Proposicao) iterator.next();
			if (p.getUltima() != null) {
				System.out.println(p.getUltima().getPautaReuniaoComissao().getData());
				System.out.println(p.getPautaComissaoAtual().getPautaReuniaoComissao().getData());
				Assert.assertEquals(p.getId() + " " + p.getSigla(), p.getUltima().getPautaReuniaoComissaoId(), p.getPautaComissaoAtual().getPautaReuniaoComissaoId());

			}

		}

	}

	@Test
	public void testSvcAreaMerito() {
		System.out.println(amSvc.listAll().size());
		AreaDeMerito am = em.find(AreaDeMerito.class, 17836l);
		System.out.println(amSvc.listRevisoes(17836l, false).size());
		System.out.println(amSvc.listRevisoesProposicao(3017l).size());

	}

	@Test
	public void testCriaAreaMerito() {
		EntityTransaction t = em.getTransaction();
		try {
			AreaDeMerito am = new AreaDeMerito();
			Usuario u = userSvc.findByEmail("rafael.coutinho@mj.gov.br");
			am.setContato(u);
			am.setNome("Area1");

			t.begin();
			em.persist(am);
			t.commit();

			am = em.find(AreaDeMerito.class, am.getId());
			System.out.println(am.getContato().getNome());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			t.rollback();
		}

	}

	@Test
	public void testCriaParecerAreaMerito() {
		EntityTransaction t = em.getTransaction();
		try {
			AreaDeMerito am = em.find(AreaDeMerito.class, 17836l);
			AreaDeMeritoRevisao revisao = new AreaDeMeritoRevisao();
			revisao.setAreaMerito(am);
			revisao.setParecer("Asdfasdfsadfasdfasdfa");

			revisao.setProposicao(proposicaoService.findById(3017l));
			System.out.println(revisao.getProposicao());
			// revisao.setPosicionamento(posicionamentoSvc.listAll().get(0));

			t.begin();
			em.persist(revisao);
			t.commit();
			System.out.println(revisao.getId() + " " + revisao.isPendente());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			t.rollback();
		}

	}
}