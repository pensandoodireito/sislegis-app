package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Casa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;

@Local
public interface AgendaComissaoService extends Service<AgendaComissao> {

	AgendaComissao getAgenda(Casa casa, String comissao);

	AgendaComissao getAgenda(Casa casa, String comissao, boolean loadSessoes);

	void atualizaStatusAgendas();

	List<Usuario> listSeguidoresAgenda(AgendaComissao agenda);

	List<AgendaComissao> listAgendasSeguidas();

	void followComissao(Casa casa, String comissao, Usuario user);

	void unfollowComissao(Casa casa, String comissao, Usuario user);

}
