package br.gov.mj.sislegis.app.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.json.ComentarioJSON;
import br.gov.mj.sislegis.app.model.Comentario;
import br.gov.mj.sislegis.app.model.Usuario;

@Local
public interface ComentarioService extends Service<Comentario> {

	List<ComentarioJSON> findByProposicao(Long id);

	List<ComentarioJSON> findByProposicao(Long idProposicao, Integer posicaoInicial, Integer limite);

	BigInteger totalByProposicao(Long idProposicao);

	void salvarComentario(ComentarioJSON comentarioJSON, Usuario usuario) throws IllegalAccessException;

	ComentarioJSON findByIdJSON(Long id);

}
