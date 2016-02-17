package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComissaoEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve listar todas as comissoes da camara"(){
        def caminho = "sislegis/rest/comissaos/comissoesCamara"

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
        response.data.each{
            println it
        }
        println "Total: " + response.data.size()
    }

    def "deve listar todas as comissoes do senado"(){
        def caminho = "sislegis/rest/comissaos/comissoesSenado"

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
        response.data.each{
            println it
        }
        println "Total: " + response.data.size()
    }

}
