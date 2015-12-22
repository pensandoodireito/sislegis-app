package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.SituacaoLegislativa;
import br.gov.mj.sislegis.app.model.Usuario;

/**
 * Servico para gerenciar situacoes legislativas
 * 
 * @author rafaelcoutinho
 *
 */
@Local
public interface SituacaoLegislativaService extends Service<SituacaoLegislativa> {

	SituacaoLegislativa getSituacao(Origem casa, Long idExterno);

	List<SituacaoLegislativa> listSituacoes(Origem origem);

	Long save(SituacaoLegislativa sit, Usuario usuario);
	void updateSituacoes();

}
