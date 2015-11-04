package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.model.PosicionamentoProposicao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.PosicionamentoProposicaoService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PosicionamentoProposicaoServiceEjb extends AbstractPersistence<PosicionamentoProposicao, Long> implements PosicionamentoProposicaoService{

    @PersistenceContext
    private EntityManager em;

    public PosicionamentoProposicaoServiceEjb() {
        super(PosicionamentoProposicao.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
