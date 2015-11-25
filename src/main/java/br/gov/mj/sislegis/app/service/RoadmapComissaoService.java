package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.RoadmapComissao;

import javax.ejb.Local;
import java.util.List;

@Local
public interface RoadmapComissaoService extends Service<RoadmapComissao> {

    RoadmapComissao inserir(Long idComissao, Long idProposicao);

    List<RoadmapComissao> listarPorProposicao(Long idProposicao);

    void reordenar(List<RoadmapComissao> etapasRoadmap);

    void remover(Long idEtapaRoadmap);

}
