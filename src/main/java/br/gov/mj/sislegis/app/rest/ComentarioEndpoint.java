package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.List;

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

import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ComentarioService;

@Path("/comentarios")
public class ComentarioEndpoint {

	@Inject
	private ComentarioService comentarioService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	public ComentarioEndpoint() {
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Comentario entity, @HeaderParam("Authorization") String authorization) {

		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			comentarioService.salvarComentario(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response
				.created(UriBuilder.fromResource(ComentarioEndpoint.class).path(String.valueOf(entity.getId())).build())
				.header("Access-Control-Expose-Headers", "Location").build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		comentarioService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		Comentario entity = comentarioService.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Path("/proposicao/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comentario> findByProposicao(@PathParam("id") Long id) {
		final List<Comentario> results = comentarioService.findByIdProposicao(id);
		return results;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comentario> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		final List<Comentario> results = comentarioService.listAll();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Comentario entity, @HeaderParam("Authorization") String authorization) {
		try {
			comentarioService.salvarComentario(entity,
					controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization));

		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response.noContent().build();
	}

	@PUT
	@Path("/ocultar/{id:[0-9][0-9]*}")
	public Response ocultar(@PathParam("id") Long id){
		try {
			comentarioService.ocultar(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok().build();
	}
}