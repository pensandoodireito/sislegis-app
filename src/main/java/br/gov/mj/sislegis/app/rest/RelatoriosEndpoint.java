package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.EfetividadeSAL;
import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.ResultadoCongresso;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.documentos.Briefing;
import br.gov.mj.sislegis.app.model.documentos.Emenda;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EquipeService;
import br.gov.mj.sislegis.app.service.PosicionamentoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;

/**
 * 
 */
@Stateless
@Path("/report")
public class RelatoriosEndpoint {

	@Inject
	private EquipeService equipeService;
	@Inject
	private UsuarioService userService;
	@Inject
	private PosicionamentoService posicionamentoService;
	@Inject
	ComissaoService comissaoService;
	@Inject
	ProposicaoService proposicaoService;
	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;
	@PersistenceContext
	private EntityManager em;

	public void setInjectedEntities(Object... injections) {
		em = (EntityManager) injections[0];
		equipeService = (EquipeService) injections[1];
		controleUsuarioAutenticado = (UsuarioAutenticadoBean) injections[2];
	}

	private BigDecimal getTempoMedioNotaTecnica(Usuario responsavel, Date inicio, Date fim) {
		//@formatter:off
		String sql = "select "
				+ "avg(criacao-foiatribuida) "
				+ "from proposicao p left join proposicao_notatecnica pn on pn.proposicao_id=p.id "
				+ "where criacao is not null and foiatribuida is not null "
					+ "and p.responsavel_id=:userId and pn.usuario_id=:userId "
					+ "and criacao>foiatribuida "
					+ "and criacao<=:e and foiatribuida>:s "
					+ "and foiatribuida<=:e";
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
				+ "where criacao is not null and foiatribuida is not null "
					+ "and p.responsavel_id=:userId and pn.usuario_id=:userId "
					+ "and criacao>foiatribuida "
					+ "and criacao<=:e and foiatribuida>:s "
					+ "and foiatribuida<=:e";
		BigDecimal obj = (BigDecimal) em
				.createNativeQuery(sql)
				.setParameter("userId", responsavel.getId())
				.setParameter("s", inicio.getTime())
				.setParameter("e",fim.getTime())
				.getSingleResult();
		//@formatter:on
		return obj;
	}

	@GET
	@Path("corpoTecnico")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCorpo(@HeaderParam("Authorization") String authorization, @QueryParam("i") String iniciostr, @QueryParam("f") String fimstr) throws IOException, ParseException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
		Date inicio = new SimpleDateFormat("dd-MM-yyyy").parse(iniciostr);
		Date fim = new SimpleDateFormat("dd-MM-yyyy").parse(fimstr);

		JSONObject dashInfo = new JSONObject();

