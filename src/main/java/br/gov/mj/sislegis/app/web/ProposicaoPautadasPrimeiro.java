package br.gov.mj.sislegis.app.web;

import java.util.Comparator;

import br.gov.mj.sislegis.app.model.Proposicao;

public class ProposicaoPautadasPrimeiro implements Comparator<Proposicao> {
	@Override
	public int compare(Proposicao o1, Proposicao o2) {
		if (o1.getPautaComissaoAtual() != null) {
			if (o2.getPautaComissaoAtual() == null) {
				return -1;
			} else {
				int compComissao = o1.getPautaComissaoAtual().getPautaReuniaoComissao().getComissao()
						.compareTo(o2.getPautaComissaoAtual().getPautaReuniaoComissao().getComissao());
				if (compComissao == 0) {
					return o1.getPautaComissaoAtual().getOrdemPauta()
							.compareTo(o2.getPautaComissaoAtual().getOrdemPauta());
				} else {
					return compComissao;
				}

			}

		} else if (o2.getPautaComissaoAtual() != null) {
			return 1;
		} else {
			if (o1.getComissao() == null) {
				if (o2.getComissao() == null) {
					return 0;
				} else {
					return 1;
				}
			} else if (o2.getComissao() == null) {
				return -1;
			} else {
				return o1.getComissao().compareTo(o2.getComissao());
			}
		}

	}
}