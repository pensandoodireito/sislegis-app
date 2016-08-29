package br.gov.mj.sislegis.app.parser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ejbs.EJBUnitTestable;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoServiceEjb;

public class ImporterTests {
	ProposicaoService proposicaoService;
	EntityManager entityManager;
	private static EntityManagerFactory emf = null;
	EntityManager em;
	private ReuniaoServiceEjb reuniaoEJB;
	private ReuniaoProposicaoServiceEjb reuniaoProposicaoEJB;

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
			emf = Persistence.createEntityManagerFactory("sislegis-persistence-unit-standalone");
			initEJBS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deInitEJBS() {
		if (em.isOpen()) {
			em.close();
		}
	}

	private void initEJBS() {
		em = emf.createEntityManager();
		proposicaoService = new ProposicaoServiceEjb();

		reuniaoEJB = new ReuniaoServiceEjb();
		reuniaoEJB.setInjectedEntities(em);
		reuniaoProposicaoEJB = new ReuniaoProposicaoServiceEjb();
		reuniaoProposicaoEJB.setInjectedEntities(em);
		((EJBUnitTestable) proposicaoService).setInjectedEntities(em, new ParserProposicaoCamara(), reuniaoEJB,
				reuniaoProposicaoEJB);
	}

	@Test
	public void testDBAccess() {

		System.out.println(proposicaoService.listAll().size());

	}
}
