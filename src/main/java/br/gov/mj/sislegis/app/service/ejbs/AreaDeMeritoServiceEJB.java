package br.gov.mj.sislegis.app.service.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.AreaDeMerito;
import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.AreaDeMeritoService;
import br.gov.mj.sislegis.app.service.DocumentoService;

@Stateless
public class AreaDeMeritoServiceEJB extends AbstractPersistence<AreaDeMerito, Long> implements AreaDeMeritoService, EJBUnitTestable {
	@PersistenceContext
	private EntityManager em;
	@Inject
	private DocumentoService docService;

	public AreaDeMeritoServiceEJB() {
		super(AreaDeMerito.class);
	}

	@Override
	public List<AreaDeMeritoRevisao> listRevisoesProposicao(Long idProposicao) {
		List<AreaDeMeritoRevisao> seguidas = em.createNamedQuery("AreaDeMeritoRevisaoByProposicao", AreaDeMeritoRevisao.class).setParameter("prop", idProposicao).getResultList();
		return seguidas;
	}

	@Override
	public AreaDeMerito saveAreaDeMerito(AreaDeMerito entity) {
		entity = this.save(entity);
		return entity;
	}

	@Override
	public List<AreaDeMeritoRevisao> listRevisoes(Long idArea, boolean pendentes) {
		List<AreaDeMeritoRevisao> seguidas = em.createNamedQuery("AreaDeMeritoRevisaoByArea", AreaDeMeritoRevisao.class).setParameter("idArea", idArea).getResultList();
		return seguidas;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}

	@Override
	public AreaDeMeritoRevisao saveRevisao(AreaDeMeritoRevisao e) {

		if (e.getId() != null)
			return getEntityManager().merge(e);
		else {

			getEntityManager().persist(e);
			return e;
		}
	}

	public void remove(AreaDeMeritoRevisao entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	@Override
	public AreaDeMeritoRevisao findRevisao(Long idRevisao) {
		AreaDeMeritoRevisao rev = getEntityManager().find(AreaDeMeritoRevisao.class, idRevisao);
		if (rev == null) {
			System.out.println("Por algum motivo não enconra pelo id");
			return getEntityManager().createQuery("select rev from AreaDeMeritoRevisao rev where rev.id=:id", AreaDeMeritoRevisao.class).setParameter("id", idRevisao).setMaxResults(1).getSingleResult();
		}
		return rev;
	}

	@Override
	public void deleteRevisao(AreaDeMeritoRevisao rev) {
		getEntityManager().merge(rev);
		getEntityManager().remove(rev);

	}

	@Override
	public void deleteRevisao(Long idRevisao) {
		getEntityManager().remove(findRevisao(idRevisao));
		
	}

}
