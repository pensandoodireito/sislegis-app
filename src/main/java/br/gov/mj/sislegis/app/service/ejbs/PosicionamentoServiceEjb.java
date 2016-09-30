package br.gov.mj.sislegis.app.service.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Posicionamento;
import br.gov.mj.sislegis.app.model.Usuario;
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

	@Override
	public Posicionamento getByName(String string) {
		TypedQuery<Posicionamento> findByIdQuery = em
				.createQuery(
						"SELECT u FROM Posicionamento u WHERE upper(TRANSLATE(u.nome,'ÀÁáàÃãÉÈéèÍíÕõÓóÒòÚú','AAaaAaEEeeIiOoOoOoUu')) = upper(TRANSLATE(:nome,'ÀÁáàÃãÉÈéèÍíÕõÓóÒòÚú','AAaaAaEEeeIiOoOoOoUu')) ",
						Posicionamento.class);
		findByIdQuery.setParameter("nome", string);

		try {
			Posicionamento user = findByIdQuery.getSingleResult();
			return user;
		} catch (javax.persistence.NoResultException e) {
			// no execption just return null
			return null;
		}
	}

}
