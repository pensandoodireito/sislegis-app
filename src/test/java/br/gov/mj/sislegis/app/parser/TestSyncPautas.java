package br.gov.mj.sislegis.app.parser;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
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
import br.gov.mj.sislegis.app.util.Conversores;

public class TestSyncPautas {

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
		if (emf != null)
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
		((EJBUnitTestable) proposicaoService).setInjectedEntities(em, new ParserProposicaoCamara(), reuniaoEJB,
				reuniaoProposicaoEJB, comissaoService, comentarioService, encaminhamentoService, amSvc);
		((EJBUnitTestable) userSvc).setInjectedEntities(em);
		((EJBUnitTestable) posicionamentoSvc).setInjectedEntities(em);
		((EJBUnitTestable) tagService).setInjectedEntities(em);
		tipoEncaminhamentoService = new TipoEncaminhamentoServiceEjb();
		((EJBUnitTestable) tipoEncaminhamentoService).setInjectedEntities(em);
		((EJBUnitTestable) amSvc).setInjectedEntities(em);

	}

	@Test
	public void testBusca() throws Exception {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.add(Calendar.WEEK_OF_YEAR, 1);
		List<Comissao> ls = comissaoService.listarComissoesCamara();
//		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
//			Comissao comissao = (Comissao) iterator.next();
//			System.out.println("Comissao " + comissao.getSigla());
//			try {
//				ParserPautaCamara parserPautaCamara = new ParserPautaCamara();
//
//				Set<PautaReuniaoComissao> pss = parserPautaCamara.getPautaComissao(comissao.getSigla(),
//						comissao.getId(), Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"),
//						Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
//				// proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
//				// dataInicial.getTime(), dataFinal.getTime());
//				for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
//					PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
//					System.out.println(object + " " + object.getData());
//					SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
//					for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3
//							.hasNext();) {
//						ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
//						Proposicao p = proposicaoService.findProposicaoBy(Origem.CAMARA, propPauta.getProposicao()
//								.getIdProposicao());
//						System.out.println("\t" + propPauta + " === " + p);
//						if (p != null) {
//							paraSalvar.add(propPauta);
//						}
//
//					}
//					if (!paraSalvar.isEmpty()) {
//						EntityTransaction trans = em.getTransaction();
//						trans.begin();
//						object.setProposicoesDaPauta(paraSalvar);
//						proposicaoService.savePautaReuniaoComissao(object);
//						trans.commit();
//					}
//
//				}
//			} catch (Exception e) {
//				System.err.println("Falhou ao buscar para a comissao " + comissao.getSigla() + " " + e.getMessage());
//			}
//		}
		ls = comissaoService.listarComissoesSenado();
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			Comissao comissao = (Comissao) iterator.next();
			System.out.println("Comissao " + comissao.getSigla());
			try {
				ParserPautaSenado parserSenado = new ParserPautaSenado();
				ParserPlenarioSenado parserPlenarioSenado = new ParserPlenarioSenado();
				Set<PautaReuniaoComissao> pss = null;
				if (comissao.getSigla().equals("PLEN")) {
					pss = parserPlenarioSenado.getProposicoes(Conversores.dateToString(dataInicial.getTime(),
							"yyyyMMdd"));
				} else {
					pss = parserSenado.getPautaComissao(comissao.getSigla(),
							Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"),
							Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
				}
				// proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
				// dataInicial.getTime(), dataFinal.getTime());
				for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
					PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
					System.out.println(object + " " + object.getData());
					SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
					for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3
							.hasNext();) {
						ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
						Proposicao p = proposicaoService.findProposicaoBy(Origem.SENADO, propPauta.getProposicao()
								.getIdProposicao());
						System.out.println("\t" + propPauta + " === " + p);
						if (p != null) {
							paraSalvar.add(propPauta);
						}

					}
					if (!paraSalvar.isEmpty()) {
//						EntityTransaction trans = em.getTransaction();
//						trans.begin();
						object.setProposicoesDaPauta(paraSalvar);
						proposicaoService.savePautaReuniaoComissao(object);
//						trans.commit();
					}

				}
			} catch (Exception e) {
				System.err.println("Falhou ao buscar para a comissao " + comissao.getSigla() + " " + e.getMessage());
			}
		}

	}
}