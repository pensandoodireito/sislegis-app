package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.model.ProcessoSei;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.Votacao;
import org.jboss.resteasy.annotations.cache.Cache;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoService;

/**
 * 
 */

@Path("/proposicaos")
public class ProposicaoEndpoint {

	@Inject
	private ProposicaoService proposicaoService;

	@GET
	@Path("/proposicoesPautaCamara")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<PautaReuniaoComissao> buscarProposicoesPautaCamara(@QueryParam("idComissao") Long idComissao,
			@QueryParam("data") Date data) throws Exception {

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idComissao", idComissao);
		parametros.put("data", data);

		Set<PautaReuniaoComissao> lista = proposicaoService.buscarProposicoesPautaCamaraWS(parametros);

		return lista;
	}

	@GET
	@Path("/proposicoesPautaSenado")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<PautaReuniaoComissao> buscarProposicoesPautaSenado(@QueryParam("siglaComissao") String siglaComissao,
			@QueryParam("data") Date data) throws Exception {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("siglaComissao", siglaComissao);
		parametros.put("data", data);

		return proposicaoService.buscarProposicoesPautaSenadoWS(parametros);

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
		// nao é usada mais
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	@Inject
	private ReuniaoService serviceReuniao;

	@POST
	@Path("/salvarProposicoesDePauta")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adicionaProposicoesDePautaEmReuniao(AddProposicaoPautaWrapper proposicaoPautaWrapper) {
		try {
			Date reuniaoDate = new Date(proposicaoPautaWrapper.getReuniaoDate());
			Reuniao reuniao = null;
			try {
				reuniao = serviceReuniao.buscaReuniaoPorData(reuniaoDate);
			} catch (Exception e) {
				System.err.println("E " + e.getMessage());

			}
			if (reuniao == null) {
				reuniao = new Reuniao();
				reuniao.setData(reuniaoDate);
			}
			proposicaoService.adicionaProposicoesReuniao(proposicaoPautaWrapper.getPautaReunioes(), reuniao);

			return Response.noContent().build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}

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
		proposicaoService.save(entity);
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
	public Response findById(@PathParam("id") Integer id,@QueryParam("fetchAll") Boolean fetchAll) {
		return Response.ok(proposicaoService.buscarPorId(id,(fetchAll != null && fetchAll))).build();
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
			@QueryParam("sigla") String sigla, @QueryParam("origem") String origem,
			@QueryParam("isFavorita") String isFavorita, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset) {

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
	@Path("/buscaIndependente/{origem:[A-Z]*}/{tipo:[A-Z\\.]*}/{ano:[0-9]{4}}")
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

	@POST
	@Path("/check4updates/{id:[0-9]+}")
	public Response syncpauta(@PathParam("id") Long id) {
		try {

			if (proposicaoService.syncDadosPautaProposicao(id) || proposicaoService.syncDadosProposicao(id)) {
				return Response.status(Status.ACCEPTED).entity(proposicaoService.findById(id)).build();
			} else {
				return Response.status(Status.NOT_MODIFIED).build();
			}

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

	@POST
	@Path("/alterarPosicionamento")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarPosicionamento(PosicionamentoProposicaoWrapper posicionamentoProposicaoWrapper,
			@HeaderParam("Authorization") String authorization) {
		try {
			Usuario usuarioLogado = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.alterarPosicionamento(posicionamentoProposicaoWrapper.getId(),
					posicionamentoProposicaoWrapper.getIdPosicionamento(), posicionamentoProposicaoWrapper.preliminar, usuarioLogado);
			return Response.ok().build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/historicoPosicionamentos/{id:[0-9]+}")
	public List<PosicionamentoProposicao> historicoPosicionamentos(@PathParam("id") Long id) {
		return proposicaoService.listarHistoricoPosicionamentos(id);
	}


	@GET
	@Path("/{id:[0-9]+}/pautas")
	@Cache(maxAge = 24, noStore = false, isPrivate = false, sMaxAge = 24)
	@Produces(MediaType.APPLICATION_JSON)
	public SortedSet<ProposicaoPautaComissao> listPautasProposicao(@PathParam("id") Long id) throws Exception {
		return proposicaoService.findById(id).getPautasComissoes();
	}

	@POST
	@Path("/setRoadmapComissoes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setRoadmapComissoes(RoadmapComissoesWrapper roadmapComissoesWrapper){
		try {
			proposicaoService.setRoadmapComissoes(roadmapComissoesWrapper.getIdProposicao(), roadmapComissoesWrapper.getComissoes());
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/vincularProcessoSei")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ProcessoSei inserirProcessoSei(ProcessoSeiWrapper processoSeiWrapper){
		try {
			ProcessoSei processoSei = proposicaoService.vincularProcessoSei(processoSeiWrapper.getId(), processoSeiWrapper.getProtocolo());
			return processoSei;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DELETE
	@Path("/excluirProcessoSei/{idProcesso:[0-9]+}")
	public Response excluirProcessoSei(@PathParam("idProcesso") Long idProcesso){
		try {
			proposicaoService.excluirProcessoSei(idProcesso);
			return Response.noContent().build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/listarVotacoes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Votacao> listarVotacoes(@QueryParam("idProposicao") String idProposicao, @QueryParam("tipo") String tipo, @QueryParam("numero") String numero, @QueryParam("ano") String ano, @QueryParam("origem") String origem){

		try {
			Integer idProp = (idProposicao == null || "".equals(idProposicao)) ? null : Integer.valueOf(idProposicao);
			List<Votacao> votacoes = proposicaoService.listarVotacoes(idProp, tipo, numero, ano, Origem.valueOf(origem));
			return votacoes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}

class AddProposicaoPautaWrapper {
	Set<PautaReuniaoComissao> pautaReunioes;
	long reuniaoDate;

	public Set<PautaReuniaoComissao> getPautaReunioes() {
		return pautaReunioes;
	}

	public void setPautaReunioes(Set<PautaReuniaoComissao> pautaReunioes) {
		this.pautaReunioes = pautaReunioes;
	}

	public long getReuniaoDate() {
		return reuniaoDate;
	}

	public void setReuniaoDate(long reuniaoDate) {
		this.reuniaoDate = reuniaoDate;
	}

}

class PosicionamentoProposicaoWrapper {
	Long id;
	Long idPosicionamento;
	boolean preliminar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPosicionamento() {
		return idPosicionamento;
	}

	public void setIdPosicionamento(Long idPosicionamento) {
		this.idPosicionamento = idPosicionamento;
	}

	public boolean isPreliminar() {
		return preliminar;
	}

	public void setPreliminar(boolean preliminar) {
		this.preliminar = preliminar;
	}
}

class RoadmapComissoesWrapper{
	private Long idProposicao;
	private List<String> comissoes;

	public Long getIdProposicao() {
		return idProposicao;
	}

	public void setIdProposicao(Long idProposicao) {
		this.idProposicao = idProposicao;
	}

	public List<String> getComissoes() {
		return comissoes;
	}

	public void setComissoes(List<String> comissoes) {
		this.comissoes = comissoes;
	}
}

class ProcessoSeiWrapper {
	private Long id;
	private String protocolo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
}
