package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.model.Resultado;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ResultadoService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ResultadoServiceEjb extends AbstractPersistence<Resultado, Long> implements ResultadoService {

    @PersistenceContext
    private EntityManager em;

    public ResultadoServiceEjb() {
        super(Resultado.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public List<Resultado> findByReuniaoProposicao(Long idReuniao, Long idProposicao) {
        TypedQuery<Resultado> query = getEntityManager().createQuery(
                "SELECT r FROM Resultado r WHERE r.reuniaoProposicao.id.idReuniao = :idReuniao AND r.reuniaoProposicao.id.idProposicao = :idProposicao ORDER BY r.dataCriacao DESC", Resultado.class);
        query.setParameter("idReuniao", idReuniao);
        query.setParameter("idProposicao", idProposicao);

        return query.getResultList();
    }
}
