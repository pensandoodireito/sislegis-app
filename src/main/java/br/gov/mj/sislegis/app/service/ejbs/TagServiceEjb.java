package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.model.Tag;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.TagService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class TagServiceEjb extends AbstractPersistence<Tag, Long>
implements TagService, EJBUnitTestable{
	
	@PersistenceContext
    private EntityManager em;
	
	public TagServiceEjb() {
		super(Tag.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Tag findById(String id) {
		return findByProperty("tag", id);
	}

	@Override
	public List<Tag> listarTodasTags() {
		return listAll();
	}
	
	@Override
	public List<Tag> buscaPorSufixo(String sufixo) {
		List<Tag> lista = findByProperty("tag", sufixo, "ASC");
		return lista;
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];

	}
}
