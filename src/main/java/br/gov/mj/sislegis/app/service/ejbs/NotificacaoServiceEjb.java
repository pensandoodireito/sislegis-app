package br.gov.mj.sislegis.app.service.ejbs;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Notificacao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.NotificacaoService;

@Stateless
public class NotificacaoServiceEjb extends AbstractPersistence<Notificacao, Long> implements NotificacaoService,
		EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	public NotificacaoServiceEjb() {
		super(Notificacao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Notificacao> listaNotificacoesParaUsuario(Usuario usuario) {
		return listaNotificacoesParaUsuario(usuario.getId());
	}

	@Override
	public List<Notificacao> listaNotificacoesParaUsuario(Long idUsuario) {
		return listaNotificacoesParaUsuario(idUsuario, null);
	}

	@Override
	public List<Notificacao> listaNotificacoesParaUsuario(Usuario usuario, String categoria) {
		return listaNotificacoesParaUsuario(usuario.getId(), categoria);
	}

	@Override
	public List<Notificacao> listaNotificacoesParaUsuario(Long idUsuario, String categoria) {
		TypedQuery<Notificacao> findByIdQuery = null;

		if (categoria == null) {
			findByIdQuery = em.createNamedQuery("getAllNotificacaoByUsuario", Notificacao.class);
		} else {
			findByIdQuery = em.createNamedQuery("getCategoriaNotificacaoByUsuario", Notificacao.class);
			findByIdQuery.setParameter("categoria", categoria);
		}
		findByIdQuery.setParameter("idUsuario", idUsuario);

		return findByIdQuery.getResultList();
	}

	@Override
	public void marcarComoVisualizada(Collection<Long> ids) {
		for (Iterator<Long> iterator = ids.iterator(); iterator.hasNext();) {
			Long id = (Long) iterator.next();
			Notificacao notificacao = findById(id);
			notificacao.setVisualizada();
			save(notificacao);
		}
	}

	@Override
	public Notificacao buscarPorCategoriaEntidade(String categoria, String entidadeId) {
		List<Notificacao> l = (em.createNamedQuery("getByCategoriaEntidade", Notificacao.class)
				.setParameter("categoria", categoria).setParameter("identificacaoEntidade", entidadeId).getResultList());
		if (l.isEmpty()) {
			return null;
		} else {
			return l.get(0);

		}
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}

}
