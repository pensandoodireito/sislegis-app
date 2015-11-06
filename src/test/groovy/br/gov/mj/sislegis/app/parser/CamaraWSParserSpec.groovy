package br.gov.mj.sislegis.app.parser

import br.gov.mj.sislegis.app.parser.camara.ParserComissoesCamara
import br.gov.mj.sislegis.app.parser.camara.ParserPautaCamara
import br.gov.mj.sislegis.app.parser.camara.ParserProposicaoCamara
import spock.lang.Specification

class CamaraWSParserSpec extends Specification{

    def "ParserComissoesCamara - deve retornar uma quantidade minima de comissoes"(){

        given:
        def parserComissoesCamara = new ParserComissoesCamara()
        def comissoes

        when:
        comissoes = parserComissoesCamara.getComissoes()

        then:
        comissoes.size() >= 84

    }

    def "ParserPautaCamara - deve retornar n proposicoes para determinada comissao (por id) dentro de um intervalo de tempo"(){

        given:
        def parserPautaCamara = new ParserPautaCamara()
        def proposicoes
        def idComissao = 2003L
        def datIni = "20140702"
        def datFim = "20140702"

        when:
        proposicoes = parserPautaCamara.getProposicoes(idComissao, datIni, datFim)

        then:
        proposicoes.size() == 35

    }

    def "ParserProposicaoCamara - validar os dados retornados da proposicao retornada"(){

        given:
        def parserProposicaoCamara = new ParserProposicaoCamara()
        def proposicao
        def idProposicao = 562039L

        when:
        proposicao = parserProposicaoCamara.getProposicao(idProposicao)

        then:
        proposicao.getAutor() == "PRESIDÊNCIA DA CÂMARA DOS DEPUTADOS"
        proposicao.getAno() == "2012"

    }
}
