package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;

@Local
public interface ComentarioService extends Service<Comentario> {

	public List<Comentario> findByIdProposicao(Long id);


	public void salvarComentario(Comentario comentario, Usuario usuario) throws IllegalAccessException;


}
