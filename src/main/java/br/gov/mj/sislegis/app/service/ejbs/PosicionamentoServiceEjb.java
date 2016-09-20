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
	public void deleteById(Long id) {
		em.createNativeQuery(
				"update proposicao set posicionamento_atual_id = NULL where posicionamento_atual_id in (select id from posicionamento_proposicao where posicionamento_id=:id)")
				.setParameter("id", id).executeUpdate();
		super.deleteById(id);
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
	}

}
