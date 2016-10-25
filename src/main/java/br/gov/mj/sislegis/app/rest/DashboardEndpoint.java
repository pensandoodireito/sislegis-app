package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Papel;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.EquipeService;

/**
 * 
 */
@Stateless
@Path("/dashboard")
public class DashboardEndpoint {

	@Inject
	private EquipeService equipeService;
	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;
	@PersistenceContext
	private EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@HeaderParam("Authorization") String authorization) throws IOException {
		Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
		Calendar inicioMes = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));

		JSONObject dashInfo = new JSONObject();
		inicioMes.set(Calendar.DAY_OF_MONTH, 1);
		inicioMes.set(Calendar.HOUR_OF_DAY, 0);
		inicioMes.set(Calendar.MINUTE, 0);
		inicioMes.set(Calendar.SECOND, 0);
		inicioMes.set(Calendar.MILLISECOND, 0);
		Calendar inicioAno = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
		inicioAno.setTimeInMillis(inicioMes.getTimeInMillis());
		inicioAno.set(Calendar.MONTH, 0);
		dashInfo.put("inicioMes", inicioMes.getTimeInMillis());
		Long totalProposicoesAlteradasNoMes = (Long) em
				.createQuery("select count(p.id) from Proposicao p where p.updated>:data")
				.setParameter("data", inicioMes.getTime()).getSingleResult();

		dashInfo.put("totalProposicoesAlteradasNoMes", totalProposicoesAlteradasNoMes);
		Long totalProposicoesAlteradasNoAno = (Long) em
				.createQuery("select count(p.id) from Proposicao p where p.updated>:data")
				.setParameter("data", inicioAno.getTime()).getSingleResult();

		dashInfo.put("totalProposicoesAlteradasNoAno", totalProposicoesAlteradasNoAno);

		Long totalProposicoesEmAnalise = (Long) em
				.createQuery("select count(p.id) from Proposicao p where p.estado=:estado")

				.setParameter("estado", EstadoProposicao.EMANALISE).getSingleResult();
		dashInfo.put("totalProposicoesEmAnalise", totalProposicoesEmAnalise);

		if (user.getPapeis().contains(Papel.SECRETARIO)) {

			Long totalProposicoesDespachadasNoMes = (Long) em
					.createQuery("select count(p.id) from Proposicao p where p.updated>:data and estado=:estado")
					.setParameter("data", inicioMes.getTime()).setParameter("estado", EstadoProposicao.DESPACHADA)
					.getSingleResult();
			dashInfo.put("totalProposicoesDespachadasNoMes", totalProposicoesDespachadasNoMes);

			Long totalProposicoesDespachadasNoAno = (Long) em
					.createQuery("select count(p.id) from Proposicao p where p.updated>:data and estado=:estado")
					.setParameter("data", inicioAno.getTime()).setParameter("estado", EstadoProposicao.DESPACHADA)
					.getSingleResult();
			dashInfo.put("totalProposicoesDespachadasNoAno", totalProposicoesDespachadasNoAno);

			Long totalProposicoesADespachar = (Long) em
					.createQuery("select count(p.id) from Proposicao p where p.estado=:estado")
					.setParameter("estado", EstadoProposicao.ADESPACHAR).getSingleResult();
			dashInfo.put("totalProposicoesADespachar", totalProposicoesADespachar);

		} else if (user.getPapeis().contains(Papel.DIRETOR)) {
			if (user.getEquipe() == null) {
				dashInfo.put("error", "Não foi possível identificar equipe do diretor.");
			} else {
				Equipe equipe = user.getEquipe();
				Query q = em
						.createNativeQuery(
								"select eq.id as idEquipe,eq.nome as nomeEquipe,u.nome as nomeUsuario,u.email,u.id,count(p.id) from Usuario u left join (select id,responsavel_id from Proposicao where estado=:estado and idequipe=:idEquipe) p on p.responsavel_id=u.id, Equipe eq where eq.id=u.idequipe and eq.id=:idEquipe group by eq.id,eq.nome,u.id,u.nome,u.email")
						.setParameter("idEquipe", equipe.getId())
						.setParameter("estado", EstadoProposicao.EMANALISE.name());

				JSONArray porResponsavelArr = new JSONArray();
				List<Object[]> results = q.getResultList();
				Map<Long, JSONObject> membros = new HashMap<Long, JSONObject>();
				long totalEmAnalise = 0;
				for (Iterator iterator = results.iterator(); iterator.hasNext();) {
					Object[] objects = (Object[]) iterator.next();
					JSONObject porResponsavel = new JSONObject();
					porResponsavel.put("id", objects[4]);
					porResponsavel.put("nome", objects[2]);
					porResponsavel.put("totalEmAnalise", objects[5]);
					totalEmAnalise += ((BigInteger) objects[5]).longValue();
					membros.put(((BigInteger) objects[4]).longValue(), porResponsavel);
					porResponsavelArr.put(porResponsavel);
				}

				Query q1 = em
						.createNativeQuery(
								"select eq.id as idEquipe,eq.nome as nomeEquipe,u.nome as nomeUsuario,u.email,u.id,count(p.id) from Usuario u left join (select id,responsavel_id from Proposicao where (estado=:estado1 or estado=:estado2 or estado=:estado3) and idequipe=:idEquipe and updated>:data) p on p.responsavel_id=u.id, Equipe eq where eq.id=u.idequipe and eq.id=:idEquipe group by eq.id,eq.nome,u.id,u.nome,u.email")
						.setParameter("idEquipe", equipe.getId()).setParameter("data", inicioMes.getTime())
						.setParameter("estado1", EstadoProposicao.ANALISADA.name())
						.setParameter("estado2", EstadoProposicao.ADESPACHAR.name())
						.setParameter("estado3", EstadoProposicao.DESPACHADA.name());
				List<Object[]> resultadoAnalisadasPorMembro = q1.getResultList();
				long totalEmAnalisadas = 0;
				for (Iterator iterator = resultadoAnalisadasPorMembro.iterator(); iterator.hasNext();) {
					Object[] objects = (Object[]) iterator.next();
					JSONObject porResponsavel = membros.get(((BigInteger) objects[4]).longValue());
					totalEmAnalisadas += ((BigInteger) objects[5]).longValue();
					porResponsavel.put("totalAnalisadas", objects[5]);

				}

				Long totalSemResponsavel = (Long) em
						.createQuery(
								"select count(p.id) from Proposicao p where p.estado=:estado and p.equipe.id=:idEquipe and p.responsavel is null")
						.setParameter("idEquipe", equipe.getId()).setParameter("estado", EstadoProposicao.EMANALISE)
						.getSingleResult();
				totalEmAnalise += totalSemResponsavel;

				Long totalEmRevisao = (Long) em
						.createQuery(
								"select count(p.id) from Proposicao p where p.estado=:estado and p.equipe.id=:idEquipe")
						.setParameter("idEquipe", equipe.getId()).setParameter("estado", EstadoProposicao.ANALISADA)
						.getSingleResult();

				JSONObject minhaEquipe = new JSONObject();
				minhaEquipe.put("equipe", equipe.toJson());
				minhaEquipe.put("pessoal", porResponsavelArr);
				minhaEquipe.put("totalSemResponsavel", totalSemResponsavel);
				minhaEquipe.put("totalEmAnalise", totalEmAnalise);
				minhaEquipe.put("totalAnalisadas", totalEmAnalisadas);
				
				minhaEquipe.put("totalEmRevisao", totalEmRevisao);
				dashInfo.put("minhaEquipe", minhaEquipe);
			}
		}
		List<Equipe> equipes = equipeService.listAll();
		JSONArray equipesArr = new JSONArray();
		for (Iterator<Equipe> iterator = equipes.iterator(); iterator.hasNext();) {
			Equipe equipe = (Equipe) iterator.next();
			if (equipe.getNome().contains("ASPAR")) {
				continue;
			}
			JSONObject porEquipe = new JSONObject();
			porEquipe.put("e", equipe.toJson());

			Long totalEmAnalise = (Long) em
					.createQuery(
							"select count(p.id) from Proposicao p where p.estado=:estado and p.updated>:data and p.equipe.id=:idEquipe")
					.setParameter("idEquipe", equipe.getId()).setParameter("data", inicioMes.getTime())
					.setParameter("estado", EstadoProposicao.EMANALISE).getSingleResult();
			Long totalEmAnalisada = (Long) em
					.createQuery(
							"select count(p.id) from Proposicao p where p.estado=:estado and p.updated>:data and p.equipe.id=:idEquipe")
					.setParameter("idEquipe", equipe.getId()).setParameter("data", inicioMes.getTime())
					.setParameter("estado", EstadoProposicao.ANALISADA).getSingleResult();

			porEquipe.put("totalEmAnalise", totalEmAnalise);
			porEquipe.put("totalAnalisada", totalEmAnalisada);
			equipesArr.put(porEquipe);
		}
		Long totalSemEquipe = (Long) em
				.createQuery(
						"select count(p.id) from Proposicao p where p.estado=:estado and p.updated>:data and p.equipe.id is null")
				.setParameter("data", inicioMes.getTime()).setParameter("estado", EstadoProposicao.EMANALISE)
				.getSingleResult();
		dashInfo.put("semEquipe", totalSemEquipe);
		dashInfo.put("equipes", equipesArr);

		return Response.ok(dashInfo.toString()).build();
	}
}