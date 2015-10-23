package br.gov.mj.sislegis.app.rest;

import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.Service;
import br.gov.mj.sislegis.app.service.TarefaService;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.List;


@Path("/tarefas")
public class TarefaEndpoint {
	@Inject
	private Service<Tarefa> service;
	
	@Inject
	private TarefaService tarefaService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tarefa entity, @HeaderParam("Referer") String referer) {
		tarefaService.save(entity, referer);
		return Response.created(UriBuilder.fromResource(TarefaEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		service.deleteById(id);
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
		return service.listAll();
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
	public Response update(Tarefa entity, @HeaderParam("Referer") String referer) {
		try {

			// validar o campo comentario quando a tarefa for finalizada
			if (entity.isFinalizada() && StringUtils.isEmpty(entity.getComentarioFinalizacao())){
				entity.setFinalizada(false);
				return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
			}

			tarefaService.save(entity, referer);

		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

	@POST
	@Path("/marcarVisualizadas")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response marcarComoVisualizadas(List<Long> idTarefas, @HeaderParam("Referer") String referer){
		tarefaService.marcarComoVisualizadas(idTarefas);
		return Response.noContent().build();
	}
}