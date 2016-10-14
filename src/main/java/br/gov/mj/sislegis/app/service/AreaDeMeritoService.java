package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.AreaDeMerito;
import br.gov.mj.sislegis.app.model.AreaDeMeritoRevisao;

@Local
public interface AreaDeMeritoService extends Service<AreaDeMerito> {

	public List<AreaDeMeritoRevisao> listRevisoesProposicao(Long idProposicao);

	public List<AreaDeMeritoRevisao> listRevisoes(Long idArea, boolean pendentes);

	public AreaDeMeritoRevisao saveRevisao(AreaDeMeritoRevisao entity);
	public AreaDeMerito saveAreaDeMerito(AreaDeMerito entity);

	public AreaDeMeritoRevisao findRevisao(Long idRevisao);

	public void deleteRevisao(AreaDeMeritoRevisao rev);

	public void deleteRevisao(Long idRevisao);

}
