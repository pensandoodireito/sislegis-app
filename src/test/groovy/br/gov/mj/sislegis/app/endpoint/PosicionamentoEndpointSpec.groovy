package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class PosicionamentoEndpointSpec extends Specification{
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir um novo posicionamento"(){
        given:
        def caminho = "/sislegis/rest/posicionamentos"
        def dados = [nome: "Novo posicionamento"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve excluir um posicionamento"(){
        given:
        def idPosicionamento = 220
        def caminho = "/sislegis/rest/posicionamentos/" + idPosicionamento

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve consultar um posicionamento pelo id"(){
        given:
        def idPosicionamento = 218
        def caminho = "/sislegis/rest/posicionamentos/" + idPosicionamento

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each{
            println it
        }
    }

    def "deve listar todos os posicionamentos"(){
        given:
        def caminho = "/sislegis/rest/posicionamentos"
        def query = [start: 0, max: 300]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it
        }
    }

    def "deve alterar um posicionamento"(){
        given:
        def idPosicionamento = 218
        def caminho = "/sislegis/rest/posicionamentos/" + idPosicionamento
        def dados = [id: idPosicionamento, nome: "Posicionamento ALTERADO 1"]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }
}
