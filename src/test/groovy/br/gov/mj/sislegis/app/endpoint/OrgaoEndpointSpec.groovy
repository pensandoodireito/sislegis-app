package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class OrgaoEndpointSpec extends Specification{
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir um novo orgao"(){
        given:
        def caminho = "/sislegis/rest/orgaos/"
        def dados = [nome: "Novo Órgão Teste"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve consultar um orgao pelo id"(){
        given:
        def orgaos = listarTodosOrgaos()
        def ultimoOrgao = orgaos[orgaos.size - 1]
        def idOrgao = ultimoOrgao.id
        def caminho = "/sislegis/rest/orgaos/" + idOrgao

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve listar todos os orgaos"(){
        when:
        def orgaos = listarTodosOrgaos()

        then:
        orgaos.each{
            println it
        }
    }

    def "deve alterar um orgao"(){
        given:
        def orgaos = listarTodosOrgaos()
        def ultimoOrgao = orgaos[orgaos.size - 1]
        def idOrgao = ultimoOrgao.id
        def caminho = "/sislegis/rest/orgaos/" + idOrgao
        def dados = [id: idOrgao, nome: "Órgão Teste ALTERADO"]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve excluir um orgao"(){
        given:
        def orgaos = listarTodosOrgaos()
        def ultimoOrgao = orgaos[orgaos.size - 1]
        def idOrgao = ultimoOrgao.id
        def caminho = "/sislegis/rest/orgaos/" + idOrgao

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def listarTodosOrgaos(){
        def caminho = "/sislegis/rest/orgaos/"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }
}
