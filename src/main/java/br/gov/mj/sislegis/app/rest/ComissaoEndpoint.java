package br.gov.mj.sislegis.app.rest;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.Service;

/**
 * 
 */
@Path("/comissaos")
public class ComissaoEndpoint {

	@Inject
	private ComissaoService comissaoService;

	@Inject
	private Service<Comissao> service;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Comissao entity) {
		service.save(entity);
		return Response.created(
				UriBuilder.fromResource(ComissaoEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
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
		Comissao entity = comissaoService.findById(id);
		return Response.ok(entity).build();
	}
	@GET
	@Path("/comissoesCamara")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comissao> listarComissoesCamara() throws Exception {
		return comissaoService.listarComissoesCamara();
	}

	@GET
	@Path("/comissoesSenado")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comissao> listarComissoesSenado() throws Exception {
		return comissaoService.listarComissoesSenado();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comissao> listAll() {
		final List<Comissao> results = service.listAll();
		return results;
	}	

	
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Comissao entity) {
		try {
			entity = comissaoService.save(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}