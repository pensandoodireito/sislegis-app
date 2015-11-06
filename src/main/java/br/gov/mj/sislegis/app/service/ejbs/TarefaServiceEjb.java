package br.gov.mj.sislegis.app.service.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.TarefaService;

@Stateless
public class TarefaServiceEjb extends AbstractPersistence<Tarefa, Long> implements TarefaService {

	@PersistenceContext
	private EntityManager em;

	public TarefaServiceEjb() {
		super(Tarefa.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Tarefa save(Tarefa entity) {
		entity = super.save(entity);
		return entity;
	}

	@Override
	public Tarefa buscarPorId(Long idTarefa) {
		TypedQuery<Tarefa> findByIdQuery = em
				.createQuery("SELECT t FROM Tarefa t WHERE t.id = :idTarefa", Tarefa.class);
		findByIdQuery.setParameter("idTarefa", idTarefa);
		List<Tarefa> resultList = findByIdQuery.getResultList();

		if (resultList.size() > 0) {
			for (Tarefa tarefa : resultList) {
				if (tarefa.getEncaminhamentoProposicao() != null) {
					tarefa.getEncaminhamentoProposicao().getProposicao();
				}
			}
			return resultList.get(0);
		}
		return null;
	}

	@Override
	public List<Tarefa> buscarPorUsuario(Long idUsuario) {
		TypedQuery<Tarefa> findByIdQuery = em.createQuery("SELECT t FROM Tarefa t WHERE t.usuario.id = :idUsuario",
				Tarefa.class);
		findByIdQuery.setParameter("idUsuario", idUsuario);
		List<Tarefa> resultList = findByIdQuery.getResultList();

//		for (Tarefa tarefa : resultList) {
//			if (tarefa.getEncaminhamentoProposicao() != null) {
//				tarefa.setProposicao(tarefa.getEncaminhamentoProposicao().getProposicao());
//			}
//		}

		return resultList;
	}

	@Override
	public Tarefa buscarPorEncaminhamentoProposicaoId(Long idEncaminhamentoProposicao) {
		TypedQuery<Tarefa> findByIdQuery = em.createQuery(
				"SELECT t FROM Tarefa t WHERE t.encaminhamento.id = :idEncaminhamentoProposicao", Tarefa.class);
		findByIdQuery.setParameter("idEncaminhamentoProposicao", idEncaminhamentoProposicao);
		List<Tarefa> resultList = findByIdQuery.getResultList();

		if (resultList.size() > 0) {
			return resultList.get(0);
		}

		return null;
	}

	@Override
	public void marcarComoVisualizadas(List<Long> idTarefas) {
		for (Long id : idTarefas) {
			Query query = em.createQuery("UPDATE Tarefa SET isVisualizada = TRUE WHERE id = :id", Tarefa.class);
			query.setParameter("id", id);
			query.executeUpdate();
		}
	}

	@Override
	public void updateTarefa(EncaminhamentoProposicao savedEntity) {
		// Caso uma tarefa já exista, significa que foi atualizada. Excluímos a
		// antiga antes de atualizar.
		Tarefa tarefaPorEncaminhamentoProposicaoId = buscarPorEncaminhamentoProposicaoId(savedEntity.getId());
		if (tarefaPorEncaminhamentoProposicaoId != null) {
			if (savedEntity.getResponsavel() != null) {
				tarefaPorEncaminhamentoProposicaoId.setUsuario(savedEntity.getResponsavel());
				save(tarefaPorEncaminhamentoProposicaoId);
			} else {
				deleteById(tarefaPorEncaminhamentoProposicaoId.getId());
			}
		} else {
			if (savedEntity.getResponsavel() != null) {
				// Criamos a nova tarefa
				Tarefa tarefa = Tarefa.createTarefaEncaminhamento(savedEntity.getResponsavel(), savedEntity);

				save(tarefa);
			}
		}
	}

}
