package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.Resultado;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ResultadoService extends Service<Resultado> {

    List<Resultado> findByReuniaoProposicao(Long idReuniao, Long idProposicao);

}
