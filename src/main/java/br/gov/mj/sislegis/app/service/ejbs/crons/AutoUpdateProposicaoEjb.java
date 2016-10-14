package br.gov.mj.sislegis.app.service.ejbs.crons;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.service.AutoUpdateProposicaoService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.service.ejbs.EJBUnitTestable;
import br.gov.mj.sislegis.app.util.SislegisUtil;

/**
 * Executa atualizaca de proposicao em datas definidas
 * 
 * @author rafael.coutinho
 *
 */
@Stateless
public class AutoUpdateProposicaoEjb implements AutoUpdateProposicaoService, EJBUnitTestable {
	@Inject
	ProposicaoService proposicaoService;

	@Inject
	UsuarioService usuarioService;
	@Inject
	private ComissaoService comissaoService;

	@Override
	@Schedule(dayOfWeek = "*", hour = "3", persistent = false, info = "Atualiza proposicoes da reuniao")
	public void atualizaProposicoesDaReuniao() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualiza proposicoes da reuniao de hoje");
		Collection<Proposicao> proposicoesReuniao = proposicaoService.buscarProposicoesPorDataReuniao(new Date());
		if (proposicoesReuniao.size() == 0) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Nenhuma proposicao para a reunião de hoje ");
			return;
		}
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Ha " + proposicoesReuniao.size() + " a checar");
		for (Iterator<Proposicao> iterator = proposicoesReuniao.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			try {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualizando " + proposicao);
				if (proposicaoService.syncDadosProposicao(proposicao)) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Dados basicos alterados");
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Dados basicos sem alteracao");
				}
				if (proposicaoService.syncDadosPautaProposicao(proposicao)) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Dados pauta alterados ");
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Dados pauta sem alteracao");
				}
			} catch (IOException e) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao atualizar proposicao " + proposicao.getSigla(), e);
			}
		}
	}

	@Override
	@Schedule(dayOfWeek = "*", hour = "4", persistent = false, info = "Atualiza pautas das reunioes passadas e suas proposicoes")
	public void atualizaPautaReuniaoEProposicoes() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualiza pautas das reunioes anteriores e suas proposicoes");

		List<PautaReuniaoComissao> prcLocalList = proposicaoService.findPautaReuniaoPendentes();

		for (PautaReuniaoComissao prcLocal : prcLocalList) {
			try {
				if (proposicaoService.syncDadosPautaReuniaoComissao(prcLocal)) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Dados da pauta Reuniao e/ou Proposicao alterados");
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).finest("Dados pauta Reuniao e/ou Proposicao sem alteracao");
				}

			} catch (IOException e) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao atualizar pauta reuniao e/ou proposicao " + prcLocal.getTitulo(), e);
			}
		}

	}

	@Override
	@Schedule(dayOfWeek = "*", hour = "6", minute = "00", persistent = false, info = "Atualiza todas as proposicoes pautadas do senado")
	public void atualizaPautadasSenado() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualiza pautas das reunioes anteriores e suas proposicoes do senado");

		updatePautasSenado();

	}

	@Override
	@Schedule(dayOfWeek = "*", hour = "5", minute = "30", persistent = false, info = "Atualiza todas as proposicoes pautadas do camara")
	public void atualizaPautadasCamara() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine("Atualiza pautas das reunioes anteriores e suas proposicoes da camara");

		updatePautasCamara();

	}

	@Override
	public void updatePautasCamara() {
		List<Comissao> ls;
		try {
			ls = comissaoService.listarComissoesCamara();

			for (Iterator<Comissao> iterator = ls.iterator(); iterator.hasNext();) {
				Comissao comissao = (Comissao) iterator.next();
				System.out.println("Comissao " + comissao.getSigla());
				proposicaoService.syncPautaAtualComissao(Origem.CAMARA, comissao);

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void updatePautasSenado() {
		try {
			List<Comissao> ls = comissaoService.listarComissoesSenado();
			for (Iterator<Comissao> iterator = ls.iterator(); iterator.hasNext();) {
				Comissao comissao = (Comissao) iterator.next();

				System.out.println("Comissao " + comissao.getSigla());
				try {
					proposicaoService.syncPautaAtualComissao(Origem.SENADO, comissao);

				} catch (Exception e) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao processo comissao " + comissao, e);
				}
			}
		} catch (Exception e1) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falhou ao processar pautas senado ", e1);
		}
	}

	@Override
	public void setInjectedEntities(Object... injections) {
		usuarioService = (UsuarioService) injections[0];
		proposicaoService = (ProposicaoService) injections[1];
		comissaoService = (ComissaoService) injections[2];
	}

}
