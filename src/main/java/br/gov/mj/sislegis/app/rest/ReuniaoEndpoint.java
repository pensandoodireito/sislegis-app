package br.gov.mj.sislegis.app.rest;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.jboss.resteasy.annotations.GZIP;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.Service;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Path("/reuniaos")
@GZIP
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
				UriBuilder.fromResource(ReuniaoEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
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
	public Collection<Proposicao> findByData(@QueryParam("data") Date data,
			@QueryParam("responsavel") Long idResponsavel, @QueryParam("posicionamento") Long idPosicionameto,
			@QueryParam("comissao") String comissao, @QueryParam("origem") String origem,
			@QueryParam("isFavorita") String isFavorita, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("proposicaoIds") Integer[] idsProposicoes) throws Exception {
		long start = 0;
		if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.ALL)) {
			start = System.currentTimeMillis();
		}

		Collection<Proposicao> lista = proposicaoService.buscarProposicoesPorDataReuniao(data, comissao, idResponsavel,
				origem, isFavorita, idPosicionameto, limit, offset, idsProposicoes);
		if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.ALL)) {
			long stop = System.currentTimeMillis();
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.ALL,
					"Carregamento findByData levou " + (stop - start) + " ms para " + lista.size() + " proposicoes");
		}

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
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
