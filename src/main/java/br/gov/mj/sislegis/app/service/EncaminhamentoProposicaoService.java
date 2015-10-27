package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.EncaminhamentoProposicao;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EncaminhamentoProposicaoService extends Service<EncaminhamentoProposicao> {

	EncaminhamentoProposicao salvarEncaminhamentoProposicao(EncaminhamentoProposicao encaminhamentoProposicao, String referer);

	List<EncaminhamentoProposicao> findByProposicao(Long idProposicao);

	Integer totalByProposicao(Long idProposicao);

	void finalizar(Long idEncaminhamentoProposicao, String descricaoComentario);
}
