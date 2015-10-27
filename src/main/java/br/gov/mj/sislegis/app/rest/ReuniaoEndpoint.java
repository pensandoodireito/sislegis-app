package br.gov.mj.sislegis.app.rest;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
import javax.ws.rs.core.UriBuilder;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.rest.serializers.MonthDayYearDeSerializer;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Path("/reuniaos")
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

	@PUT
	@Path("/addProposicao")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProposicao(CompactReuniao reuniaoCompacta) {
		System.out.println("d " + reuniaoCompacta);
		// Reuniao reuniao = service.findById(idReuniao);
		// try {
		// proposicao = proposicaoService
		// .buscaProposicaoIndependentePor(proposicao.getOrigem(),
		// proposicao.getTipo(),
		// Integer.valueOf(proposicao.getNumero()),
		// Integer.valueOf(proposicao.getAno())).iterator()
		// .next();
		// reuniao.addProposicao(proposicao);
		// service.save(reuniao);
		// return Response.created(
		// UriBuilder.fromResource(ReuniaoEndpoint.class).path(String.valueOf(reuniao.getId())).build())
		// .build();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	@GET
	@Path("/findByData")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Proposicao> findByData(@QueryParam("data") Date data) throws Exception {
		Collection<Proposicao> lista = proposicaoService.buscarProposicoesPorDataReuniao(data);
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

@JsonIgnoreProperties(ignoreUnknown = true)
class CompactReuniao {

	Long id;
	@JsonDeserialize(using = MonthDayYearDeSerializer.class)
	Date data;
	List<CompactProposicao> proposicoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<CompactProposicao> getProposicoes() {
		return proposicoes;
	}

	public void setProposicoes(List<CompactProposicao> proposicoes) {
		this.proposicoes = proposicoes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("id:{" + id + "} data:{" + data + "}\n");
		for (Iterator iterator = proposicoes.iterator(); iterator.hasNext();) {
			CompactProposicao compactProposicao = (CompactProposicao) iterator.next();
			sb.append(compactProposicao).append("\n");
		}
		return sb.toString();
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CompactProposicao {

	Long id;
	Integer idProposicao;
	Origem origem;
	String comissao;
	Integer codigoReuniao;

	public Integer getIdProposicao() {
		return idProposicao;
	}

	public void setIdProposicao(Integer idProposicao) {
		this.idProposicao = idProposicao;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public String getComissao() {
		return comissao;
	}

	public void setComissao(String comissao) {
		this.comissao = comissao;
	}

	public Integer getCodigoReuniao() {
		return codigoReuniao;
	}

	public void setCodigoReuniao(Integer codigoReuniao) {
		this.codigoReuniao = codigoReuniao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {

		return "id:{" + idProposicao + "} Origem:{" + origem.name() + "} comissao:{" + comissao + "} codigoReuniao:{"
				+ codigoReuniao + "}";
	}
}