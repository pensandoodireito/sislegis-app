package br.gov.mj.sislegis.app.service.ejbs;

import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.EtapaRoadmapComissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.service.AbstractPersistence;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.EtapaRoadmapComissaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EtapaRoadmapComissaoServiceEjb extends AbstractPersistence<EtapaRoadmapComissao, Long> implements EtapaRoadmapComissaoService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ProposicaoService proposicaoService;

    @Inject
    private ComissaoService comissaoService;

    public EtapaRoadmapComissaoServiceEjb() {
        super(EtapaRoadmapComissao.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public EtapaRoadmapComissao inserir(Long idProposicao, Long idComissao) {
        Proposicao proposicao = proposicaoService.findById(idProposicao);
        Comissao comissao = comissaoService.findById(idComissao);

        if (proposicao != null && comissao != null) {
            EtapaRoadmapComissao etapaRoadmapComissao = new EtapaRoadmapComissao();
            etapaRoadmapComissao.setProposicao(proposicao);
            etapaRoadmapComissao.setComissao(comissao);

            EtapaRoadmapComissao ultimaEtapa = buscarUltimaEtapa(idProposicao);
            Integer proximaOrdem = ultimaEtapa == null ? 1 : ultimaEtapa.getOrdem() + 1;
            etapaRoadmapComissao.setOrdem(proximaOrdem);

            em.persist(etapaRoadmapComissao);
            return etapaRoadmapComissao;

        } else {
            Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
                    "Tentativa de inserir roadmap para comissao ou proposicao nao encontrada: Comissao: " + idComissao + " Proposicao: " + idProposicao);
            return null;
        }
    }

    @Override
    public void reordenar(List<EtapaRoadmapComissao> etapasReordenadas) {

        for (EtapaRoadmapComissao etapaRoadmapComissao : etapasReordenadas){
            Query query = em.createQuery("UPDATE EtapaRoadmapComissao SET ordem = :ordem WHERE id = :id ");
            query.setParameter("id", etapaRoadmapComissao.getId());
            query.setParameter("ordem", etapaRoadmapComissao.getOrdem());
            query.executeUpdate();
        }

    }

    @Override
    public void remover(Long idEtapa) {
        Query query = em.createQuery("DELETE FROM EtapaRoadmapComissao WHERE id = :id ");
        query.setParameter("id", idEtapa);
        query.executeUpdate();
    }

    private EtapaRoadmapComissao buscarUltimaEtapa(Long idProposicao) {
        try {
            TypedQuery<EtapaRoadmapComissao> query = em.createQuery("FROM EtapaRoadmapComissao WHERE proposicao.id = :idProposicao ORDER BY ordem DESC ", EtapaRoadmapComissao.class);

            query.setParameter("idProposicao", idProposicao);
            query.setMaxResults(1);

            return query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE,
                    "Nenhuma etapa encontrada no roadmap, para a proposicao id: " + idProposicao);
            return null;
        }
    }
}
