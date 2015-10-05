package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.UsuarioService;

/**
 * 
 */
@Path("/comentarios")
public class ComentarioEndpoint {

	@Inject
	private ComentarioService comentarioService;
	@Inject
	private UsuarioService usuarioService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	public ComentarioEndpoint() {
		super();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(ComentarioJSON entity,
			@HeaderParam("Authorization") String authorization) {

		try {
			Usuario user = controleUsuarioAutenticado
					.carregaUsuarioAutenticado(authorization);
			comentarioService.salvarComentario(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		}  catch (IOException e) {
			// TODO O que fazer???
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		return Response.created(
				UriBuilder.fromResource(ComentarioEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
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
	public List<Comentario> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<Comentario> results = comentarioService.listAll();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(ComentarioJSON entity,
			@HeaderParam("Authorization") String authorization) {
		try {

			comentarioService.salvarComentario(entity,
					controleUsuarioAutenticado
							.carregaUsuarioAutenticado(authorization));

		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		} catch (IOException e) {
			// TODO O que fazer???
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}
}