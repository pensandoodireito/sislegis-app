package br.gov.mj.sislegis.app.rest;

import java.util.ArrayList;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.json.DropdownMultiselectStringJSON;
import br.gov.mj.sislegis.app.model.Tag;
import br.gov.mj.sislegis.app.service.TagService;

@Path("/tags")
public class TagEndpoint {

	@Inject
	private TagService tagService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tag entity) {

		tagService.save(entity);
		return Response
				.created(UriBuilder.fromResource(TagEndpoint.class).path(String.valueOf(entity.getId())).build())
				.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		tagService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") String id) {
		Tag entity = tagService.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") String id) {
		
		tagService.deleteById(id);
		return Response.ok().build();
	}

	@GET
	@Path("/listarTodos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> listAll() {
		return tagService.listarTodasTags();
	}

	@GET
	@Path("/buscarPorSufixo")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> buscarPorSufixo(@QueryParam("sufixo") String sufixo) {
		return tagService.buscaPorSufixo(sufixo);
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Tag entity) {
		try {
			
			
			
			tagService.replace(id,entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

	@GET
	@Path("/listAllDropdownMultiple")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DropdownMultiselectStringJSON> listAllDropdownMultiple() {
		final List<Tag> results = tagService.listarTodasTags();
		List<DropdownMultiselectStringJSON> listaDropdownMultiselectJSON = new ArrayList<DropdownMultiselectStringJSON>();
		for (Tag tag : results) {
			DropdownMultiselectStringJSON dropdownMultiselectJSON = new DropdownMultiselectStringJSON();
			dropdownMultiselectJSON.setLabel(tag.getTag());
			dropdownMultiselectJSON.setId(tag.getTag());
			listaDropdownMultiselectJSON.add(dropdownMultiselectJSON);
		}
		return listaDropdownMultiselectJSON;
	}

}
