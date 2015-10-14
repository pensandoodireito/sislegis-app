package br.gov.mj.sislegis.app.rest;

import br.gov.mj.sislegis.app.model.Andamento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.AndamentoService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.List;

@Path("/andamentos")
public class AndamentoEndpoint {

    @Inject
    private AndamentoService andamentoService;

    @Inject
    private UsuarioAutenticadoBean controleUsuarioAutenticado;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Andamento andamento, @HeaderParam("Authorization") String authorization) {
        try {
            Usuario usuario = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
            andamento.setUsuario(usuario);
            andamentoService.save(andamento);

        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.created(UriBuilder.fromResource(AndamentoEndpoint.class).path(String.valueOf(andamento.getId())).build()).build();
    }

    @GET
    @Path("/{idProposicao:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Andamento> findByIdProposicao(@PathParam("idProposicao") Long idProposicao) {
        List<Andamento> andamentos = andamentoService.findByIdProposicao(idProposicao);
        return andamentos;
    }

}
