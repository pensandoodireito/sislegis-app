package br.gov.mj.sislegis.app.service.ejbs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.mail.EmailException;

import br.gov.mj.sislegis.app.model.Casa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;
import br.gov.mj.sislegis.app.parser.ReuniaoBean;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ReuniaoBeanSenado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.AgendaComissaoService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Singleton
public class AgendaComissaoServiceEjb extends AbstractPersistence<AgendaComissao, Long> implements
		AgendaComissaoService {

	@Inject
	ComissaoService comissaoService;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private UsuarioService usuarioService;

	public AgendaComissaoServiceEjb() {
		super(AgendaComissao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public AgendaComissao getAgenda(Casa casa, String comissao) {
		return getAgenda(casa, comissao, false);
	}

	@Override
	public AgendaComissao getAgenda(Casa casa, String comissao, boolean forceload) {

		try {
			AgendaComissao agenda = (AgendaComissao) em.createNamedQuery("getByCasaComissao")
					.setParameter("casa", casa).setParameter("comissao", comissao).getSingleResult();
			if (forceload) {
				agenda.getSessoes().size();
			}
			return agenda;
		} catch (javax.persistence.NoResultException e) {
			return null;
		} catch (Exception e) {

			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao buscar agenda", e);
			return null;
		}

	}

	public List<AgendaComissao> listAgendasSeguidas() {
		List<AgendaComissao> seguidas = em.createNamedQuery("getAllAgendasSeguidas", AgendaComissao.class)
				.getResultList();
		return seguidas;
	}

	public List<Usuario> listSeguidoresAgenda(AgendaComissao agenda) {
		List<Usuario> seguidas = em.createNamedQuery("getSeguidoresAgenda", Usuario.class)
				.setParameter(":idAgenda", agenda.getId()).getResultList();
		return seguidas;
	}

	Comparator<Sessao> changeDetectpr = new Comparator<Sessao>() {

		@Override
		public int compare(Sessao o1, Sessao o2) {
			if (o1.getData().equals(o2.getData())) {
				if (o1.getSituacao().equals(o2.getSituacao())) {
					if (o1.getTitulo().equals(o2.getTitulo())) {
						return 0;
					}
				}

			}
			return 1;
		}
	};

	@Timeout
	public void timeout(Timer timer) {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning(
				"Timeout na execução da atualização de agenda " + timer.getInfo());
	}

	@Override
	public void unfollowComissao(Casa casa, String comissao, Usuario user) {
		AgendaComissao agenda = getAgenda(casa, comissao);
		if (agenda == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning(
					"Tentativa de remover notificacoes de comissao nao seguida");
			return;
		}
		user = usuarioService.loadComAgendasSeguidas(user.getId());
		user.removeAgendaSeguida(agenda);
		usuarioService.save(user);
	}

	@Override
	public void followComissao(Casa casa, String comissao, Usuario user) {
		AgendaComissao agenda = getAgenda(casa, comissao);
		if (agenda == null) {
			agenda = new AgendaComissao(casa, comissao, getNextMonday());
			agenda = save(agenda);
		}
		user = usuarioService.loadComAgendasSeguidas(user.getId());
		if (user.getAgendasSeguidas().contains(agenda)) {
			return;
		}
		user.addAgendaSeguida(agenda);
		usuarioService.save(user);
	}

	/**
	 * Busca a próxima segunda feira
	 * */

	private Calendar getNextMonday() {

		return getNextMondayAfter(Calendar.getInstance());
	}

	private Calendar getNextMondayAfter(Calendar date1) {

		while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			date1.add(Calendar.DATE, 1);
		}
		return date1;
	}

	@Schedule(minute = "*/3", hour = "*", persistent = false, info = "Atualiza status pautas")
	public void atualizaStatusAgendas() {
		Set<AgendaComissao> atualizadas = new HashSet<AgendaComissao>();
		Calendar nextMonday = getNextMonday();
		Calendar weekEnd = Calendar.getInstance();
		weekEnd.setTime(nextMonday.getTime());
		weekEnd.add(Calendar.DATE, 6);// Até o domingo
		try {
			SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyyMMdd");
			String semanaDo = dataFormatter.format(nextMonday.getTime());
			String semanaAte = dataFormatter.format(weekEnd.getTime());
			ParserPautaSenado parserSenado = new ParserPautaSenado();

			ParserPautaCamara parserCamara = new ParserPautaCamara();

			List<AgendaComissao> comissoes = listAgendasSeguidas();
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info("Há " + comissoes.size() + " comissões seguidas");
			for (Iterator<AgendaComissao> iterator = comissoes.iterator(); iterator.hasNext();) {
				AgendaComissao agenda = iterator.next();
				List<Sessao> sessoesReuniao = null;
				List<ReuniaoBean> reunioes = new ArrayList<ReuniaoBean>();
				if (Casa.CAMARA.equals(agenda.getCasa())) {
					reunioes.addAll(parserCamara.getReunioes(agenda.getComissao(), semanaDo, semanaAte));
				} else {
					reunioes.addAll(parserSenado.getReunioes(agenda.getComissao(), semanaDo));
				}
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info(
						"Atualizando " + agenda.getComissao() + " há " + reunioes.size() + " reunioes");

				for (Iterator<ReuniaoBean> iterator2 = reunioes.iterator(); iterator2.hasNext();) {
					ReuniaoBean reuniaoBean = (ReuniaoBean) iterator2.next();
					Sessao sessaoWS = reuniaoBean.getSessao();
					Sessao sessaoDb = agenda.getSessao(sessaoWS.getIdentificadorExterno());
					if (sessaoDb == null) {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info("Sessão nao existia.");
						agenda.addSessao(sessaoWS);
						atualizadas.add(agenda);
					} else {
						if (changeDetectpr.compare(sessaoWS, sessaoDb) != 0) {
							Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info(
									"Mudança detectada em " + agenda.getComissao());
							sessaoDb.popula(sessaoWS);
							agenda.addSessao(sessaoDb);
							atualizadas.add(agenda);
						}

					}
				}
				agenda.setConsultada();
				if (atualizadas.contains(agenda)) {
					agenda.setPautasAtualizadas();
					notifyUsuariosPautaMudou(agenda);
				}
				save(agenda);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Asynchronous
	public Future<String> notifyUsuariosPautaMudou(AgendaComissao agenda) {
		String status = "";
		List<Usuario> seguidores = usuarioService.listUsuariosSeguidoresDeComissao(agenda);
		if (seguidores != null && !seguidores.isEmpty()) {
			for (Iterator<Usuario> iterator = seguidores.iterator(); iterator.hasNext();) {
				Usuario usuario = (Usuario) iterator.next();
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning("Notificando " + usuario.getEmail());
				try {
					SislegisUtil
							.sendEmail(usuario.getEmail(), usuario.getNome(), "Detectada mudança na pauta da comissão "
									+ agenda.getComissao(), "Veja as mudanças aqui :");
				} catch (EmailException e) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
							"Erro ao enviar email para " + usuario.getEmail(), e);
				}
			}
		} else {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning(
					"Nenhum usuário seguindo a agenda " + agenda.getComissao());
		}

		return new AsyncResult<String>(status);
	}

}
