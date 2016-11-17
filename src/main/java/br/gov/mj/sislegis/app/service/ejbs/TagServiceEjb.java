package br.gov.mj.sislegis.app.service.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.Tag;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.TagService;

@Stateless
public class TagServiceEjb extends AbstractPersistence<Tag, Long> implements TagService, EJBUnitTestable {

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

	@Override
	public void replace(String id, Tag entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(String id) {
		Tag entity = findById(id);
		getEntityManager().createNativeQuery("delete from tagproposicao where tag_id=:tag")
				.setParameter("tag", entity.getTag()).executeUpdate();
		getEntityManager().remove(entity);

	}
}
