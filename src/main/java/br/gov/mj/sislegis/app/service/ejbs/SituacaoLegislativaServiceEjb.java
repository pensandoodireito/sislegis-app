package br.gov.mj.sislegis.app.service.ejbs;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.SituacaoLegislativa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.parser.SituacaoParser;
import br.gov.mj.sislegis.app.parser.camara.ParserSituacaoCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserSituacaoSenado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.SituacaoLegislativaService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class SituacaoLegislativaServiceEjb extends AbstractPersistence<SituacaoLegislativa, Long> implements
		SituacaoLegislativaService, EJBUnitTestable {
	@PersistenceContext
	private EntityManager em;

	public SituacaoLegislativaServiceEjb() {
		super(SituacaoLegislativa.class);
	}

	@Override
	public SituacaoLegislativa getSituacao(Origem casa, Long idExterno) {
		try {
			SituacaoLegislativa situacao = (SituacaoLegislativa) em.createNamedQuery("findByIdExterno")
					.setParameter("origem", casa).setParameter("idExterno", idExterno).getSingleResult();

			return situacao;
		} catch (javax.persistence.NoResultException e) {
			return null;
		} catch (Exception e) {

			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao buscar situacao", e);
			return null;
		}

	}

	@Override
	public List<SituacaoLegislativa> listSituacoes(Origem origem) {
		return em.createNamedQuery("findByOrigem").setParameter("origem", origem).getResultList();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Long save(SituacaoLegislativa sit, Usuario usuario) {
		sit.setAtualizadoPor(usuario);
		save(sit);
		return sit.getId().longValue();
	}

	@Override
	// @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SituacaoLegislativa save(SituacaoLegislativa e) {
		e.setAtualizadaEm(new Date());
		return super.save(e);
	}

	@Override
	public void updateSituacoes() {

		try {
			SituacaoParser camara = new ParserSituacaoCamara();
			Collection<SituacaoLegislativa> sit = camara.listSituacoes();
			persistList(sit);
			persistList(new ParserSituacaoSenado().listSituacoes());

		} catch (IOException e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao buscar situacao do ws", e);
		}
	}

	private void persistList(Collection<SituacaoLegislativa> sit) {
		// EntityTransaction et = em.getTransaction();
		try {

			for (Iterator<SituacaoLegislativa> iterator = sit.iterator(); iterator.hasNext();) {
				SituacaoLegislativa situacaoLegislativa = (SituacaoLegislativa) iterator.next();
				SituacaoLegislativa sitDb = getSituacao(situacaoLegislativa.getOrigem(),
						situacaoLegislativa.getIdExterno());

				if (sitDb == null) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINEST,
							"Criando nova SituacaoLegislativa " + situacaoLegislativa);
					save(situacaoLegislativa);
				} else {
					if (situacaoLegislativa.getObsoleta() && !sitDb.getObsoleta()) {
						Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINEST,
								"Atualizando SituacaoLegislativa obsoleta " + situacaoLegislativa);
						sitDb.setObsoleta(true);
						save(sitDb);
					}
					// et.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			throw e;
		}
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		em = (EntityManager) injections[0];

	}

}
