package br.gov.mj.sislegis.app.service.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.TipoEncaminhamentoService;

@Stateless
public class TipoEncaminhamentoServiceEjb extends AbstractPersistence<TipoEncaminhamento, Long> implements
		TipoEncaminhamentoService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	public TipoEncaminhamentoServiceEjb() {
		super(TipoEncaminhamento.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void deleteById(Long id) {

		int deleted = em
				.createNativeQuery(
						"delete from tarefa where encaminhamentoproposicao_id in (select id from encaminhamentoproposicao where tipo_encaminhamento_id=:id)")
				.setParameter("id", id).executeUpdate();
		super.deleteById(id);
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}

}
