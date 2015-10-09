package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.model.Andamento;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.AndamentoService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AndamentoServiceEjb extends AbstractPersistence<Andamento, Long> implements AndamentoService {

    @PersistenceContext
    private EntityManager em;

    public AndamentoServiceEjb() {
        super(Andamento.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public List<Andamento> findByIdProposicao(Long idProposicao) {
        TypedQuery<Andamento> query = getEntityManager().createQuery(
                "SELECT a FROM Andamento a WHERE a.proposicao.id = :idProposicao ORDER BY a.dataHora", Andamento.class);
        query.setParameter("idProposicao", idProposicao);

        return query.getResultList();
    }
}
