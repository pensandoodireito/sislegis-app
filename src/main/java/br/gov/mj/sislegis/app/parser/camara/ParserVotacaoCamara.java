package br.gov.mj.sislegis.app.parser.camara;

import br.gov.mj.sislegis.app.model.Votacao;

import java.util.ArrayList;
import java.util.List;

public class ParserVotacaoCamara {

    public List<Votacao> votacoesPorProposicao(Integer idProposicao, String ano, String tipo){
        List<Votacao> votacoes = new ArrayList<>();

        StringBuilder wsURL = new StringBuilder("http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ObterVotacaoProposicao?numero=1992&ano=2007&tipo=PL");


        return votacoes;
    }
}
