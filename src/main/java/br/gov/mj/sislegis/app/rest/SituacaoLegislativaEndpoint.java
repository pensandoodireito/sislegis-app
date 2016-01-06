package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.SituacaoLegislativa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.SituacaoLegislativaService;

@Path("/situacoes")
public class SituacaoLegislativaEndpoint {

	@Inject
	private SituacaoLegislativaService situacaoService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		SituacaoLegislativa entity = situacaoService.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Path("/SENADO/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SituacaoLegislativa> listSenado() {
		return situacaoService.listSituacoes(Origem.SENADO);
	}

	@GET
	@Path("/CAMARA/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SituacaoLegislativa> listCamara() {
		return situacaoService.listSituacoes(Origem.CAMARA);
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(SituacaoLegislativa entity, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			if (user == null) {
				throw new IllegalAccessException();
			}
			situacaoService.save(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return Response.status(Response.Status.FORBIDDEN).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

}
