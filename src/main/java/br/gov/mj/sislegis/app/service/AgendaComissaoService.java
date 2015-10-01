package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;

@Local
public interface AgendaComissaoService extends Service<AgendaComissao> {

	AgendaComissao getAgenda(String comissao);

	void atualizaStatusAgendas();

	List<Usuario> listSeguidoresAgenda(AgendaComissao agenda);

	List<AgendaComissao> listAgendasSeguidas();

}
