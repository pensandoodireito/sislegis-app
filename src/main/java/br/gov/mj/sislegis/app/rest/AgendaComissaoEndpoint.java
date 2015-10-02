package br.gov.mj.sislegis.app.rest;

import java.io.Serializable;
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
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;

/**
 * 
 */
@Path("/agendacomissao")
public class AgendaComissaoEndpoint {

	@Inject
	private AgendaComissaoService service;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;
	@Inject
	private UsuarioService usuarioService;

	@POST
	@Path("/{comissao:[A-Z]*}")
	public Response follow(@PathParam("comissao") String comissao, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			service.followComissao(comissao, user);
			return Response.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@DELETE
	@Path("/{comissao:[A-Z]*}")
	public Response unfollow(@PathParam("comissao") String comissao, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			service.unfollowComissao(comissao, user);
			return Response.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

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

			AgendaComissao agenda = service.getAgenda(comissao.trim(), true);
			if (agenda == null) {
				return Response.noContent().build();
			} else {
				user = usuarioService.loadComAgendasSeguidas(user.getId());
				boolean seguindo = user.getAgendasSeguidas().contains(agenda);
				DetalhesAgendaComissao detalhes = new DetalhesAgendaComissao(agenda, seguindo);
				return Response.ok(detalhes).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	class DetalhesAgendaComissao implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5000997074521269387L;

		public DetalhesAgendaComissao(AgendaComissao agenda2, boolean seguindo) {
			this.agenda = agenda2;
			this.seguindo = seguindo;
		}

		public AgendaComissao getAgenda() {
			return agenda;
		}

		public boolean isSeguindo() {
			return seguindo;
		}

		AgendaComissao agenda;
		boolean seguindo = false;
	}
}