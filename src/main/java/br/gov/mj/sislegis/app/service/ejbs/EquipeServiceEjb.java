package br.gov.mj.sislegis.app.service.ejbs;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.model.Equipe;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.EquipeService;
import br.gov.mj.sislegis.app.service.UsuarioService;

@Stateless
public class EquipeServiceEjb extends AbstractPersistence<Equipe, Long> implements EquipeService, EJBUnitTestable {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private UsuarioService usuarioService;

	public EquipeServiceEjb() {
		super(Equipe.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Equipe salvarEquipe(Equipe equipe) {
		if (equipe.getId() != null) {
			Equipe db = findById(equipe.getId());
			for (Iterator iterator = db.getListaEquipeUsuario().iterator(); iterator.hasNext();) {
				Usuario dbuser = (Usuario) iterator.next();
				if (!equipe.getListaEquipeUsuario().contains(dbuser)) {
					dbuser.setEquipe(null);
					em.merge(dbuser);
				}
			}
		}
		Equipe managedEquipe = this.save(equipe);
		for (Usuario equipeUsuario : equipe.getListaEquipeUsuario()) {
			equipeUsuario.setEquipe(managedEquipe);
			em.merge(equipeUsuario);
		}

		return managedEquipe;
	}

	@Override
	public void remove(Equipe equipe) {
		if (equipe.getId() != null) {
			Equipe db = findById(equipe.getId());
			for (Iterator iterator = db.getListaEquipeUsuario().iterator(); iterator.hasNext();) {
				Usuario dbuser = (Usuario) iterator.next();
				dbuser.setEquipe(null);
				em.merge(dbuser);
			}
		}
		super.remove(equipe);
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		usuarioService = (UsuarioService) injections[1];

	}

	@Override
	public Equipe getByName(String nome) {
		try {
			return em.createQuery("select e from Equipe e where e.nome=:nome", Equipe.class).setParameter("nome", nome)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Equipe findByIdFull(Long id) {
		Equipe e = findById(id);
		e.getListaEquipeUsuario().size();
		return e;
	}
}
