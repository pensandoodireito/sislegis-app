package br.gov.mj.sislegis.app.service.ejbs.crons;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara;
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado;
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado;
import br.gov.mj.sislegis.app.service.AutoUpdateProposicaoService;
import br.gov.mj.sislegis.app.service.ComissaoService;
import br.gov.mj.sislegis.app.service.ProposicaoService;
import br.gov.mj.sislegis.app.service.UsuarioService;
import br.gov.mj.sislegis.app.util.Conversores;
import br.gov.mj.sislegis.app.util.SislegisUtil;

/**
 * Executa atualizaca de proposicao em datas definidas
 * 
 * @author rafael.coutinho
 *
 */
@Singleton
public class AutoUpdateProposicaoEjb implements AutoUpdateProposicaoService {
	@Inject
	ProposicaoService proposicaoService;

	@Inject
	UsuarioService usuarioService;

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
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
						"Falhou ao atualizar proposicao " + proposicao.getSigla(), e);
			}
		}
	}

	@Override
	@Schedule(dayOfWeek = "*", hour = "4", persistent = false, info = "Atualiza pautas das reunioes passadas e suas proposicoes")
	public void atualizaPautaReuniaoEProposicoes() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
				"Atualiza pautas das reunioes anteriores e suas proposicoes");

		List<PautaReuniaoComissao> prcLocalList = proposicaoService.findPautaReuniaoPendentes();

		for (PautaReuniaoComissao prcLocal : prcLocalList) {
			try {
				if (proposicaoService.syncDadosPautaReuniaoComissao(prcLocal)) {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
							"Dados da pauta Reuniao e/ou Proposicao alterados");
				} else {
					Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).finest(
							"Dados pauta Reuniao e/ou Proposicao sem alteracao");
				}

			} catch (IOException e) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE,
						"Falhou ao atualizar pauta reuniao e/ou proposicao " + prcLocal.getTitulo(), e);
			}
		}

	}

	@Inject
	private ComissaoService comissaoService;

	@Override
	@Schedule(dayOfWeek = "*", hour = "6", persistent = false, info = "Atualiza todas as proposicoes pautadas")
	public void atualizaPautadas() {
		Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).fine(
				"Atualiza pautas das reunioes anteriores e suas proposicoes");

		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.add(Calendar.WEEK_OF_YEAR, 1);
		List<Comissao> ls;
		try {
			ls = comissaoService.listarComissoesCamara();

			for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
				Comissao comissao = (Comissao) iterator.next();
				System.out.println("Comissao " + comissao.getSigla());
				try {
					ParserPautaCamara parserPautaCamara = new ParserPautaCamara();

					Set<PautaReuniaoComissao> pss = parserPautaCamara.getPautaComissao(comissao.getSigla(),
							comissao.getId(), Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"),
							Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
					// proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
					// dataInicial.getTime(), dataFinal.getTime());
					for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
						PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
						System.out.println(object + " " + object.getData());
						SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
						for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3
								.hasNext();) {
							ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
							Proposicao p = proposicaoService.findProposicaoBy(Origem.CAMARA, propPauta.getProposicao()
									.getIdProposicao());
							System.out.println("\t" + propPauta + " === " + p);
							if (p != null) {
								paraSalvar.add(propPauta);
							}

						}
						if (!paraSalvar.isEmpty()) {
							object.setProposicoesDaPauta(paraSalvar);
							proposicaoService.savePautaReuniaoComissao(object);
						}

					}
				} catch (Exception e) {
					System.err
							.println("Falhou ao buscar para a comissao " + comissao.getSigla() + " " + e.getMessage());
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ls = comissaoService.listarComissoesSenado();
			for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
				Comissao comissao = (Comissao) iterator.next();
				System.out.println("Comissao " + comissao.getSigla());
				try {
					ParserPautaSenado parserSenado = new ParserPautaSenado();
					ParserPlenarioSenado parserPlenarioSenado = new ParserPlenarioSenado();
					Set<PautaReuniaoComissao> pss = null;
					if (comissao.getSigla().equals("PLEN")) {
						pss = parserPlenarioSenado.getProposicoes(Conversores.dateToString(dataInicial.getTime(),
								"yyyyMMdd"));
					} else {
						pss = parserSenado.getPautaComissao(comissao.getSigla(),
								Conversores.dateToString(dataInicial.getTime(), "yyyyMMdd"),
								Conversores.dateToString(dataFinal.getTime(), "yyyyMMdd"));
					}
					// proposicaoService.buscarProposicoesPautaCamaraWS(comissao.getId(),
					// dataInicial.getTime(), dataFinal.getTime());
					for (Iterator<PautaReuniaoComissao> iterator2 = pss.iterator(); iterator2.hasNext();) {
						PautaReuniaoComissao object = (PautaReuniaoComissao) iterator2.next();
						System.out.println(object + " " + object.getData());
						SortedSet<ProposicaoPautaComissao> paraSalvar = new TreeSet<ProposicaoPautaComissao>();
						for (Iterator<ProposicaoPautaComissao> iterator3 = object.getProposicoesDaPauta().iterator(); iterator3
								.hasNext();) {
							ProposicaoPautaComissao propPauta = (ProposicaoPautaComissao) iterator3.next();
							Proposicao p = proposicaoService.findProposicaoBy(Origem.SENADO, propPauta.getProposicao()
									.getIdProposicao());
							System.out.println("\t" + propPauta + " === " + p);
							if (p != null) {
								paraSalvar.add(propPauta);
							}

						}
						if (!paraSalvar.isEmpty()) {
							// EntityTransaction trans = em.getTransaction();
							// trans.begin();
							object.setProposicoesDaPauta(paraSalvar);
							proposicaoService.savePautaReuniaoComissao(object);
							// trans.commit();
						}

					}
				} catch (Exception e) {
					System.err
							.println("Falhou ao buscar para a comissao " + comissao.getSigla() + " " + e.getMessage());
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
