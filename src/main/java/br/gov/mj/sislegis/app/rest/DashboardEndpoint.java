package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

		}
		List<Equipe> equipes = equipeService.listAll();
		JSONArray equipesArr = new JSONArray();
		for (Iterator iterator = equipes.iterator(); iterator.hasNext();) {
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