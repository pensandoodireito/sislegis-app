package br.gov.mj.sislegis.app.service;

import java.io.IOException;
import java.util.List;

import javax.ejb.Local;
import javax.servlet.http.Part;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;

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
