package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.TarefaService;

@Path("/tarefas")
public class TarefaEndpoint {

	@Inject
	private TarefaService tarefaService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tarefa entity) {
		tarefaService.save(entity);
		return Response.created(
				UriBuilder.fromResource(TarefaEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		tarefaService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		return Response.ok(tarefaService.buscarPorId(id)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tarefa> listAll(@QueryParam("start") Integer startPosition,	@QueryParam("max") Integer maxResult) {
		return tarefaService.listAll();
	}

	@GET
	@Path("/usuario")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tarefa> buscarPorUsuario(@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			List<Tarefa> tarefas = tarefaService.buscarPorUsuario(user.getId());
			return tarefas;

		} catch (IOException e) {
			// TODO O que fazer???
			e.printStackTrace();
			return null;
		}
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Tarefa entity) {
		try {
			tarefaService.save(entity);

		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

	@POST
	@Path("/marcarVisualizadas")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response marcarComoVisualizadas(List<Long> idTarefas, @HeaderParam("Referer") String referer) {
		tarefaService.marcarComoVisualizadas(idTarefas);
		return Response.noContent().build();
	}

	@POST
	@Path("/finalizar")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response finalizar(@FormParam("idTarefa") Long idTarefa, @FormParam("descricaoComentario") String descricaoComentario){
		tarefaService.finalizar(idTarefa, descricaoComentario);
		return Response.ok().build();
	}
}