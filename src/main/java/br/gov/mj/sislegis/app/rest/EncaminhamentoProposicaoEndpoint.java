package br.gov.mj.sislegis.app.rest;

import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/encaminhamentoProposicao")
public class EncaminhamentoProposicaoEndpoint {

	@Inject
	private EncaminhamentoProposicaoService service;

	@Inject
	private UsuarioService usuarioService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(EncaminhamentoProposicao entity, @HeaderParam("Referer") String referer) {
		EncaminhamentoProposicao savedEntity = service.salvarEncaminhamentoProposicao(entity, referer);
		
		return Response.created(
				UriBuilder.fromResource(TipoEncaminhamentoEndpoint.class)
						.path(String.valueOf(savedEntity.getId())).build()).build();
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
	@Produces(MediaType.APPLICATION_JSON)
	public List<EncaminhamentoProposicao> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		return service.listAll();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(EncaminhamentoProposicao entity, @HeaderParam("Referer") String referer) {
		try {
			entity = service.salvarEncaminhamentoProposicao(entity, referer);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

	@GET
	@Path("/proposicao/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EncaminhamentoProposicao> findByProposicao(@PathParam("id") Long id) {
		final List<EncaminhamentoProposicao> results = service.findByProposicao(id);
		return results;
	}

	@POST
	@Path("/finalizar")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response finalizar(@FormParam("idEncaminhamentoProposicao") Long idEncaminhamentoProposicao, @FormParam("descricaoComentario") String descricaoComentario){
		service.finalizar(idEncaminhamentoProposicao, descricaoComentario);
		return Response.ok().build();
	}
}
