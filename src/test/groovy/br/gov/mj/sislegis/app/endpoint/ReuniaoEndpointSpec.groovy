package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ReuniaoEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve listar reunioes por mes"(){

        given:
        def caminho = "/sislegis/rest/reuniaos/reunioesPorMes"
        def mes = 02
        def ano = 2016
        def query = [mes: mes, ano: ano]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each(){
            println it
        }
    }
}
