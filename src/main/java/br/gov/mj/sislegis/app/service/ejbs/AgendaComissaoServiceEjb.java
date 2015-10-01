package br.gov.mj.sislegis.app.service.ejbs;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
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

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ReuniaoBean;
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
	public AgendaComissao getAgenda(String comissao) {
		try {
			return findByProperty("comissao", comissao);
		} catch (Exception e) {
			e.printStackTrace();
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

	@Schedule(minute = "*/3", hour = "*", persistent = false, info = "Atualiza status pautas")
	public void atualizaStatusAgendas() {
		Set<AgendaComissao> atualizadas = new HashSet<AgendaComissao>();

		ParserPautaSenado parserSenado = new ParserPautaSenado();
		try {
			String semanaDo = "20150928";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			List<Comissao> comissoesCamara = comissaoService.listarComissoesCamara();
			for (Iterator<Comissao> iterator = comissoesCamara.iterator(); iterator.hasNext();) {
				Comissao comissao = (Comissao) iterator.next();
				String sigla = comissao.getSigla().trim();
				AgendaComissao agenda = getAgenda(sigla);
				if (agenda == null) {
					agenda = new AgendaComissao(sigla, sdf.parse(semanaDo));
					agenda = save(agenda);
				}

				List<ReuniaoBean> reunioes = parserSenado.getReunioes(sigla, semanaDo);

				for (Iterator<ReuniaoBean> iterator2 = reunioes.iterator(); iterator2.hasNext();) {
					ReuniaoBean reuniaoBean = (ReuniaoBean) iterator2.next();
					Sessao sessaoWS = reuniaoBean.getSessao();
					Sessao sessaoDb = agenda.getSessao(sessaoWS.getIdentificadorExterno());
					if (sessaoDb == null) {
						agenda.addSessao(sessaoWS);
						atualizadas.add(agenda);
					} else {
						if (changeDetectpr.compare(sessaoWS, sessaoDb) != 0) {
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
			}
		} else {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).warning(
					"Nenhum usuário seguindo a agenda " + agenda.getComissao());
		}
		// SislegisUtil.sendEmail(toMail, toName, subject, body);

		return new AsyncResult<String>(status);
	}
}
