package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.EtapaRoadmapComissao;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EtapaRoadmapComissaoService extends Service<EtapaRoadmapComissao> {

    EtapaRoadmapComissao inserir(Long idProposicao, Long idComissao);

    void reordenar(List<EtapaRoadmapComissao> etapasReordenadas);

    void remover(Long idEtapa);

}
