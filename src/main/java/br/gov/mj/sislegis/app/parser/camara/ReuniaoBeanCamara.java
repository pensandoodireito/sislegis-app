package br.gov.mj.sislegis.app.parser.camara;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.SituacaoCamara;
import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;
import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;
import br.gov.mj.sislegis.app.parser.ReuniaoBean;
import br.gov.mj.sislegis.app.util.SislegisUtil;

class ReuniaoBeanCamara extends ReuniaoBean {

	protected List<ProposicaoPautaComissaoWrapper> proposicoes = new ArrayList<ProposicaoPautaComissaoWrapper>();

	protected List<ProposicaoPautaComissaoWrapper> getPautaProposicoes() {
		return proposicoes;
	}

	@Override
	public Sessao getSessao() {
		Sessao sessao = new Sessao();
		try {
			sessao.setData(getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sessao.setIdentificadorExterno(getCodigo().toString());
		sessao.setTitulo(titulo);
		situacao = situacao.replace("(Final)", "").trim();
		situacao = situacao.replace("(Comunicado)", "").trim();
		situacao = situacao.replace("(Termo)", "").trim();

		try {
			sessao.setSituacao(SituacaoCamara.valueOf(situacao).situacaoSessaoCorrespondente());

		} catch (IllegalArgumentException e) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.SEVERE, "Falha ao converter a situacao da Camara: " + situacao, e);
			sessao.setSituacao(SituacaoSessao.Desconhecido);
		}

		return sessao;
	}

	public Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		return sdf.parse(data + " " + hora);
	}
}