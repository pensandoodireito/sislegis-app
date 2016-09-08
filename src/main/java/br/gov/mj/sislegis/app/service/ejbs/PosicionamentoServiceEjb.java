package br.gov.mj.sislegis.app.service.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.PosicionamentoService;

@Stateless
public class PosicionamentoServiceEjb extends AbstractPersistence<Posicionamento, Long> implements
		PosicionamentoService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	public PosicionamentoServiceEjb() {
		super(Posicionamento.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}

}
