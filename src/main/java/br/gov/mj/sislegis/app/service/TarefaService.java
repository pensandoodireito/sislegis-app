package br.gov.mj.sislegis.app.service;

import java.util.List;

import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Tarefa;

public interface TarefaService extends Service<Tarefa> {

	Tarefa save(Tarefa entity);

	Tarefa buscarPorId(Long idTarefa);

	List<Tarefa> buscarPorUsuario(Long idUsuario);

	Tarefa buscarPorEncaminhamentoProposicaoId(Long idEncaminhamentoProposicao);

	void marcarComoVisualizadas(List<Long> idTarefas);
	void finalizar(Long idTarefa, String comentarioFinalizacao);

	void updateTarefa(EncaminhamentoProposicao savedEntity);

	Tarefa getTarefaDeComentario(Long id);

	void refreshTarefa(EncaminhamentoProposicao savedEntity, String newText);

}
