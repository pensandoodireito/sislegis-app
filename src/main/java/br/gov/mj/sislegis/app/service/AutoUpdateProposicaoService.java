package br.gov.mj.sislegis.app.service;

/**
 * Serviço de atualizacao de proposições
 * 
 * @author rafael.coutinho
 *
 */
public interface AutoUpdateProposicaoService {

	void atualizaProposicoesDaReuniao();

	void atualizaPautaReuniaoEProposicoes();

	void atualizaPautadasSenado();

	void updatePautasCamara();

	void updatePautasSenado();

	void atualizaPautadasCamara();
}
