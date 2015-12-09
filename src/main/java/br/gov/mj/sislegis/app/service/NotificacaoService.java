package br.gov.mj.sislegis.app.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.gov.mj.sislegis.app.model.Notificacao;
import br.gov.mj.sislegis.app.model.Usuario;

/**
 * Serviço de controle de notificações.
 * 
 * @author coutinho
 *
 */
public interface NotificacaoService extends Service<Notificacao> {

	Notificacao save(Notificacao entity);

	/**
	 * Lista todas para usuário de qualquer categoria
	 * 
	 * @param idUsuario
	 * @return
	 */
	List<Notificacao> listaNotificacoesParaUsuario(Long idUsuario);

	List<Notificacao> listaNotificacoesParaUsuario(Usuario usuario);

	/**
	 * Lista todas para usuário de uma categoria
	 * 
	 * @param idUsuario
	 * @return
	 */
	List<Notificacao> listaNotificacoesParaUsuario(Long idUsuario, String categoria);

	List<Notificacao> listaNotificacoesParaUsuario(Usuario usuario, String categoria);

	Notificacao buscarPorCategoriaEntidade(String categoria, String entidadeId);

	void marcarComoVisualizada(Collection<Long> ids);

}
