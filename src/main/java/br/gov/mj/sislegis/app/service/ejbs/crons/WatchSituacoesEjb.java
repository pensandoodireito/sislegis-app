package br.gov.mj.sislegis.app.service.ejbs.crons;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import br.gov.mj.sislegis.app.service.SituacaoLegislativaService;
import br.gov.mj.sislegis.app.service.WatchSituacoesService;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@Singleton
public class WatchSituacoesEjb implements WatchSituacoesService {

	@Inject
	SituacaoLegislativaService service;

	@Override
	@Schedule(second = "24", minute = "*", hour = "*", persistent = false, info = "Atualiza situacoes")
	public void atualizaSituacoes() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualizando situacoes");
		service.updateSituacoes();
	}

}
