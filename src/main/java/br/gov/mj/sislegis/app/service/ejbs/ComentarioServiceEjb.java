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
	public List<Comentario> findByIdProposicao(Integer id) {
		return findByIdProposicao(id, false);
	}

	@Override
	public List<Comentario> findByIdProposicao(Integer id, boolean incluiOcultos) {
		StringBuilder query = new StringBuilder(
				"SELECT DISTINCT c FROM Comentario c INNER JOIN FETCH c.autor a INNER JOIN FETCH c.proposicao p WHERE p.idProposicao = :entityId ");
		if (!incluiOcultos) {
			query.append("AND c.oculto = FALSE");
		}
		query.append(" ORDER BY c.dataCriacao ");

		TypedQuery<Comentario> findByIdQuery = em.createQuery(query.toString(), Comentario.class);

		findByIdQuery.setParameter("entityId", id);
		final List<Comentario> results = findByIdQuery.getResultList();

		return results;
	}

	@Override
	public List<Comentario> findByProposicaoId(Long id) {
		return findByProposicaoId(id, false);
	}

	@Override
	public List<Comentario> findByProposicaoId(Long id, boolean incluiOcultos) {

		StringBuilder query = new StringBuilder(
				"SELECT DISTINCT c FROM Comentario c INNER JOIN FETCH c.autor a INNER JOIN FETCH c.proposicao p WHERE p.id = :entityId ");
		if (!incluiOcultos) {
			query.append("AND c.oculto = FALSE");
		}
		TypedQuery<Comentario> findByIdQuery = em.createQuery(query.toString(), Comentario.class);
		findByIdQuery.setParameter("entityId", id);
		final List<Comentario> results = findByIdQuery.getResultList();

		return results;
	}

	@Override
	public void salvarComentario(Comentario comentario, Usuario autor) throws IllegalAccessException {
		if (comentario.getId() != null) {
			if (comentario.getAutor() != null) {
				if (!comentario.getAutor().getEmail().equals(autor.getEmail())) {
					throw new IllegalAccessException("Somente autor do comentário pode alterá-lo.");
				}
			}
		}
		comentario.setAutor(autor);
		save(comentario);
	}

	@Override
	public Integer totalByProposicao(Long idProposicao) {
		Query query = em
				.createNativeQuery("SELECT COUNT(1) FROM comentario WHERE proposicao_id = :idProposicao AND oculto = FALSE ");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total.intValue();
	}

	@Override
	public void ocultar(Long idComentario) {
		Comentario comentario = findById(idComentario);
		comentario.setOculto(true);
		save(comentario);
	}
}
