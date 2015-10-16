package br.gov.mj.sislegis.app.rest;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ComentarioService;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.List;

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
	public Response create(ComentarioJSON entity, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			comentarioService.salvarComentario(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}  catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.created(UriBuilder.fromResource(ComentarioEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
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
	public List<ComentarioJSON> findByProposicao(@PathParam("id") Long id) {
		final List<ComentarioJSON> results = comentarioService
				.findByProposicao(id);
		return results;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comentario> listAll(@QueryParam("start") Integer startPosition,	@QueryParam("max") Integer maxResult) {
		final List<Comentario> results = comentarioService.listAll();
		return results;
	}

	@GET
	@Path("/total/{idProposicao:[0-9][0-9]*}")
	@Produces(MediaType.TEXT_PLAIN)
	public Long totalByProposicao(@PathParam("idProposicao") Long idProposicao){
		return comentarioService.totalByProposicao(idProposicao);
	}

	@GET
	@Path("/proposicaoLazy/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ComentarioJSON> findByProposicaoLazy(@QueryParam("idProposicao") Long idProposicao, @QueryParam("posicaoInicial") Integer posicaoInicial, @QueryParam("limite")Integer limite){
		return comentarioService.findByProposicao(idProposicao, posicaoInicial, limite);
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(ComentarioJSON entity,
			@HeaderParam("Authorization") String authorization) {
		try {
			comentarioService.salvarComentario(entity, controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization));

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
}