		JSONArray equipesArr = new JSONArray();
		dashInfo.put("equipes", equipesArr);
		List<Equipe> equipes = equipeService.listAll();
		long totalEquipes = 0;
		for (Iterator<Equipe> iterator = equipes.iterator(); iterator.hasNext();) {
			Equipe equipe = (Equipe) iterator.next();
			if (equipe.getNome().contains("ASPAR")) {
				continue;
			}
			JSONObject equipeJson = equipe.toJson();

			JSONArray membrosEquipeArr = new JSONArray();
			equipeJson.put("membros", membrosEquipeArr);

			for (Iterator<Usuario> iteratorQ = equipe.getListaEquipeUsuario().iterator(); iteratorQ.hasNext();) {
				Usuario responsavel = iteratorQ.next();
				JSONObject porResponsavel = new JSONObject();
				porResponsavel.put("id", responsavel.getId());
				porResponsavel.put("nome", responsavel.getNome());

				BigInteger totalAnalisado = (BigInteger) em.createNativeQuery("select count(id) from proposicao where responsavel_id=:userId and foiatribuida>:s and foiatribuida<=:e and foianalisada<=:e  and foianalisada is not null and estado<>:estado").setParameter("estado", EstadoProposicao.EMANALISE.name()).setParameter("userId", responsavel.getId()).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
				porResponsavel.put("analisados", totalAnalisado);

				JSONObject tempo = new JSONObject();
				java.math.BigDecimal tempoMedioAnalise = (java.math.BigDecimal) (BigDecimal) em.createNativeQuery("select avg(foianalisada-foiencaminhada) from proposicao  where responsavel_id=:userId and foiatribuida>:s and foiatribuida<=:e and foianalisada<=:e and foianalisada is not null and estado<>:estado").setParameter("estado", EstadoProposicao.EMANALISE.name()).setParameter("userId", responsavel.getId()).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime())
						.getSingleResult();

				tempo.put("analise", tempoMedioAnalise);

				java.math.BigDecimal tempoMedioNotaTecnica = getTempoMedioNotaTecnica(responsavel, inicio, fim);
				tempo.put("notatecnica", tempoMedioNotaTecnica);

				java.math.BigDecimal tempoMedioEmenda = getTempoMedioEmenda(responsavel, inicio, fim);

				tempo.put("tempoEmenda", tempoMedioEmenda);

				porResponsavel.put("tempo", tempo);

				JSONArray posicionamentosArray = new JSONArray();
				List<Object> list = em.createNamedQuery("contadorPosicionamentosPorResponavel").setParameter("estado", EstadoProposicao.EMANALISE).setParameter("responsavel", responsavel).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getResultList();
				for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
					Object[] object = (Object[]) iterator2.next();
					JSONObject p = new JSONObject();
					p.put("total", object[0]);
					p.put("nome", object[1]);
					posicionamentosArray.put(p);
				}

