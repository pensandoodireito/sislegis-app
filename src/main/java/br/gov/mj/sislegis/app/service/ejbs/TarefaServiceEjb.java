package br.gov.mj.sislegis.app.service.ejbs;

import java.text.SimpleDateFormat;
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
import br.gov.mj.sislegis.app.model.Notificacao;
import br.gov.mj.sislegis.app.model.Tarefa;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.NotificacaoService;
import br.gov.mj.sislegis.app.service.TarefaService;

@Stateless
public class TarefaServiceEjb extends AbstractPersistence<Tarefa, Long> implements TarefaService {

	private static final String CATEGORIA_TAREFAS = "TAREFAS";
	@PersistenceContext
	private EntityManager em;
	@Inject
	NotificacaoService notificacaoService;

	public TarefaServiceEjb() {
		super(Tarefa.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Tarefa save(Tarefa entity) {
		boolean criando = (entity.getId() == null);

		entity = super.save(entity);
		if (criando) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM kk:mm");
			// cria notificacao
			EncaminhamentoProposicao enc = entity.getEncaminhamentoProposicao();
			String notTxt = enc.getTipoEncaminhamento().getNome();
			if (enc.getDetalhes() != null && !enc.getDetalhes().isEmpty()) {
				notTxt += enc.getDetalhes();
			}
			if (enc.getDataHoraLimite() != null) {
				notTxt += " - " + sdf.format(enc.getDataHoraLimite());
			}
			Notificacao not = new Notificacao(entity.getUsuario(), notTxt, entity.getId().toString(), CATEGORIA_TAREFAS);

			notificacaoService.save(not);
		} else if (entity.isFinalizada()) {
			deleteNotificacaoAssociada(entity.getId());
		}

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

		return resultList;
	}

	@Override
	public Tarefa buscarPorEncaminhamentoProposicaoId(Long idEncaminhamentoProposicao) {
		TypedQuery<Tarefa> findByIdQuery = em.createQuery(
				"SELECT t FROM Tarefa t WHERE t.encaminhamentoProposicao.id = :idEncaminhamentoProposicao",
				Tarefa.class);
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

	private void deleteNotificacaoAssociada(Long idTarefa) {
		Notificacao n = notificacaoService.buscarPorCategoriaEntidade(CATEGORIA_TAREFAS, idTarefa.toString());
		if (n != null) {
			notificacaoService.deleteById(n.getId().longValue());
		}
	}

	@Override
	public void deleteById(Long id) {
		deleteNotificacaoAssociada(id);
		super.deleteById(id);
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

	@Override
	public void finalizar(Long idTarefa, String comentarioFinalizacao) {
		Tarefa tarefa = findById(idTarefa);
		tarefa.setFinalizada(true);
		tarefa.getEncaminhamentoProposicao().setFinalizado(true);
		deleteNotificacaoAssociada(idTarefa);

		Comentario comentario = new Comentario();
		comentario.setProposicao(tarefa.getEncaminhamentoProposicao().getProposicao());
		comentario.setDescricao(comentarioFinalizacao);
		comentario.setDataCriacao(new Date());
		comentario.setAutor(tarefa.getUsuario());

		tarefa.setComentarioFinalizacao(comentario);
		tarefa.getEncaminhamentoProposicao().setComentarioFinalizacao(comentario);

		save(tarefa);
	}

}
