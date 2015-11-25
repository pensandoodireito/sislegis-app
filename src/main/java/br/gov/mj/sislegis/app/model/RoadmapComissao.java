package br.gov.mj.sislegis.app.model;

/**
 * Etapa do Roadmap (roteiro) de Comissoes por onde uma Proposicao deve tramitar
 */

public class RoadmapComissao {

    private Long id;

    private Proposicao proposicao;

    private Comissao comissao;

    private Integer ordem;

}