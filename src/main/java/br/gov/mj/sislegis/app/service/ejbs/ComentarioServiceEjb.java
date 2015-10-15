package br.gov.mj.sislegis.app.service.ejbs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComentarioService;

@Stateless
public class ComentarioServiceEjb extends AbstractPersistence<Comentario, Long>
		implements ComentarioService {

	@PersistenceContext
	private EntityManager em;

	public ComentarioServiceEjb() {
		super(Comentario.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<ComentarioJSON> findByProposicao(Long id) {
		List<ComentarioJSON> lista = new ArrayList<>();
		TypedQuery<Comentario> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Comentario c "
								+ "INNER JOIN FETCH c.autor a "
								+ "INNER JOIN FETCH c.proposicao p WHERE p.id = :entityId",
						Comentario.class);
		findByIdQuery.setParameter("entityId", id);
		final List<Comentario> results = findByIdQuery.getResultList();
		for (Comentario comentario : results) {
			lista.add(new ComentarioJSON(comentario.getId(), comentario
					.getDescricao(), comentario.getAutor(), comentario
					.getDataCriacao(), comentario.getProposicao().getId()));

		}
		return lista;
	}

	@Override
	public List<ComentarioJSON> findByProposicao(Long idProposicao, Integer posicaoInicial, Integer limite) {
		List<ComentarioJSON> lista = new ArrayList<>();
		TypedQuery<Comentario> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Comentario c "
								+ "INNER JOIN FETCH c.autor a "
								+ "INNER JOIN FETCH c.proposicao p "
								+ "WHERE p.id = :entityId "
								+ "ORDER BY c.dataCriacao desc",
						Comentario.class);
		findByIdQuery.setParameter("entityId", idProposicao);

		findByIdQuery.setFirstResult(posicaoInicial);
		findByIdQuery.setMaxResults(limite);

		final List<Comentario> results = findByIdQuery.getResultList();
		for (Comentario comentario : results) {
			lista.add(new ComentarioJSON(comentario.getId(), comentario
					.getDescricao(), comentario.getAutor(), comentario
					.getDataCriacao(), comentario.getProposicao().getId()));

		}
		return lista;
	}

	@Override
	public BigInteger totalByProposicao(Long idProposicao) {
		Query query = em.createNativeQuery("SELECT COUNT(1) FROM comentario WHERE proposicao_id = :idProposicao");
		query.setParameter("idProposicao", idProposicao);
		BigInteger total = (BigInteger) query.getSingleResult();
		return total;
	}

	public ComentarioJSON findByIdJSON(Long id) {
		TypedQuery<Comentario> findByIdQuery = em
				.createQuery(
						"SELECT c FROM Comentario c "
								+ "LEFT JOIN FETCH c.autor a "
								+ "LEFT JOIN FETCH c.proposicao p WHERE c.id = :entityId",
						Comentario.class);
		findByIdQuery.setParameter("entityId", id);
		List<Comentario> lista = findByIdQuery.getResultList();
		Comentario comentario = null;
		if (!Objects.isNull(lista) && !lista.isEmpty()) {
			comentario = lista.get(0);
		}
		Long idProposicao = Objects.isNull(comentario.getProposicao()) ? null
				: comentario.getProposicao().getId();
		return new ComentarioJSON(comentario.getId(),
				comentario.getDescricao(), comentario.getAutor(),
				comentario.getDataCriacao(), idProposicao);
	}

	@Override
	public void salvarComentario(ComentarioJSON comentarioJSON, Usuario autor) throws IllegalAccessException {
		if(comentarioJSON.getId()!=null){
			if(comentarioJSON.getAutor()!=null){
				if(!comentarioJSON.getAutor().equals(autor)){
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
