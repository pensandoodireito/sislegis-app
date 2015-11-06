package br.gov.mj.sislegis.app.service.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.TipoEncaminhamento;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.TipoEncaminhamentoService;

@Stateless
public class TipoEncaminhamentoServiceEjb extends AbstractPersistence<TipoEncaminhamento, Long> implements TipoEncaminhamentoService {
	
	@PersistenceContext
    private EntityManager em;
	
	public TipoEncaminhamentoServiceEjb() {
		super(TipoEncaminhamento.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
