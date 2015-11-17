package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;

import br.gov.mj.sislegis.app.model.Notificacao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.NotificacaoService;

@Path("/notificacao")
@GZIP
public class NotificacaoEndpoint {

	@Inject
	private NotificacaoService notificacaoService;

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@GET
	@Path("/usuario")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Notificacao> buscarPorUsuario(@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			return notificacaoService.listaNotificacoesParaUsuario(user);

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/usuario/{categoria:[A-Z]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Notificacao> buscarPorUsuarioCategoria(@HeaderParam("Authorization") String authorization,
			@PathParam("categoria") String categoria) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			return notificacaoService.listaNotificacoesParaUsuario(user, categoria);

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/marcarVisualizadas")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response marcarComoVisualizadas(Collection<Long> idNotificacoes) {
		notificacaoService.marcarComoVisualizada(idNotificacoes);
		return Response.noContent().build();
	}

}