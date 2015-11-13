package br.gov.mj.sislegis.app.enumerated;

import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;

/**
 * Representa as situacoes das pautas de reuniao da Camara
 */
public enum SituacaoCamara {
    Encerrada,
    Convocada,
    EmAndamento;

    public SituacaoSessao situacaoSessaoCorrespondente(){
        switch (this){
            case Encerrada:
                return SituacaoSessao.Realizada;
            case Convocada:
            case EmAndamento:
                return SituacaoSessao.Agendada;
            default:
                return SituacaoSessao.Desconhecido;
        }
    }
}
