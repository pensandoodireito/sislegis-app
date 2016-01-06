package br.gov.mj.sislegis.app.parser

import br.gov.mj.sislegis.app.parser.senado.ParserVotacaoSenado
import spock.lang.Specification

class VotacaoSenadoParserSpec extends Specification{

    def "deve retornar as votacoes de uma proposicao"(){
        given:
        def parserVotacaoSenado = new ParserVotacaoSenado()
        def idProposicao = 112464
        def votacoes

        when:
        votacoes = parserVotacaoSenado.votacoesPorProposicao(idProposicao)

        then:
        votacoes.each{
            println it.data
            println it.resultado
        }
    }
}
