package br.gov.mj.sislegis.app.service.ejbs;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.apache.commons.mail.EmailException;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.service.WatchProposicaoService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

/**
 * Executa monitoramento de proposicao
 * 
 * @author rafael.coutinho
 *
 */
@Singleton
public class WatchProposicaoEjb implements WatchProposicaoService {
	@Inject
	ProposicaoService proposicaoService;

	@Inject
	UsuarioService usuarioService;

	@Override
	@Schedule(minute = "*", hour = "*", persistent = false, info = "Atualiza status proposicoes")
	public void atualizaProposicoesSeguidas() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualizando proposicoes seguidas");
		List<Proposicao> proposicoesSeguidas = proposicaoService.listProposicoesSeguidas();
		for (Iterator<Proposicao> iterator = proposicoesSeguidas.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			try {
				if (proposicaoService.syncDadosProposicao(proposicao)) {
					notifyUsuariosProposicaoMudou(proposicao);
				}
			} catch (IOException e) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
						"Falhou ao atualizar proposicao " + proposicao.getSigla(), e);
			}
		}
	}

	/**
	 * Envia email notificando da alteração de pauta para todos os seguidores de
	 * uma comissão
	 * 
	 * @param agenda
	 * @return
	 */
	@Asynchronous
	public Future<String> notifyUsuariosProposicaoMudou(Proposicao proposicao) {
		String status = "";
		ResourceBundle res = ResourceBundle.getBundle("messages");
		String assunto = MessageFormat.format(res.getString("email.mudanca_proposicao.assunto"), proposicao.getSigla());

		List<Usuario> seguidores = usuarioService.listUsuariosSeguidoresDeProposicao(proposicao);
		if (seguidores != null && !seguidores.isEmpty()) {
			for (Iterator<Usuario> iterator = seguidores.iterator(); iterator.hasNext();) {
				Usuario usuario = (Usuario) iterator.next();
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info("Notificando " + usuario.getEmail());

				String body = MessageFormat.format(res.getString("email.mudanca_proposicao.body"), usuario.getNome(),
						proposicao.getSigla());

				try {
					SislegisUtil.sendEmail(usuario.getEmail(), usuario.getNome(), assunto, body);
				} catch (EmailException e) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
							"Erro ao enviar email para " + usuario.getEmail(), e);
				}
			}
			status = "OK+";
		} else {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning(
					"Nenhum usuário seguindo a proposicao " + proposicao.getSigla());
			status = "OK";
		}

		return new AsyncResult<String>(status);
	}

}
