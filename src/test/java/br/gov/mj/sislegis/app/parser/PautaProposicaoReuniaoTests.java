package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ejbs.EJBUnitTestable;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoProposicaoServiceEjb;
import br.gov.mj.sislegis.app.service.ejbs.ReuniaoServiceEjb;

public class PautaProposicaoReuniaoTests {
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
			emf = Persistence.createEntityManagerFactory("test-sislegis-persistence-unit");
			initEJBS();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	public void testPautaCamara() {
		try {
			ParserPautaCamara parser = new ParserPautaCamara();
			Long idComissao = 2002L;
			String datIni = "20151014";
			String datFim = "20151015";
			Set<PautaReuniaoComissao> pautas = parser.getPautaComissao("",idComissao, datIni, datFim);
			Reuniao reuniao = new Reuniao();
			reuniao.setData(new Date());
			proposicaoService.adicionaProposicoesReuniao(pautas, reuniao);

			Collection<Proposicao> proposicoes = proposicaoService.buscarProposicoesPorDataReuniao(reuniao.getData());

			PautaReuniaoComissao prc = proposicaoService.retrievePautaReuniao(pautas.iterator().next()
					.getCodigoReuniao());
			System.out.println(prc.getId());
			System.out.println("Aa " + prc.getProposicoesDaPauta().size());
			System.out.println(proposicoes.size());
			reuniao = reuniaoEJB.findById(reuniao.getId());
			// deInitEJBS();
			for (Iterator iterator = proposicoes.iterator(); iterator.hasNext();) {
				Proposicao proposicao = (Proposicao) iterator.next();
				System.out.println(proposicao.getPautasComissoes().size());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

	}
}
