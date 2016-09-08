package br.gov.mj.sislegis.app.service.ejbs;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.TarefaService;

@Stateless
public class EncaminhamentoProposicaoServiceEjb extends AbstractPersistence<EncaminhamentoProposicao, Long> implements
		EncaminhamentoProposicaoService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TarefaService tarefaService;

	public EncaminhamentoProposicaoServiceEjb() {
		super(EncaminhamentoProposicao.class);
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
	public List<EncaminhamentoProposicao> findByProposicao(Long idProposicao) {
		TypedQuery<EncaminhamentoProposicao> findByIdQuery = em.createQuery(
				"SELECT c FROM EncaminhamentoProposicao c where c.proposicao.id=:entityId",
				EncaminhamentoProposicao.class);
		findByIdQuery.setParameter("entityId", idProposicao);
		final List<EncaminhamentoProposicao> results = findByIdQuery.getResultList();

		return results;
	}

	// Por algum motivo esse metodo não está usando JPA, e está fazendo join na
	// mao...

	@Deprecated
	public List<EncaminhamentoProposicao> findByProposicao2(Long idProposicao) {
		TypedQuery<EncaminhamentoProposicao> findByIdQuery = em.createQuery("SELECT c FROM EncaminhamentoProposicao c "
				+ "INNER JOIN FETCH c.responsavel res " + "INNER JOIN FETCH c.comentario com "
				+ "INNER JOIN FETCH c.tipoEncaminhamento enc "
				+ "INNER JOIN FETCH c.proposicao p WHERE p.id = :entityId", EncaminhamentoProposicao.class);
		findByIdQuery.setParameter("entityId", idProposicao);
		final List<EncaminhamentoProposicao> results = findByIdQuery.getResultList();

		return results;
	}

	@Override
	public Integer totalByProposicao(Long idProposicao) {
		Query query = em
				.createNativeQuery("SELECT COUNT(1) FROM encaminhamentoproposicao WHERE proposicao_id = :idProposicao");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

	@Override
	public void finalizar(Long idEncaminhamentoProposicao, String descricaoComentario) {
		EncaminhamentoProposicao encaminhamento = findById(idEncaminhamentoProposicao);
		encaminhamento.setFinalizado(true);

		Comentario comentario = new Comentario();
		comentario.setAutor(encaminhamento.getResponsavel());
		comentario.setDataCriacao(new Date());
		comentario.setDescricao(descricaoComentario);
		comentario.setProposicao(encaminhamento.getProposicao());

		encaminhamento.setComentarioFinalizacao(comentario);

		Tarefa tarefa = tarefaService.buscarPorEncaminhamentoProposicaoId(idEncaminhamentoProposicao);
		if (tarefa != null) {
			tarefa.setFinalizada(true);
			tarefa.setComentarioFinalizacao(comentario);
			tarefaService.save(tarefa); // tarefa salva tambem o encaminhamento
										// (cascade)
		} else {
			save(encaminhamento);
		}
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		this.tarefaService = (TarefaService) injections[1];

	}

}
