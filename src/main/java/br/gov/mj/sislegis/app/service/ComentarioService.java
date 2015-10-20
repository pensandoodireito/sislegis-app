package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;

@Local
public interface ComentarioService extends Service<Comentario> {
	public List<ComentarioJSON> findByProposicao(Long id);

	public List<Comentario> findByIdProposicao(Long id);

	public void salvarComentario(ComentarioJSON comentarioJSON, Usuario usuario) throws IllegalAccessException;

	public void salvarComentario(Comentario comentario, Usuario usuario) throws IllegalAccessException;

	public ComentarioJSON findByIdJSON(Long id);

}
