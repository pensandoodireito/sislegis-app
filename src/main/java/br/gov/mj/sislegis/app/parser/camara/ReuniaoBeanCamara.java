package br.gov.mj.sislegis.app.parser.camara;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;
import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;
import br.gov.mj.sislegis.app.parser.ReuniaoBean;

class ReuniaoBeanCamara extends ReuniaoBean {

	protected List<Proposicao> proposicoes = new ArrayList<Proposicao>();

	protected List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	protected enum Situacao {
		Encerrada, Convocada
	};

	@Override
	public Sessao getSessao() {
		Sessao sessao = new Sessao();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		try {
			sessao.setData(sdf.parse(data + " " + hora));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sessao.setIdentificadorExterno(getCodigo().toString());
		sessao.setTitulo(titulo);
		situacao = situacao.replace("(Final)", "").trim();
		switch (Situacao.valueOf(situacao)) {
		case Encerrada:
			sessao.setSituacao(SituacaoSessao.Realizada);
			break;
		case Convocada:
			sessao.setSituacao(SituacaoSessao.Agendada);
			break;
		

		default:
			break;
		}
		return sessao;
	}
}