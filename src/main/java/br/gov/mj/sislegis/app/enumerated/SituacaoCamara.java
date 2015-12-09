package br.gov.mj.sislegis.app.enumerated;

import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;

/**
 * Representa as situacoes das pautas de reuniao da Camara
 */
public enum SituacaoCamara {
    Encerrada,
    Convocada,
    Cancelada,
    EmAndamento;

    public SituacaoSessao situacaoSessaoCorrespondente(){
        switch (this){
            case Encerrada:
                return SituacaoSessao.Realizada;
            case Convocada:
            case EmAndamento:
                return SituacaoSessao.Agendada;
            case Cancelada:
            	return SituacaoSessao.Cancelada;
            default:
                return SituacaoSessao.Desconhecido;
        }
    }
}
