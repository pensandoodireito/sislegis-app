package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ComentarioService extends Service<Comentario> {

	List<ComentarioJSON> findByProposicao(Long id);

	List<ComentarioJSON> findByProposicao(Long idProposicao, Integer posicaoInicial, Integer limite);

	Long totalByProposicao(Long idProposicao);

	void salvarComentario(ComentarioJSON comentarioJSON, Usuario usuario) throws IllegalAccessException;

	ComentarioJSON findByIdJSON(Long id);

}
