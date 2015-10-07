package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;

/**
 * Servico para gerenciar Agendas de comissões seguidas
 * 
 * @author rafaelcoutinho
 *
 */
@Local
public interface AgendaComissaoService extends Service<AgendaComissao> {

	AgendaComissao getAgenda(Origem casa, String comissao);

	AgendaComissao getAgenda(Origem casa, String comissao, boolean loadSessoes);

	void atualizaStatusAgendas();

	List<Usuario> listSeguidoresAgenda(AgendaComissao agenda);

	List<AgendaComissao> listAgendasSeguidas();

	void followComissao(Origem casa, String comissao, Usuario user);

	void unfollowComissao(Origem casa, String comissao, Usuario user);

}
