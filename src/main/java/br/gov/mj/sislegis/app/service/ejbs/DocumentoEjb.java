package br.gov.mj.sislegis.app.service.ejbs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;

import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.DocumentoService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class DocumentoEjb extends AbstractPersistence<Documento, Long> implements DocumentoService {
	private static final String HOME_SISLEGIS_FILES = "/home/sislegis/files";
	@PersistenceContext
	private EntityManager em;

	public DocumentoEjb() {
		super(Documento.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void remove(Documento entity) {
		try {
			new File(entity.getPath()).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.remove(entity);
	}

	@Override
	public Documento save(Documento documento, Part filePart) throws IOException {
		save(documento);

		File doc = new File(HOME_SISLEGIS_FILES, documento.getId() + "_" + documento.getNome());
		filePart.write(doc.getAbsolutePath());
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).info("Arquivo criado em " + doc.getAbsolutePath());
		documento.setPath(doc.getAbsolutePath());
		save(documento);
		return documento;
	}
}
