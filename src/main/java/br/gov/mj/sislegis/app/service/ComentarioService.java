package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;

@Local
public interface ComentarioService extends Service<Comentario> {
	/**
	 * busca por id local da proposicao
	 * 
	 * @param id
	 * @return
	 */
	List<Comentario> findByProposicaoId(Long id);

	List<Comentario> findByProposicaoId(Long id, boolean incluiOcultos);

	/**
	 * busca por idProposicao (identificacao global que inclue os WSs) da
	 * proposicao
	 * 
	 * @param id
	 * @return
	 */
	List<Comentario> findByIdProposicao(Integer id);

	List<Comentario> findByIdProposicao(Integer id, boolean incluiOcultos);

	void salvarComentario(Comentario comentario, Usuario usuario) throws IllegalAccessException;

	Integer totalByProposicao(Long idProposicao);

	void ocultar(Long idComentario);

}
