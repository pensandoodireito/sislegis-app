package br.gov.mj.sislegis.app.service;

import java.io.IOException;

import javax.ejb.Local;
import javax.servlet.http.Part;

import br.gov.mj.sislegis.app.model.Documento;

/**
 * Servico para gerenciar Documento
 * 
 * @author rafaelcoutinho
 *
 */
@Local
public interface DocumentoService extends Service<Documento> {

	Documento save(Documento documento, Part filePart) throws IOException;

}
