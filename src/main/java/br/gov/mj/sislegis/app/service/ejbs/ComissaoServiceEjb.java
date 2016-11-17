package br.gov.mj.sislegis.app.service.ejbs;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.parser.camara.ParserComissoesCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserComissoesSenado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EJBDataCacher;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class ComissaoServiceEjb extends AbstractPersistence<Comissao, Long> implements ComissaoService, EJBUnitTestable {

	private static final String CACHE_KEY_COMISSOES_CAMARA = "COMISSOES_CAMARA";
	private static final String CACHE_KEY_COMISSOES_SENADO = "COMISSOES_SENADO";

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ParserComissoesSenado parserComissoesSenado;

	@Inject
	private ParserComissoesCamara parserComissoesCamara;

	@Inject
	private EJBDataCacher dataCacher;

	public ComissaoServiceEjb() {
		super(Comissao.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<Comissao> listarComissoesCamara() throws Exception {
		if (!dataCacher.isEntityCached(CACHE_KEY_COMISSOES_CAMARA)) {
			dataCacher.updateDataCache(CACHE_KEY_COMISSOES_CAMARA, parserComissoesCamara.getComissoes());
		}
		return (List<Comissao>) dataCacher.getReferenceData(CACHE_KEY_COMISSOES_CAMARA);
	}

	@Override
	public List<Comissao> listarComissoesSenado() throws Exception {
		if (!dataCacher.isEntityCached(CACHE_KEY_COMISSOES_SENADO)) {
			dataCacher.updateDataCache(CACHE_KEY_COMISSOES_SENADO, parserComissoesSenado.getComissoes());
		}
		return (List<Comissao>) dataCacher.getReferenceData(CACHE_KEY_COMISSOES_SENADO);
	}

	@Override
	public Comissao getBySigla(String sigla) throws Exception {
		if (sigla != null && sigla.indexOf("-") > 0) {
			sigla = sigla.substring(0, sigla.indexOf("-")).trim();
		}

		for (Iterator<Comissao> iterator = listarComissoesCamara().iterator(); iterator.hasNext();) {
			Comissao c = iterator.next();
			if (c.getSigla().trim().equals(sigla)) {
				return c;
			}
		}
		return null;

	}

	@Override
	public String getComissaoCamara(Long idComissao) throws Exception {
		if (idComissao == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Idcomissao invalido em getComissaoCamara");
			return null;
		}
		for (Iterator<Comissao> iterator = listarComissoesCamara().iterator(); iterator.hasNext();) {
			Comissao c = iterator.next();
			if (idComissao.equals(c.getId())) {
				return c.getSigla();
			}
		}
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Não encontrou comissão da camara baseado no ID " + idComissao);
		return null;
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		this.em = (EntityManager) injections[0];
		this.dataCacher = (EJBDataCacher) injections[1];
		parserComissoesSenado = new ParserComissoesSenado();
		parserComissoesCamara = new ParserComissoesCamara();

	}

	@Override
	public Comissao getComissao(Origem origem, String sigla) throws Exception {
		String comissaoPorExtenso = sigla.trim().toLowerCase();
		List<Comissao> lista = null;
		if (Origem.CAMARA.equals(origem)) {
			lista = listarComissoesCamara();

		} else {
			lista = listarComissoesSenado();
		}
		for (Iterator<Comissao> iterator = lista.iterator(); iterator.hasNext();) {
			Comissao type = (Comissao) iterator.next();
			if (type.getSigla().trim().toLowerCase().equals(comissaoPorExtenso)) {
				return type;
			}

		}
		return null;
	}
}
