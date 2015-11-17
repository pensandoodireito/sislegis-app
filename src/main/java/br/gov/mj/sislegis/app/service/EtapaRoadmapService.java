package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.EtapaRoadmap;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EtapaRoadmapService extends Service<EtapaRoadmap> {

    EtapaRoadmap inserir(Long idComissao, Long idProposicao);

    List<EtapaRoadmap> listarPorProposicao(Long idProposicao);

    void reordenar(List<EtapaRoadmap> etapasRoadmap);

    void remover(Long idEtapaRoadmap);

}
