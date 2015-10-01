package br.gov.mj.sislegis.app.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.AgendaComissaoService;

/**
 * 
 */
@Path("/agendacomissao")
public class AgendaComissaoEndpoint {

	@Inject
	private AgendaComissaoService service;
	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@POST
	@Path("/follow/{comissao:[A-Z]*}")
	public Response follow(@PathParam("comissao") String comissao, @HeaderParam("Authorization") String authorization) {
		System.out.println("Follow " + comissao);
		System.out.println(service.listAgendasSeguidas());

		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response unfollow(@PathParam("id") Long id, @HeaderParam("Authorization") String authorization) {
		service.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<AgendaComissao> listAll() throws Exception {
		return service.listAll();
	}

	@GET
	@Path("/{comissao:[A-Z]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgendaComissao(@PathParam("comissao") String comissao,
			@HeaderParam("Authorization") String authorization) throws Exception {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);

			AgendaComissao agenda = service.getAgenda(comissao);
			boolean seguindo = user.getAgendasSeguidas().contains(agenda);
			DetalhesAgendaComissao detalhes = new DetalhesAgendaComissao(agenda, seguindo);
			return Response.created(
					UriBuilder.fromResource(AgendaComissaoEndpoint.class).path(String.valueOf(agenda.getId())).build())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	class DetalhesAgendaComissao {
		public DetalhesAgendaComissao(AgendaComissao agenda2, boolean seguindo) {
			this.agenda = agenda2;
			this.seguindo = seguindo;
		}

		AgendaComissao agenda;
		boolean seguindo = false;
	}
}