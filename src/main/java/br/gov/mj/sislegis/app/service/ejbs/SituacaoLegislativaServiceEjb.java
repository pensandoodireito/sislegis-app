package br.gov.mj.sislegis.app.service.ejbs;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.SituacaoLegislativa;
import br.gov.mj.sislegis.app.model.Usuario;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.SituacaoLegislativaService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Stateless
public class SituacaoLegislativaServiceEjb extends AbstractPersistence<SituacaoLegislativa, Long> implements
		SituacaoLegislativaService {
	@PersistenceContext
	private EntityManager em;

	public SituacaoLegislativaServiceEjb() {
		super(SituacaoLegislativa.class);
	}

	@Override
	public SituacaoLegislativa getSituacao(Origem casa, Long idExterno) {
		try {
			SituacaoLegislativa situacao = (SituacaoLegislativa) em.createNamedQuery("findByIdExterno")
					.setParameter("casa", casa).setParameter("idExterno", idExterno).getSingleResult();

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
		return em.createNamedQuery("findByOrigem").setParameter("casa", origem).getResultList();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public void updateObsoletos() {

	}

	@Override
	public Long save(SituacaoLegislativa sit, Usuario usuario) {
		sit.setAtualizadoPor(usuario);
		sit.setAtualizadaEm(new Date());
		save(sit);
		return sit.getId().longValue();
	}

}
