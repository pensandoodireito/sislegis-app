package br.gov.mj.sislegis.app.rest;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
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

import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.EquipeService;

/**
 * 
 */
@Stateless
@Path("/equipes")
public class EquipeEndpoint {

	@Inject
	private EquipeService equipeService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(EquipeWrapper entityw) {
		Equipe entity = equipeService.salvarEquipe(entityw.equipe);
		return Response.created(
				UriBuilder.fromResource(EquipeEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		equipeService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		Equipe equipe = equipeService.findByIdFull(id);
		return Response.ok(new EquipeWrapper(equipe)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipe> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		return equipeService.listAll();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(EquipeWrapper entity) {
		try {
			Equipe equipe = equipeService.salvarEquipe(entity.equipe);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}
		return Response.ok(UriBuilder.fromResource(EquipeEndpoint.class).path(String.valueOf(entity.getId())).build())
				.build();
	}
}

class EquipeWrapper {
	Equipe equipe;

	EquipeWrapper(Equipe e) {
		this.equipe = e;
	}

	EquipeWrapper() {
		this.equipe = new Equipe();
	}

	public void setId(Long id) {
		this.equipe.setId(id);
	}

	public Long getId() {
		return equipe.getId();
	}

	public void setNome(String id) {
		this.equipe.setNome(id);
	}

	public String getNome() {
		return equipe.getNome();
	}

	public Set<Usuario> getListaEquipeUsuario() {
		return equipe.getListaEquipeUsuario();
	}

	public void setListaEquipeUsuario(Set<Usuario> id) {
		this.equipe.setListaEquipeUsuario(id);
	}

}