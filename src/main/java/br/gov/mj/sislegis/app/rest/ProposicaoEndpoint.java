package br.gov.mj.sislegis.app.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

import org.jboss.resteasy.annotations.cache.Cache;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.model.ProcessoSei;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.Votacao;
import br.gov.mj.sislegis.app.model.documentos.Briefing;
import br.gov.mj.sislegis.app.model.documentos.DocRelated;
import br.gov.mj.sislegis.app.model.documentos.Emenda;
import br.gov.mj.sislegis.app.model.documentos.NotaTecnica;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.rest.authentication.UsuarioAutenticadoBean;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.ReuniaoService;

/**
 * 
 */

@Path("/proposicaos")
public class ProposicaoEndpoint {

	@Inject
	private ProposicaoService proposicaoService;

	@Inject
	private AreaDeMeritoService areaMeritoRevisao;

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
	@Path("/salvarProposicoesGenericas")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarProposicoesGenericas(ProposicaoPautadaPautaWrapper propPautadas) {
		try {
			List<Proposicao> responses = new ArrayList<>();
			Iterator<PautaReuniaoComissao> prs = propPautadas.getPautas().iterator();
			for (Iterator iterator = propPautadas.getProposicoes().iterator(); iterator.hasNext();) {
				Proposicao proposicao = (Proposicao) iterator.next();
				PautaReuniaoComissao prc = prs.next();
				responses.add(proposicaoService.persistProposicaoAndPauta(proposicao, prc));

			}
			return Response.ok(responses, MediaType.APPLICATION_JSON).build();
		} catch (EJBTransactionRolledbackException e) {
			return Response.status(Response.Status.CONFLICT).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@POST
	@Path("/salvarProposicoesExtras")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarProposicoesExtras(List<Proposicao> proposicoes) {
		try {
			Map<Integer, Proposicao> responses = new HashMap<>();
			for (Iterator iterator = proposicoes.iterator(); iterator.hasNext();) {
				Proposicao proposicao = (Proposicao) iterator.next();

				int result = proposicaoService.salvarProposicaoIndependente(proposicao);
				responses.put(result, proposicao);
			}
			return Response.ok(responses, MediaType.APPLICATION_JSON).build();
		} catch (EJBTransactionRolledbackException e) {
			return Response.status(Response.Status.CONFLICT).build();
		}

	}

	@POST
	@Path("/salvarProposicaoExtra")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response salvarProposicaoExtra(Proposicao proposicao) {
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

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Proposicao entity, @HeaderParam("Authorization") String authorization) {

		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.save(entity, user);
			return Response.ok(
					UriBuilder.fromResource(ProposicaoEndpoint.class).path(String.valueOf(entity.getId())).build())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Proposicao entity, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.save(entity, user);
			return Response.created(
					UriBuilder.fromResource(ProposicaoEndpoint.class).path(String.valueOf(entity.getId())).build())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

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
	public Response findById(@PathParam("id") Integer id, @QueryParam("fetchAll") Boolean fetchAll) {
		return Response.ok(proposicaoService.buscarPorId(id, (fetchAll != null && fetchAll))).build();
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
			@QueryParam("estado") String estado, @QueryParam("isFavorita") String isFavorita,
			@QueryParam("idEquipe") Long idEquipe, @QueryParam("limit") Integer limit,
			@QueryParam("macrotema") String macrotema, @QueryParam("offset") Integer offset) {

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("sigla", sigla);
		m.put("ementa", ementa);
		m.put("autor", autor);
		m.put("origem", origem);
		m.put("isFavorita", isFavorita);
		m.put("estado", estado);
		m.put("macrotema", macrotema);
		m.put("idEquipe", idEquipe);

		List<Proposicao> results = proposicaoService.consultar(m, offset, limit);
		return results;
	}

	@GET
	@Path("/buscarPorSufixo")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Proposicao> buscarPorSufixo(@QueryParam("sufixo") String sufixo) {
		return proposicaoService.buscarPorSufixo(sufixo);
	}

	@GET
	@Path("/buscaIndependente/{origem:[A-Z]*}/{tipo:[A-Z\\.]*}/{ano:[0-9]{4}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Proposicao> buscaIndependente(@PathParam("origem") String origem, @PathParam("tipo") String tipo,
			@QueryParam("numero") String numero, @PathParam("ano") Integer ano) throws Exception {

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
			PosicionamentoProposicao pp = proposicaoService.alterarPosicionamento(
					posicionamentoProposicaoWrapper.getId(), posicionamentoProposicaoWrapper.getIdPosicionamento(),
					posicionamentoProposicaoWrapper.preliminar, usuarioLogado);

			return Response.ok(pp).build();

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
	@Path("/{id:[0-9]+}/revisaoMerito")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AreaDeMeritoRevisao> listRevisoes(@PathParam("id") Long id) throws Exception {
		return areaMeritoRevisao.listRevisoesProposicao(id);
	}

	@POST
	@Path("/{id:[0-9]+}/revisaoMerito")
	@Produces(MediaType.APPLICATION_JSON)
	public AreaDeMeritoRevisao saveRevisao(@PathParam("id") Long id, AreaDeMeritoRevisao entity) throws Exception {
		entity.setProposicao(proposicaoService.findById(id));

		return areaMeritoRevisao.saveRevisao(entity);
	}

	@GET
	@Path("/{id:[0-9]+}/notatecnica")
	@Produces(MediaType.APPLICATION_JSON)
	public List<NotaTecnica> listNotaTecnicas(@PathParam("id") Long id) throws Exception {
		return proposicaoService.getNotaTecnicas(id);
	}

	@DELETE
	@Path("/{id:[0-9]+}/docrelated/{type:[0-9]+}/{docId:[0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeDocRelated(@PathParam("id") Long id, @PathParam("docId") Long docId,
			@PathParam("type") Integer type, @HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);

			switch (type) {
			case 1:
				proposicaoService.deleteDocRelated(docId, NotaTecnica.class);
				break;
			case 2:
				proposicaoService.deleteDocRelated(docId, Briefing.class);
				break;
			case 3:
				proposicaoService.deleteDocRelated(docId, Emenda.class);
				break;
			default:
				throw new IllegalArgumentException("tipo invalido");
			}
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{id:[0-9]+}/docrelated/{type:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public List listDocRelated(@PathParam("id") Long id, @PathParam("type") Integer type) throws Exception {
		switch (type) {
		case 1:
			return proposicaoService.getNotaTecnicas(id);
		case 2:
			return proposicaoService.getBriefings(id);
		case 3:
			return proposicaoService.getEmendas(id);

		default:
			break;
		}
		return proposicaoService.getNotaTecnicas(id);
	}

	@POST
	@Path("/{id:[0-9]+}/notatecnica")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNotaTecnica(@PathParam("id") Long id, NotaTecnica nt,
			@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			nt.setUsuario(user);
			nt.setProposicao(proposicaoService.findById(id));

			proposicaoService.saveNotaTecnica(nt);
			return Response.ok(nt).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/{id:[0-9]+}/notatecnica/{idNota:[0-9]+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNotaTecnica(@PathParam("id") Long id, @PathParam("idNota") Long idNota,
			@HeaderParam("Authorization") String authorization) {
		try {
			Usuario user = controleUsuarioAutenticado.carregaUsuarioAutenticado(authorization);
			proposicaoService.deleteDocRelated(idNota, NotaTecnica.class);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
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
	public Response setRoadmapComissoes(RoadmapComissoesWrapper roadmapComissoesWrapper) {
		try {
			proposicaoService.setRoadmapComissoes(roadmapComissoesWrapper.getIdProposicao(),
					roadmapComissoesWrapper.getComissoes());
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
	public ProcessoSei inserirProcessoSei(ProcessoSeiWrapper processoSeiWrapper) {
		try {
			ProcessoSei processoSei = proposicaoService.vincularProcessoSei(processoSeiWrapper.getId(),
					processoSeiWrapper.getProtocolo());
			return processoSei;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DELETE
	@Path("/excluirProcessoSei/{idProcesso:[0-9]+}")
	public Response excluirProcessoSei(@PathParam("idProcesso") Long idProcesso) {
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
	public List<Votacao> listarVotacoes(@QueryParam("idProposicao") String idProposicao,
			@QueryParam("tipo") String tipo, @QueryParam("numero") String numero, @QueryParam("ano") String ano,
			@QueryParam("origem") String origem) {

		try {
			Integer idProp = (idProposicao == null || "".equals(idProposicao)) ? null : Integer.valueOf(idProposicao);
			List<Votacao> votacoes = proposicaoService
					.listarVotacoes(idProp, tipo, numero, ano, Origem.valueOf(origem));
			return votacoes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}

class ProposicaoPautadaPautaWrapper {
	List<Proposicao> proposicoes;
	List<PautaReuniaoComissao> pautas;

	public List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	public void setProposicoes(List<Proposicao> proposicoes) {
		this.proposicoes = proposicoes;
	}

	public List<PautaReuniaoComissao> getPautas() {
		return pautas;
	}

	public void setPautas(List<PautaReuniaoComissao> pautas) {
		this.pautas = pautas;
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

class RoadmapComissoesWrapper {
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
