package br.gov.mj.sislegis.app.parser;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ejbs.ProposicaoServiceEjb;

public class PautaProposicaoReuniaoTests {
	ProposicaoService proposicaoService;
	EntityManager entityManager;
	private static EntityManagerFactory emf = null;
	EntityManager em;

	public static void closeEntityManager() {
		emf.close();
	}

	@After
	public void tearDown() {
		em.close();
		closeEntityManager();
	}

	@Before
	public void setUp() {
		try {
			emf = Persistence.createEntityManagerFactory("test-sislegis-persistence-unit");
			proposicaoService = new ProposicaoServiceEjb();
			em = emf.createEntityManager();
			em.setFlushMode(FlushModeType.AUTO);
			((ProposicaoServiceEjb) proposicaoService).setEntityManager(em);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testPautaCamara() {
		try {
			ParserPautaCamara parser = new ParserPautaCamara();
			Long idComissao = 2003L;
			String datIni = "20151012";
			String datFim = "20151015";
			System.out.println("BUSCANDO" + datFim);
			Set<PautaReuniaoComissao> pautas = parser.getPautaComissao(idComissao, datIni, datFim);
			for (Iterator<PautaReuniaoComissao> iterator = pautas.iterator(); iterator.hasNext();) {

				PautaReuniaoComissao pautaReuniaoComissao = (PautaReuniaoComissao) iterator.next();
				System.out.println("trantaod " + pautaReuniaoComissao);

				pautaReuniaoComissao = proposicaoService.savePautaReuniaoComissao(pautaReuniaoComissao);

				for (Iterator<ProposicaoPautaComissao> iterator2 = pautaReuniaoComissao.getProposicoes().iterator(); iterator2
						.hasNext();) {
					ProposicaoPautaComissao ppc = (ProposicaoPautaComissao) iterator2.next();
					Proposicao p = ppc.getProposicao();

					proposicaoService.save(ppc.getProposicao());
					System.out.println(ppc.getProposicao().getId());
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

	}

}
