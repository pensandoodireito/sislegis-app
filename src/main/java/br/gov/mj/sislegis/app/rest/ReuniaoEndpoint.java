package br.gov.mj.sislegis.app.rest;


import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.Service;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Date;
import java.util.List;


@Path("/reuniaos")
public class ReuniaoEndpoint {

	@Inject
	private Service<Reuniao> service;

	@Inject
	private ProposicaoService proposicaoService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Reuniao entity) {
		service.save(entity);
		return Response.created(
				UriBuilder.fromResource(ReuniaoEndpoint.class)
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
		return Response.ok(service.findById(id)).build();
	}

	@GET
	@Path("/findByData")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> findByData(@QueryParam("data") Date data) throws Exception {
		List<Proposicao> lista = proposicaoService.buscarProposicoesPorDataReuniao(data);
		return lista;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reuniao> listAll() {
		List<Reuniao> results = service.listAll();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Reuniao entity) {
		try {
			entity = service.save(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
