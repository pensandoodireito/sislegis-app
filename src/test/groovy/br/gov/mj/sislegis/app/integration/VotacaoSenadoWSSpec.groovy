package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class VotacaoSenadoWSSpec extends Specification{

    def "deve listar a votacao de uma proposicao"(){

        given:
        def client = new RESTClient("http://legis.senado.leg.br")
        def codigoProposicao = 112464
        def caminho = "/dadosabertos/materia/votacoes/" + codigoProposicao
        def xml

        when:
        def response = client.get(path: caminho)
        xml = response.data

        then:
            xml.Materia.Votacoes.each{
                it.Votos.each{
                    println it.IdentificacaoParlamentar.NomeParlamentar
                }
            }

    }

}
