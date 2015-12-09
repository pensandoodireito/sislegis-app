package br.gov.mj.sislegis.app.enumerated;

import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;

/**
 * Representa as situacoes das pautas de reuniao do Senado
 */
public enum SituacaoSenado {
	Encerrada, Realizada, Agendada, Cancelada, Convocada, Adiada, Aberta, EmAndamento, Transferida;

	public SituacaoSessao situacaoSessaoCorrespondente() {
		switch (this) {
		case Encerrada:
		case Realizada:
			return SituacaoSessao.Realizada;
		case Agendada:
		case Adiada:
		case Convocada:
		case Aberta:
		case EmAndamento:
		case Transferida:
			return SituacaoSessao.Agendada;
		case Cancelada:
			return SituacaoSessao.Cancelada;
		default:
			return SituacaoSessao.Desconhecido;
		}
	}

}
