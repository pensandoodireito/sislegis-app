package br.gov.mj.sislegis.app.service.ejbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Proposicao;
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

	public List<ComentarioJSON> findByProposicao(Long id) {
		List<ComentarioJSON> lista = new ArrayList<ComentarioJSON>();

		final List<Comentario> results = findByIdProposicao(id);
		for (Comentario comentario : results) {
			lista.add(new ComentarioJSON(comentario.getId(), comentario.getDescricao(), comentario.getAutor(),
					comentario.getDataCriacao(), comentario.getProposicao().getId()));

		}
		return lista;
	}

	public ComentarioJSON findByIdJSON(Long id) {
		TypedQuery<Comentario> findByIdQuery = em.createQuery("SELECT c FROM Comentario c "
				+ "LEFT JOIN FETCH c.autor a " + "LEFT JOIN FETCH c.proposicao p WHERE c.id = :entityId",
				Comentario.class);
		findByIdQuery.setParameter("entityId", id);
		List<Comentario> lista = findByIdQuery.getResultList();
		Comentario comentario = null;
		if (!Objects.isNull(lista) && !lista.isEmpty()) {
			comentario = lista.get(0);
		}
		Long idProposicao = Objects.isNull(comentario.getProposicao()) ? null : comentario.getProposicao().getId();
		return new ComentarioJSON(comentario.getId(), comentario.getDescricao(), comentario.getAutor(),
				comentario.getDataCriacao(), idProposicao);
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

	@Override
	public void salvarComentario(ComentarioJSON comentarioJSON, Usuario autor) throws IllegalAccessException {
		if (comentarioJSON.getId() != null) {
			if (comentarioJSON.getAutor() != null) {
				if (!comentarioJSON.getAutor().equals(autor)) {
					throw new IllegalAccessException("Somente autor do comentário pode alterá-lo.");
				}
			}
		}
		Comentario comentario = populaEntidadeComentario(comentarioJSON, autor);
		save(comentario);

	}

	private Comentario populaEntidadeComentario(ComentarioJSON comentarioJSON, Usuario autor) {
		Comentario comentario = new Comentario();
		Proposicao proposicao = new Proposicao();
		proposicao.setId(comentarioJSON.getIdProposicao());
		comentario.setDataCriacao(comentarioJSON.getDataCriacao());
		comentario.setId(comentarioJSON.getId());
		comentario.setDescricao(comentarioJSON.getDescricao());
		comentario.setProposicao(proposicao);
		comentario.setAutor(autor);
		return comentario;
	}

}
