package br.gov.mj.sislegis.app.service.ejbs;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Reuniao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ReuniaoService;

@Stateless
public class ReuniaoServiceEjb extends AbstractPersistence<Reuniao, Long> implements ReuniaoService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	public ReuniaoServiceEjb() {
		super(Reuniao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	public Reuniao buscaReuniaoPorData(Date data) {
		try {
			return findByProperty("data", data);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public List<Reuniao> reunioesPorMes(Integer mes, Integer ano) {
		TypedQuery<Reuniao> query = em.createQuery("FROM Reuniao WHERE EXTRACT(MONTH FROM data) = :paramMes AND EXTRACT(YEAR FROM data) = :paramAno ORDER BY data desc ", Reuniao.class);
		query.setParameter("paramMes", mes);
		query.setParameter("paramAno", ano);
		return query.getResultList();
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		em = (EntityManager) injections[0];

	}
}