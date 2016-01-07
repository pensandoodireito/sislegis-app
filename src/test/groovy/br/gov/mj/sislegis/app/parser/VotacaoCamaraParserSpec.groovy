package br.gov.mj.sislegis.app.parser

import br.gov.mj.sislegis.app.parser.camara.ParserVotacaoCamara
import spock.lang.Specification

class VotacaoCamaraParserSpec extends Specification{

    def "deve retornar as votacoes de uma proposicao"(){
        given:
        def parserVotacaoCamara = new ParserVotacaoCamara()
        def tipo = "PL"
        def numero = "1992"
        def ano = "2007"
        def votacoes

        when:
        votacoes = parserVotacaoCamara.votacoesPorProposicao(numero, ano, tipo)

        then:
        votacoes.each{
            assert it.data != null
        }
    }
}
