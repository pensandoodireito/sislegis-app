package br.gov.mj.sislegis.app.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.rest.RelatoriosEndpoint;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
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
import br.gov.mj.sislegis.app.service.ejbs.EquipeServiceEjb;
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
	private EquipeServiceEjb equipeSvc;

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
		equipeSvc = new EquipeServiceEjb();
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
		equipeSvc.setInjectedEntities(em, userSvc);

	}

	@Test
	public void testIt() throws ParseException {
		Date inicio = new SimpleDateFormat("ddMMyyyy kk:mm").parse("15102016 08:00");
		Date fim = new SimpleDateFormat("ddMMyyyy kk:mm").parse("15112016 18:00");
		
		JSONArray equipesArr = new JSONArray();
		List<Equipe> equipes = equipeSvc.listAll();

		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("Contrário", "contrario");
		mapa.put("Nada a opor", "nadaaopor");
		mapa.put("Monitorar", "monitorar");
		mapa.put("Favorável com emendas", "favoravelEmendas");
		mapa.put("Favorável", "favoravel");
		for (Iterator iterator = equipes.iterator(); iterator.hasNext();) {
			Equipe equipe = (Equipe) iterator.next();
			if (equipe.getNome().contains("ASPAR")) {
				continue;
			}
			JSONObject equipeJson = equipe.toJson();

			List<Object> list = em.createNamedQuery("contadorPosicionamentosPorEquipe").setParameter("equipe", equipe).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getResultList();

			long totalEquipe = 0;
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				Object[] object = (Object[]) iterator2.next();

				totalEquipe+=(Long)object[0];
				equipeJson.put(mapa.get((String) object[2]), object[0]);

			}
			equipeJson.put("total", totalEquipe);

			equipesArr.put(equipeJson);

		}
		System.out.println(equipesArr);
	}

	@Test
	public void testRelatorioEndpoint() throws IOException, ParseException {
		RelatoriosEndpoint re = new RelatoriosEndpoint();
		re.setInjectedEntities(em, equipeSvc, new UsuarioAutenticadoBean() {
			@Override
			public synchronized Usuario carregaUsuarioAutenticado(String authorization) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		});

		Response r = re.getCorpo("", "13-11-2016", "14-11-2016");
		System.out.println(r.getEntity());
	}

	@Test
	public void testaReports() throws ParseException {
		Usuario responsavel = userSvc.findByEmail("rafael.coutinho@mj.gov.br");
		Date inicio = new SimpleDateFormat("ddMMyyyy kk:mm").parse("15112016 08:00");
		Date fim = new SimpleDateFormat("ddMMyyyy kk:mm").parse("15112016 18:00");
		System.out.println(responsavel.getId());
		System.out.println(inicio.getTime());
		System.out.println(fim.getTime());

		JSONArray posicionamentosArray = new JSONArray();
		List<Object> list = em.createNamedQuery("contadorPosicionamentosPorResponavel").setParameter("responsavel", responsavel).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getResultList();
		for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
			Object[] object = (Object[]) iterator2.next();
			JSONObject p = new JSONObject();
			p.put("total", object[0]);
			p.put("nome", object[1]);
			posicionamentosArray.put(p);
			System.out.println(p);
		}

	}

	private BigDecimal getTempoMedioNotaTecnica(Usuario responsavel, Date inicio, Date fim) {
		//@formatter:off
		String sql = "select "
				+ "avg(criacao-foiatribuida) "
				+ "from proposicao p left join proposicao_notatecnica pn on pn.proposicao_id=p.id "
				+ "where datacriacao is not null and foiatribuida is not null "
					+ "and p.responsavel_id=:userId and pn.usuario_id=:userId "
					+ "and criacao>foiatribuida "
					+ "and criacao<=:e and foiatribuida>:s "
					+ "and foiatribuida<=:e";
		System.out.println(sql);
		BigDecimal obj = (BigDecimal) em
				.createNativeQuery(sql)
				.setParameter("userId", responsavel.getId())
				.setParameter("s", inicio.getTime())
				.setParameter("e",fim.getTime())
				.getSingleResult();
		//@formatter:on
		return obj;
	}

	private BigDecimal getTempoMedioEmenda(Usuario responsavel, Date inicio, Date fim) {
		//@formatter:off
		String sql = "select "
				+ "avg(criacao-foiatribuida) "
				+ "from proposicao p left join proposicao_emenda pn on pn.proposicao_id=p.id "
				+ "where datacriacao is not null and foiatribuida is not null "
					+ "and p.responsavel_id=:userId and pn.usuario_id=:userId "
					+ "and criacao>foiatribuida "
					+ "and criacao<=:e and foiatribuida>:s "
					+ "and foiatribuida<=:e";
		System.out.println(sql);
		BigDecimal obj = (BigDecimal) em
				.createNativeQuery(sql)
				.setParameter("userId", responsavel.getId())
				.setParameter("s", inicio.getTime())
				.setParameter("e",fim.getTime())
				.getSingleResult();
		//@formatter:on
		return obj;
	}

	@Test
	public void testComNotas() throws Exception {
		List<Proposicao> ps = em.createNamedQuery("findWithNotas").getResultList();// em.createQuery("select p from Proposicao where p.notatecnicas is not empty",Proposicao.class).getResultList();
		ps = em.createQuery("select p from Proposicao p where p.notatecnicas is not empty").getResultList();
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();

			Query q = em.createNamedQuery("listNotatecnicaProposicao").setParameter("idProposicao", proposicao.getId());
			List<NotaTecnica> res = q.getResultList();
			System.out.println(proposicao.getTotalNotasTecnicas() + " " + res.size());
		}

	}

	@Test
	public void testBusca() throws Exception {
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.add(Calendar.WEEK_OF_YEAR, 1);
		List<Comissao> ls = comissaoService.listarComissoesCamara();
		// for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
		// Comissao comissao = (Comissao) iterator.next();
		// System.out.println("Comissao " + comissao.getSigla());
		// try {
		// ParserPautaCamara parserPautaCamara = new ParserPautaCamara();
		//
		// Set<PautaReuniaoComissao> pss =
		// parserPautaCamara.getPautaComissao(comissao.getSigla(),
		// comissao.getId(), Conversores.dateToString(dataInicial.getTime(),
		// "yyyyMMdd"),
		// Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
		// // proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
		// // dataInicial.getTime(), dataFinal.getTime());
		// for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator();
		// iterator2.hasNext();) {
		// PautaReuniaoComissao object = (PautaReuniaoComissao)
		// iterator2.next();
		// System.out.println(object + " " + object.getData());
		// SortedSet<ProposicaoPautaComissao> paraSalvar = new
		// TreeSet<ProposicaoPautaComissao>();
		// for (Iterator<ProposicaoPautaComissao> iterator3 =
		// object.getProposicoesDaPauta().iterator(); iterator3
		// .hasNext();) {
		// ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao)
		// iterator3.next();
		// Proposicao p = proposicaoService.findProposicaoBy(Origem.CAMARA,
		// propPauta.getProposicao()
		// .getIdProposicao());
		// System.out.println("\t" + propPauta + " === " + p);
		// if (p != null) {
		// paraSalvar.add(propPauta);
		// }
		//
		// }
		// if (!paraSalvar.isEmpty()) {
		// EntityTransaction trans = em.getTransaction();
		// trans.begin();
		// object.setProposicoesDaPauta(paraSalvar);
		// proposicaoService.savePautaReuniaoComissao(object);
		// trans.commit();
		// }
		//
		// }
		// } catch (Exception e) {
		// System.err.println("Falhou ao buscar para a comissao " +
		// comissao.getSigla() + " " + e.getMessage());
		// }
		// }
		ls = comissaoService.listarComissoesSenado();
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			Comissao comissao = (Comissao) iterator.next();
			System.out.println("Comissao " + comissao.getSigla());
			try {
				ParserPautaSenado parserSenado = new ParserPautaSenado();
				ParserPlenarioSenado parserPlenarioSenado = new ParserPlenarioSenado();
				Set<PautaReuniaoComissao> pss = null;
				if (comissao.getSigla().equals("PLEN")) {
					pss = parserPlenarioSenado.getProposicoes(Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"));
				} else {
					pss = parserSenado.getPautaComissao(comissao.getSigla(), Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"), Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
				}
				// proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
				// dataInicial.getTime(), dataFinal.getTime());
				for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
					PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
					System.out.println(object + " " + object.getData());
					SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
					for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3.hasNext();) {
						ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
						Proposicao p = proposicaoService.findProposicaoBy(Origem.SENADO, propPauta.getProposicao().getIdProposicao());
						System.out.println("\t" + propPauta + " === " + p);
						if (p != null) {
							paraSalvar.add(propPauta);
						}

					}
					if (!paraSalvar.isEmpty()) {
						// EntityTransaction trans = em.getTransaction();
						// trans.begin();
						object.setProposicoesDaPauta(paraSalvar);
						proposicaoService.savePautaReuniaoComissao(object);
						// trans.commit();
					}

				}
			} catch (Exception e) {
				System.err.println("Falhou ao buscar para a comissao " + comissao.getSigla() + " " + e.getMessage());
			}
		}

	}
}