package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class VotacaoCamaraWSSpec extends Specification {

    // FIXME a chamada do metodo abaixo retorna o erro: Forbidden

    def "deve listar a votacao de uma proposicao"(){

        given:
        def client = new RESTClient("http://www.camara.gov.br")
        def tipo = "PL"
        def numero = "1992"
        def ano = 2007
        def caminho = "/SitCamaraWS/Proposicoes.asmx/ObterVotacaoProposicao"
        def parametros = [tipo: tipo, numero: numero, ano: ano]
        def xml

        when:
        def response = client.get(path: caminho, query: parametros)
        xml = response.data

        then:
        println xml

    }

}
