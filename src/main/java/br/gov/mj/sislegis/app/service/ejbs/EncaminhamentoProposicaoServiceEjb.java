package br.gov.mj.sislegis.app.service.ejbs;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.enumerated.TipoTarefa;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.EstadoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.TarefaService;
import br.gov.mj.sislegis.app.service.TipoEncaminhamentoService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class EncaminhamentoProposicaoServiceEjb extends AbstractPersistence<EncaminhamentoProposicao, Long> implements EncaminhamentoProposicaoService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TarefaService tarefaService;
	@Inject
	private TipoEncaminhamentoService tipoEncSvc;
	@Inject
	private ProposicaoService propSvc;

	public EncaminhamentoProposicaoServiceEjb() {
		super(EncaminhamentoProposicao.class);
	}

	@Override
	public void deleteById(Long id) {
		int deleted = em.createQuery("delete Tarefa where encaminhamentoProposicao.id=:id").setParameter("id", id).executeUpdate();
		super.deleteById(id);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EncaminhamentoProposicao salvarEncaminhamentoProposicao(EncaminhamentoProposicao encaminhamentoProposicao) {
		EncaminhamentoProposicao savedEntity = this.save(encaminhamentoProposicao);
		tarefaService.updateTarefa(savedEntity);
		return savedEntity;
	}

	@Override
	public EncaminhamentoProposicao salvarEncaminhamentoProposicaoAutomatico(String detalhe, Proposicao p, Usuario responsavel) {

		TipoEncaminhamento tipo = tipoEncSvc.buscarTipoEncaminhamentoAlteracaoProposicao();
		List<EncaminhamentoProposicao> encs = em.createNamedQuery("getAllEncaminhamentoProposicao", EncaminhamentoProposicao.class).setParameter("proposicao", p).setParameter("responsavel", responsavel).setParameter("tipo", tipo).getResultList();
		for (Iterator iterator = encs.iterator(); iterator.hasNext();) {
			EncaminhamentoProposicao encaminhamentoProposicao = (EncaminhamentoProposicao) iterator.next();
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Apagando Encaminhamento " + encaminhamentoProposicao.getId() + " para " + encaminhamentoProposicao.getResponsavel() + " " + encaminhamentoProposicao.getProposicao().getSigla());
			List<Tarefa> t = em.createNamedQuery("getAllTarefa", Tarefa.class).setParameter("user", responsavel).setParameter("enc", encaminhamentoProposicao).setParameter("tipo", TipoTarefa.ENCAMINHAMENTO).getResultList();
			for (Iterator iterator2 = t.iterator(); iterator2.hasNext();) {
				Tarefa tarefa = (Tarefa) iterator2.next();
				int deleted = em.createQuery("delete Notificacao t where  t.identificacaoEntidade = :identificacaoEntidade and t.categoria=:categoria").setParameter("identificacaoEntidade", tarefa.getId().toString()).setParameter("categoria", "TAREFAS").executeUpdate();
			}
			deleteById(encaminhamentoProposicao.getId());
		}
		EncaminhamentoProposicao eprop = new EncaminhamentoProposicao();
		eprop.setDetalhes(detalhe);
		eprop.setTipoEncaminhamento(tipo);
		eprop.setProposicao(p);
		eprop.setResponsavel(responsavel);
		EncaminhamentoProposicao savedEntity = salvarEncaminhamentoProposicao(eprop);
		return savedEntity;
	}

	@Override
	public List<EncaminhamentoProposicao> findByProposicao(Long idProposicao) {
		TypedQuery<EncaminhamentoProposicao> findByIdQuery = em.createQuery("SELECT c FROM EncaminhamentoProposicao c where c.proposicao.id=:entityId", EncaminhamentoProposicao.class);
		findByIdQuery.setParameter("entityId", idProposicao);
		final List<EncaminhamentoProposicao> results = findByIdQuery.getResultList();

		return results;
	}

	@Override
	public Integer totalByProposicao(Long idProposicao) {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM encaminhamentoproposicao WHERE proposicao_id = :idProposicao");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

	@Override
	public void finalizar(Long idEncaminhamentoProposicao, String descricaoComentario, Usuario autor) {
		EncaminhamentoProposicao encaminhamento = findById(idEncaminhamentoProposicao);

		encaminhamento.setFinalizado(true);
		Comentario comentarioFinalizaca = null;
		if (descricaoComentario != null && !descricaoComentario.isEmpty()) {

			comentarioFinalizaca = new Comentario();
			comentarioFinalizaca.setAutor(autor);
			comentarioFinalizaca.setDataCriacao(new Date());
			comentarioFinalizaca.setDescricao(descricaoComentario);
			comentarioFinalizaca.setProposicao(encaminhamento.getProposicao());

			encaminhamento.setComentarioFinalizacao(comentarioFinalizaca);
		}

		Tarefa tarefa = tarefaService.buscarPorEncaminhamentoProposicaoId(idEncaminhamentoProposicao);
		if (tarefa != null) {
			tarefa.setFinalizada(true);
			if (comentarioFinalizaca != null) {
				tarefa.setComentarioFinalizacao(comentarioFinalizaca);
			}
			tarefaService.save(tarefa); // tarefa salva tambem o encaminhamento
										// (cascade)
		} else {
			save(encaminhamento);
		}
		if (encaminhamento.getTipoEncaminhamento().equals(tipoEncSvc.buscarTipoEncaminhamentoDespachoPresencial())) {
			if (EstadoProposicao.ADESPACHAR_PRESENCA.equals(encaminhamento.getProposicao().getEstado())) {
				Proposicao prop = propSvc.findById(encaminhamento.getProposicao().getId());
				prop.setEstado(EstadoProposicao.DESPACHADA);
				propSvc.save(prop, null);
			}
		}
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		this.tarefaService = (TarefaService) injections[1];

	}

	@Override
	public EncaminhamentoProposicao getByComentarioFinalizacao(Long id) {
		try {
			return em.createNamedQuery("getEnc4Comentario", EncaminhamentoProposicao.class).setParameter("comentarioId", id).getSingleResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
