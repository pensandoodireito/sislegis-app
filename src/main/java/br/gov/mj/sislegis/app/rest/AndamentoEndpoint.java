package br.gov.mj.sislegis.app.rest;

import br.gov.mj.sislegis.app.model.Andamento;
import br.gov.mj.sislegis.app.service.AndamentoService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/andamentos")
public class AndamentoEndpoint {

    @Inject
    private AndamentoService andamentoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Andamento andamento) {
        andamentoService.save(andamento);
        return Response.created(UriBuilder.fromResource(AndamentoEndpoint.class).path(String.valueOf(andamento.getId())).build()).build();
    }

    @GET
    @Path("/{idProposicao:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Andamento> findByIdProposicao(Long idProposicao) {
        List<Andamento> andamentos = andamentoService.findByIdProposicao(idProposicao);
        return andamentos;
    }

}
