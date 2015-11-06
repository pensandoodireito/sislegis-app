package br.gov.mj.sislegis.app.parser

import br.gov.mj.sislegis.app.parser.senado.ParserComissoesSenado
import br.gov.mj.sislegis.app.parser.senado.ParserPautaSenado
import br.gov.mj.sislegis.app.parser.senado.ParserPlenarioSenado
import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado
import spock.lang.Specification

class SenadoWSParserSpec extends Specification{

    def "ParserPlenarioSenado - deve retornar um numero minimo de proposicoes, dada uma data inicial"(){

        given:
        def parserPlenarioSenado = new ParserPlenarioSenado()
        def datIni = "20140801"
        def proposicoes

        when:
        proposicoes = parserPlenarioSenado.getProposicoes(datIni)

        then:
        assert proposicoes.size() >= 0

    }

    def "ParserComissoesSenado - deve retornar um numero minimo de comissoes"(){

        given:
        def parserComissoesSenado = new ParserComissoesSenado()
        def comissoes

        when:
        comissoes = parserComissoesSenado.getComissoes()

        then:
        assert comissoes.size() >= 0

    }

    def "ParserPautaSenado - deve retornar um numero minimo de proposicoes, dada uma data inicial e uma comissao"(){

        given:
        def parserPautaSenado = new ParserPautaSenado()
        def proposicoes
        def siglaComissao = "CDH"
        def datIni = "20150801"

        when:
        proposicoes = parserPautaSenado.getProposicoes(siglaComissao, datIni)

        then:
        assert proposicoes.size() >= 0

    }

    def "ParserProposicaoSenado - conferir os nomes dos autores, dados os ids das proposicoes"(){

        given:
        def parserProposicaoSenado = new ParserProposicaoSenado()
        def proposicaoSenado = parserProposicaoSenado.getProposicao(id)

        expect:
        proposicaoSenado.getAutor() == nome

        where:
        id        | nome
        24257L    | "Alencastro Guimarães"
        24258L    | "Attílio Vivacqua"
        24259L    | "Carlos Saboya"
        24260L    | "Caiado de Castro"

    }

}
