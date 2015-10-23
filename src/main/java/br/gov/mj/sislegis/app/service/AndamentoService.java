package br.gov.mj.sislegis.app.service;

import br.gov.mj.sislegis.app.model.Andamento;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AndamentoService extends Service<Andamento> {

    List<Andamento> findByIdProposicao(Long idProposicao);

}
