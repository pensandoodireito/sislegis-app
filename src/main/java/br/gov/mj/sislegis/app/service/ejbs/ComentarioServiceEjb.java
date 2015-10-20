package br.gov.mj.sislegis.app.service.ejbs;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComentarioService;

@Stateless
public class ComentarioServiceEjb extends AbstractPersistence<Comentario, Long> implements ComentarioService {

	@PersistenceContext
	private EntityManager em;

	public ComentarioServiceEjb() {
		super(Comentario.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Comentario> findByIdProposicao(Long id) {

		TypedQuery<Comentario> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Comentario c "
				+ "INNER JOIN FETCH c.autor a " + "INNER JOIN FETCH c.proposicao p WHERE p.id = :entityId",
				Comentario.class);

		findByIdQuery.setParameter("entityId", id);
		final List<Comentario> results = findByIdQuery.getResultList();

		return results;
	}

	@Override
	public Integer totalByProposicao(Long idProposicao) {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM comentario WHERE proposicao_id = :idProposicao");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

	@Override
	public void salvarComentario(Comentario comentario, Usuario autor) throws IllegalAccessException {
		if (comentario.getId() != null) {
			if (comentario.getAutor() != null) {
				if (!comentario.getAutor().equals(autor)) {
					throw new IllegalAccessException("Somente autor do comentário pode alterá-lo.");
				}
			}
		}
		comentario.setAutor(autor);
		save(comentario);
	}

}
