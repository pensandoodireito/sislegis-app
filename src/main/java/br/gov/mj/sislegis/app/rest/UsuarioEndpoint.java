package br.gov.mj.sislegis.app.rest;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.model.Papel;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.UsuarioService;

/**
 * 
 */
@Stateless
@Path("/usuarios")
public class UsuarioEndpoint {

	@Inject
	private UsuarioService service;
	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMe(@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);

			return Response.ok(user).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Usuario entity) {
		service.save(entity);
		return Response.created(
				UriBuilder.fromResource(UsuarioEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		service.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/proposicoesSeguidas")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listProposicoesSeguidas(@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			Collection<Proposicao> proposicoesSeguidas = service.proposicoesSeguidas(user.getId());
			return Response.ok(proposicoesSeguidas).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		return Response.ok(service.findById(id)).build();
	}

	@GET
	@Path("/findByIdEquipe")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> findByIdEquipe(@QueryParam("idEquipe") Long id) {
		return service.findByIdEquipe(id);
	}

	@GET
	@Path("/find{nome:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNome(@QueryParam("nome") String nome) {
		return Response.ok(service.findByNome(nome)).build();
	}

	@GET
	@Path("/ldapSearch{nome:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNomeOnLDAP(@QueryParam("nome") String nome) {
		return Response.ok(service.findByNomeOnLDAP(nome)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		return service.listAll();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Usuario entity) {
		try {
			entity = service.save(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}