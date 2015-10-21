package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.enumerated.TipoTarefa;
import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComentarioService;
import br.gov.mj.sislegis.app.service.EncaminhamentoProposicaoService;
import br.gov.mj.sislegis.app.service.TarefaService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Stateless
public class EncaminhamentoProposicaoServiceEjb extends AbstractPersistence<EncaminhamentoProposicao, Long> implements
		EncaminhamentoProposicaoService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TarefaService tarefaService;

	@Inject
	private ComentarioService comentarioService;

	public EncaminhamentoProposicaoServiceEjb() {
		super(EncaminhamentoProposicao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public EncaminhamentoProposicao salvarEncaminhamentoProposicao(EncaminhamentoProposicao encaminhamentoProposicao,
			String referer) {
		EncaminhamentoProposicao savedEntity = this.save(encaminhamentoProposicao);

		criarTarefa(referer, savedEntity);

		return savedEntity;
	}

	private void criarTarefa(String referer, EncaminhamentoProposicao savedEntity) {
		// Caso uma tarefa já exista, significa que foi atualizada. Excluímos a
		// antiga antes de atualizar.
		Tarefa tarefaPorEncaminhamentoProposicaoId = tarefaService.buscarPorEncaminhamentoProposicaoId(savedEntity
				.getId());
		if (tarefaPorEncaminhamentoProposicaoId != null) {
			tarefaService.deleteById(tarefaPorEncaminhamentoProposicaoId.getId());
		}

		// Criamos a nova tarefa
		Tarefa tarefa = new Tarefa();
		tarefa.setTipoTarefa(TipoTarefa.ENCAMINHAMENTO);
		tarefa.setData(new Date());
		tarefa.setUsuario(savedEntity.getResponsavel());
		tarefa.setEncaminhamentoProposicao(savedEntity);

		tarefaService.save(tarefa, referer);
	}


	public List<EncaminhamentoProposicao> findByProposicao(Long idProposicao) {
		TypedQuery<EncaminhamentoProposicao> findByIdQuery = em
				.createQuery(
						"SELECT c FROM EncaminhamentoProposicao c "
								+ "INNER JOIN FETCH c.responsavel res "
								+ "INNER JOIN FETCH c.comentario com "
								+ "INNER JOIN FETCH c.encaminhamento enc "
								+ "INNER JOIN FETCH c.proposicao p WHERE p.id = :entityId",
								EncaminhamentoProposicao.class);
		findByIdQuery.setParameter("entityId", idProposicao);
		final List<EncaminhamentoProposicao> results = findByIdQuery.getResultList();

		return  results;
	}

	@Override
	public Integer totalByProposicao(Long idProposicao) {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM encaminhamentoproposicao WHERE proposicao_id = :idProposicao");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

}
