package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;

@Local
public interface EncaminhamentoProposicaoService extends Service<EncaminhamentoProposicao> {

	EncaminhamentoProposicao salvarEncaminhamentoProposicao(EncaminhamentoProposicao encaminhamentoProposicao);

	List<EncaminhamentoProposicao> findByProposicao(Long idProposicao);

	Integer totalByProposicao(Long idProposicao);

	void finalizar(Long idEncaminhamentoProposicao, String descricaoComentario, Usuario autor);

	EncaminhamentoProposicao salvarEncaminhamentoProposicaoAutomatico(String detalhe, Proposicao p, Usuario responsavel);

	EncaminhamentoProposicao getByComentarioFinalizacao(Long id);

}