				porResponsavel.put("posicionamentos", posicionamentosArray);
				membrosEquipeArr.put(porResponsavel);
			}

			equipesArr.put(equipeJson);

		}

		return Response.ok(dashInfo.toString()).build();

	}

	@GET
	@Path("corpoTecnicoResponsavel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCorpoResponsavel(@HeaderParam("Authorization") String authorization, @QueryParam("r") Long idReponsavel, @QueryParam("i") String iniciostr, @QueryParam("f") String fimstr) throws IOException, ParseException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
		Date inicio = new SimpleDateFormat("dd-MM-yyyy").parse(iniciostr);
		Date fim = new SimpleDateFormat("dd-MM-yyyy").parse(fimstr);
		Usuario responsavel = userService.findById(idReponsavel);

		JSONObject dashInfo = new JSONObject();
		List<Posicionamento> posicionamentos = posicionamentoService.listAll();
		JSONArray posicionamentosArray = new JSONArray();

		for (Iterator iterator = posicionamentos.iterator(); iterator.hasNext();) {
			Posicionamento posicionamento = (Posicionamento) iterator.next();
			JSONObject posicionamentoJson = new JSONObject();
			posicionamentoJson.put("id", posicionamento.getId());
			posicionamentoJson.put("nome", posicionamento.getNome());

			List<Proposicao> props = em.createNamedQuery("getAllProposicaoPosicionada4UsuarioPeriodo", Proposicao.class).setParameter("userId", idReponsavel).setParameter("e", fim.getTime()).setParameter("s", inicio.getTime()).setParameter("posicionamento", posicionamento).getResultList();
			posicionamentoJson.put("total", props.size());
			JSONArray propsArray = new JSONArray();
			Double tempoTotalGasto = 0d;
			for (Iterator<Proposicao> iterator2 = props.iterator(); iterator2.hasNext();) {
				Proposicao proposicao = (Proposicao) iterator2.next();

				JSONObject propJson = new JSONObject();
				propJson.put("id", proposicao.getId());
				propJson.put("sigla", proposicao.getSigla());
				propJson.put("tema", proposicao.getExplicacao());
				propJson.put("ementa", proposicao.getEmenta());
				propJson.put("comissao", proposicao.getEmenta());
				String comissaoPorExtenso = proposicao.getComissao();
				Comissao comissao = null;
				try {
					comissao = comissaoService.getComissao(proposicao.getOrigem(), proposicao.getComissao());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (comissao != null) {
					comissaoPorExtenso = comissao.getNome();
				}
				propJson.put("comissaoPorExtenso", comissaoPorExtenso);
				propJson.put("origem", proposicao.getOrigem().name());

				JSONObject totaisJson = new JSONObject();
				totaisJson.put("notas", em.createNamedQuery("listNotasProposicaoPorUsuarioData", NotaTecnica.class).setParameter("userId", idReponsavel).setParameter("idProposicao", proposicao.getId()).setParameter("e", fim.getTime()).setParameter("s", inicio.getTime()).getResultList().size());
				totaisJson.put("briefings", em.createNamedQuery("listBriefingProposicaoPorUsuarioData", Briefing.class).setParameter("userId", idReponsavel).setParameter("idProposicao", proposicao.getId()).setParameter("e", fim.getTime()).setParameter("s", inicio.getTime()).getResultList().size());
				totaisJson.put("emendas", em.createNamedQuery("listEmendasProposicaoPorUsuarioData", Emenda.class).setParameter("userId", idReponsavel).setParameter("idProposicao", proposicao.getId()).setParameter("e", fim.getTime()).setParameter("s", inicio.getTime()).getResultList().size());
				propJson.put("totais", totaisJson);
				propsArray.put(propJson);

				if (proposicao.getFoiAnalisada() != null && proposicao.getFoiAtribuida() != null) {
					tempoTotalGasto += (proposicao.getFoiAnalisada() - proposicao.getFoiAtribuida());
					propJson.put("tempoGasto", (proposicao.getFoiAnalisada() - proposicao.getFoiAtribuida()));
				}
				propJson.put("foiAnalisada", proposicao.getFoiAnalisada());
				propJson.put("foiAtribuida", proposicao.getFoiAtribuida());

			}
			posicionamentoJson.put("proposicoes", propsArray);
			posicionamentoJson.put("total", props.size());
			posicionamentoJson.put("tempoTotalGasto", tempoTotalGasto);
			posicionamentosArray.put(posicionamentoJson);

		}
		dashInfo.put("posicionamentos", posicionamentosArray);

		return Response.ok(dashInfo.toString()).build();

	}

	@GET
	@Path("corpoTecnicoPosicoes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCorpoPosicoes(@HeaderParam("Authorization") String authorization, @QueryParam("i") String iniciostr, @QueryParam("f") String fimstr) throws IOException, ParseException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
		Date inicio = new SimpleDateFormat("dd-MM-yyyy").parse(iniciostr);
		Date fim = new SimpleDateFormat("dd-MM-yyyy").parse(fimstr);

		JSONObject dashInfo = new JSONObject();

		JSONArray equipesArr = new JSONArray();
		dashInfo.put("equipes", equipesArr);
		List<Equipe> equipes = equipeService.listAll();

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

				totalEquipe += (Long) object[0];
				equipeJson.put(mapa.get((String) object[2]), object[0]);

			}
			equipeJson.put("total", totalEquipe);

			equipesArr.put(equipeJson);

		}

		return Response.ok(dashInfo.toString()).build();

	}

	@GET
	@Path("efetividadeCongresso")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEfetividade(@HeaderParam("Authorization") String authorization, @QueryParam("i") String iniciostr, @QueryParam("f") String fimstr) throws IOException, ParseException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
		Date inicio = new SimpleDateFormat("dd-MM-yyyy").parse(iniciostr);
		Date fim = new SimpleDateFormat("dd-MM-yyyy").parse(fimstr);

		JSONObject dashInfo = new JSONObject();
		List<Posicionamento> posicionamentos = posicionamentoService.listAll();
		JSONArray posicionamentosArray = new JSONArray();

		for (Iterator<Posicionamento> iterator = posicionamentos.iterator(); iterator.hasNext();) {
			Posicionamento posicionamento = (Posicionamento) iterator.next();
			JSONObject posicionamentoJson = new JSONObject();
			posicionamentoJson.put("id", posicionamento.getId());
			posicionamentoJson.put("nome", posicionamento.getNome());

			List<Proposicao> props = em.createNamedQuery("getAllProposicaoPosicionadaComResultado", Proposicao.class).setParameter("emTramitacao", ResultadoCongresso.EM_TRAMITACAO).setParameter("e", fim.getTime()).setParameter("s", inicio.getTime()).setParameter("posicionamento", posicionamento).getResultList();
			JSONArray propsArray = new JSONArray();

			int totalAprovada = 0;
			int totalNaoAprovada = 0;
			int totalNaoAvancou = 0;
			for (Iterator<Proposicao> iterator2 = props.iterator(); iterator2.hasNext();) {
				Proposicao proposicao = (Proposicao) iterator2.next();

				JSONObject propJson = new JSONObject();
				propJson.put("id", proposicao.getId());
				propJson.put("sigla", proposicao.getSigla());
				propJson.put("tema", proposicao.getExplicacao());
				propJson.put("ementa", proposicao.getEmenta());
				propJson.put("efetividade", proposicao.getEfetividade().name());
				propJson.put("equipe", proposicao.getEquipe() != null ? proposicao.getEquipe().getNome() : "Não definida");
				propJson.put("responsavel", proposicao.getEquipe() != null ? proposicao.getResponsavel().getNome() : "Não definido");

				propJson.put("resultadoCongresso", proposicao.getResultadoCongresso().name());

				propJson.put("comissao", proposicao.getEmenta());

				String comissaoPorExtenso = proposicao.getComissao();
				Comissao comissao = null;
				try {
					comissao = comissaoService.getComissao(proposicao.getOrigem(), proposicao.getComissao());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (comissao != null) {
					comissaoPorExtenso = comissao.getNome();
				}
				propJson.put("comissaoPorExtenso", comissaoPorExtenso);
				propJson.put("origem", proposicao.getOrigem().name());
				switch (proposicao.getResultadoCongresso()) {
				case APROVADA:
					totalAprovada++;
					break;
				case NAO_APROVADA:
					totalNaoAprovada++;
					break;
				case NAO_AVANCOU:
					totalNaoAvancou++;
					break;

				default:
					break;
				}
				propsArray.put(propJson);

			}
			posicionamentoJson.put("proposicoes", propsArray);
			posicionamentoJson.put(ResultadoCongresso.APROVADA.name(), totalAprovada);
			posicionamentoJson.put(ResultadoCongresso.NAO_APROVADA.name(), totalNaoAprovada);
			posicionamentoJson.put(ResultadoCongresso.NAO_AVANCOU.name(), totalNaoAvancou);
			posicionamentoJson.put("total", props.size());
			posicionamentosArray.put(posicionamentoJson);

		}
		dashInfo.put("posicionamentos", posicionamentosArray);

		Long totalContrarioNaoAprovado = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Contrário")).setParameter("resultadoCongresso", ResultadoCongresso.NAO_APROVADA).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalContrarioNaoAvancou = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Contrário")).setParameter("resultadoCongresso", ResultadoCongresso.NAO_AVANCOU).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalContrarioAprovados = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Contrário")).setParameter("resultadoCongresso", ResultadoCongresso.APROVADA).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalFavoravelAprovados = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Favorável")).setParameter("resultadoCongresso", ResultadoCongresso.APROVADA).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalFavoravelNaoAprovados = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Favorável")).setParameter("resultadoCongresso", ResultadoCongresso.NAO_APROVADA).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalFavoravelNaoAvancou = (Long) em.createNamedQuery("contadorResultadoCongressoPorPosicionamento").setParameter("posicionamento", posicionamentoService.getByName("Favorável")).setParameter("resultadoCongresso", ResultadoCongresso.NAO_AVANCOU).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();

		dashInfo.put("totalContrarioNaoAprovado", totalContrarioNaoAprovado);
		dashInfo.put("totalContrarioAprovados", totalContrarioAprovados);
		dashInfo.put("totalContrarioNaoAvancou", totalContrarioNaoAvancou);

		dashInfo.put("totalFavoravelNaoAprovados", totalFavoravelNaoAprovados);
		dashInfo.put("totalFavoravelAprovados", totalFavoravelAprovados);
		dashInfo.put("totalFavoravelNaoAvancou", totalFavoravelNaoAvancou);

		Long totalComResultados = (Long) em.createNamedQuery("contadorComResultado").setParameter("resultadoCongresso", ResultadoCongresso.EM_TRAMITACAO).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();

		Long totalEmendasAcatadas = (Long) em.createNamedQuery("contadorEmendasComResultado").setParameter("resultadoCongresso", ResultadoCongresso.EM_TRAMITACAO).setParameter("emendas", EfetividadeSAL.ACATADA).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		Long totalEmendasPontosImportantes = (Long) em.createNamedQuery("contadorEmendasComResultado").setParameter("resultadoCongresso", ResultadoCongresso.EM_TRAMITACAO).setParameter("emendas", EfetividadeSAL.ACATADA_PONTOS_IMPORTANTES).setParameter("s", inicio.getTime()).setParameter("e", fim.getTime()).getSingleResult();
		dashInfo.put("totalEmendasAcatadas", totalEmendasAcatadas);
		dashInfo.put("totalEmendasPontosImportantes", totalEmendasPontosImportantes);

		dashInfo.put("totalComResultados", totalComResultados);

		return Response.ok(dashInfo.toString()).build();
	}

	@GET
	@Path("desempenho")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@HeaderParam("Authorization") String authorization) throws IOException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);

		JSONObject dashInfo = new JSONObject();

		JSONArray equipesArr = new JSONArray();
		dashInfo.put("equipes", equipesArr);
		List<Equipe> equipes = equipeService.listAll();
		long totalEquipes = 0;
		for (Iterator iterator = equipes.iterator(); iterator.hasNext();) {
			Equipe equipe = (Equipe) iterator.next();
			if (equipe.getNome().contains("ASPAR")) {
				continue;
			}
			JSONObject equipeJson = equipe.toJson();

			JSONArray membrosEquipeArr = new JSONArray();
			equipeJson.put("membros", membrosEquipeArr);

			Query q = em.createNativeQuery("select eq.id as idEquipe,eq.nome as nomeEquipe,u.nome as nomeUsuario,u.email,u.id,count(p.id) total from Usuario u left join (select id,responsavel_id from Proposicao where idequipe=:idEquipe) p on p.responsavel_id=u.id, Equipe eq where eq.id=u.idequipe and eq.id=:idEquipe group by eq.id,eq.nome,u.id,u.nome,u.email").setParameter("idEquipe", equipe.getId());
			List<Object[]> resultadoAssociadaPorMembro = q.getResultList();
			long totalProcessadas = 0;
			for (Iterator iteratorQ = resultadoAssociadaPorMembro.iterator(); iteratorQ.hasNext();) {
				Object[] objects = (Object[]) iteratorQ.next();
				JSONObject porResponsavel = new JSONObject();
				porResponsavel.put("id", objects[4]);
				porResponsavel.put("nome", objects[2]);
				porResponsavel.put("associadas", objects[5]);

				membrosEquipeArr.put(porResponsavel);
			}
			Long totalComAEquipe = (Long) em.createQuery("select count(p.id) from Proposicao p where p.equipe.id=:idEquipe", Long.class).setParameter("idEquipe", equipe.getId()).getSingleResult();
			totalEquipes += totalComAEquipe;
			equipeJson.put("totalEquipe", totalComAEquipe);

			equipesArr.put(equipeJson);

		}

		Long totalNoSislegis = (Long) em.createQuery("select count(p.id) from Proposicao p", Long.class).getSingleResult();
		dashInfo.put("semEquipe", totalNoSislegis - totalEquipes);
		dashInfo.put("totalNoSislegis", totalNoSislegis);

		return Response.ok(dashInfo.toString()).build();
	}

}