package br.gov.mj.sislegis.app.parser

import br.gov.mj.sislegis.app.parser.senado.ParserProposicaoSenado
import org.junit.Assert
import spock.lang.Specification

class SenadoWSParserSpec extends Specification{

    def "deve garantir que o autor da proposicao retornada nao e nulo"(){

        def parserProposicaoSenado = new ParserProposicaoSenado()

        given:
        Long idProposicao = 24257L

        when:
        proposicaoSenado = parserProposicaoSenado.getProposicao(idProposicao)

        then:
        Assert.assertNotNull("Autor nulo", proposicaoSenado.getAutor())

    }

    def "length of Spock's and his friends' names"() {
        expect:
        name.size() == length

        where:
        name     | length
        "Spock"  | 4
        "Kirk"   | 4
        "Scotty" | 6
    }

}
