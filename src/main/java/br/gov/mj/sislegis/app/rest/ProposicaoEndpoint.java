package br.gov.mj.sislegis.app.rest;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.annotations.cache.Cache;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.Service;

/**
 * 
 */

@Path("/proposicaos")
public class ProposicaoEndpoint {

	@Inject
	private ProposicaoService proposicaoService;

	@Inject
	private Service<Proposicao> service;

	@GET
	@Path("/proposicoesPautaCamara")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> buscarProposicoesPautaCamara(@QueryParam("idComissao") Long idComissao,
			@QueryParam("data") Date data) throws Exception {

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idComissao", idComissao);
		parametros.put("data", data);

		List<Proposicao> lista = proposicaoService.buscarProposicoesPautaCamaraWS(parametros);

		return lista;
	}

	@GET
	@Path("/proposicoesPautaSenado")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> buscarProposicoesPautaSenado(@QueryParam("siglaComissao") String siglaComissao,
			@QueryParam("data") Date data) throws Exception {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("siglaComissao", siglaComissao);
		parametros.put("data", data);

		List<Proposicao> lista = proposicaoService.buscarProposicoesPautaSenadoWS(parametros);

		return lista;
	}

	@GET
	@Path("/detalharProposicaoCamaraWS")
	@Produces(MediaType.APPLICATION_JSON)
	public Proposicao detalharProposicaoCamaraWS(@QueryParam("id") Long id) throws Exception {
		return proposicaoService.detalharProposicaoCamaraWS(id);
	}

	@GET
	@Path("/detalharProposicaoSenadoWS")
	@Produces(MediaType.APPLICATION_JSON)
	public Proposicao detalharProposicaoSenadoWS(@QueryParam("id") Long id) throws Exception {
		return proposicaoService.detalharProposicaoSenadoWS(id);
	}

	@POST
	@Path("/salvarProposicoes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarProposicoes(List<Proposicao> listaProposicoesSelecionados) {
		try {
			proposicaoService.salvarListaProposicao(listaProposicoesSelecionados);
		} catch (EJBTransactionRolledbackException e) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		return Response.noContent().build();

	}

	@POST
	@Path("/salvarProposicaoExtra")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarProposicoes(Proposicao proposicao) {
		try {
			int result = proposicaoService.salvarProposicaoIndependente(proposicao);
			switch (result) {
			case 0:
				return Response.status(Response.Status.OK).build();
			case 1:
				return Response.status(Response.Status.CREATED).build();
			case -1:
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}

		} catch (EJBTransactionRolledbackException e) {
			return Response.status(Response.Status.CONFLICT).build();
		}
		return Response.noContent().build();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Proposicao entity) {
		service.save(entity);
		return Response.created(
				UriBuilder.fromResource(ProposicaoEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		proposicaoService.deleteById(id);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}/{reuniaoId:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id, @PathParam("reuniaoId") Long reuniaoId) {
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Integer id) {
		return Response.ok(proposicaoService.buscarPorId(id)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> listAll() {
		return proposicaoService.listarTodos();
	}

	@GET
	@Path("/consultar")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> consultar(@QueryParam("ementa") String ementa, @QueryParam("autor") String autor,
			@QueryParam("sigla") String sigla, @QueryParam("origem") String origem,	@QueryParam("isFavorita") String isFavorita,
			@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset) {

		List<Proposicao> results = proposicaoService.consultar(sigla, autor, ementa, origem, isFavorita, offset, limit);
		return results;
	}

	@GET
	@Path("/buscarPorSufixo")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> buscarPorSufixo(@QueryParam("sufixo") String sufixo) {
		return proposicaoService.buscarPorSufixo(sufixo);
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Proposicao entity) {
		proposicaoService.save(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/buscaIndependente/{origem:[A-Z]*}/{tipo:[A-Z]*}/{ano:[0-9]{4}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Proposicao> buscaIndependente(@PathParam("origem") String origem, @PathParam("tipo") String tipo,
			@QueryParam("numero") Integer numero, @PathParam("ano") Integer ano) throws Exception {

		return proposicaoService.buscaProposicaoIndependentePor(Origem.valueOf(origem), tipo, numero, ano);

	}

	@GET
	@Path("/listTipos/CAMARA")
	@Cache(maxAge = 24, noStore = false, isPrivate = false, sMaxAge = 24)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<TipoProposicao> listTiposCamara() throws Exception {

		return proposicaoService.listTipos(Origem.CAMARA);

	}

	@GET
	@Path("/listTipos/SENADO")
	@Cache(maxAge = 24, noStore = false, isPrivate = false, sMaxAge = 24)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<TipoProposicao> listTiposSenado() throws Exception {

		return proposicaoService.listTipos(Origem.SENADO);

	}

	@Inject
	private UsuarioAutenticadoBean controleUsuarioAutenticado;

	@POST
	@Path("/follow/{id:[0-9]+}")
	public Response follow(@PathParam("id") Long id, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.followProposicao(user, id);

			return Response.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@DELETE
	@Path("/follow/{id:[0-9]+}")
	public Response unfollow(@PathParam("id") Long id, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.unfollowProposicao(user, id);
			return Response.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

}
