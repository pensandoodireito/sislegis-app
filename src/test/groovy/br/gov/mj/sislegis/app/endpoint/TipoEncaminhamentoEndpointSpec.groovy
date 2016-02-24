package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class TipoEncaminhamentoEndpointSpec extends Specification{
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir um novo tipo de encaminhamento"(){
        given:
        def caminho = "/sislegis/rest/tiposencaminhamentos"
        def dados = [nome: "Novo Tipo de encaminhamento"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve consultar um tipo de encaminhamento pelo id"(){
        given:
        def tipos = listarTodosTipos()
        def ultimoTipo = tipos[tipos.size - 1]
        def idTipoEncaminhamento = ultimoTipo.id
        def caminho = "/sislegis/rest/tiposencaminhamentos/" + idTipoEncaminhamento

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve listar todos os tipos de encaminhamentos"(){
        when:
        def tipos = listarTodosTipos()

        then:
        tipos.each{
            println it
        }
    }

    def "deve alterar um tipo de encaminhamento"(){
        given:
        def tipos = listarTodosTipos()
        def ultimoTipo = tipos[tipos.size - 1]
        def idTipoEncaminhamento = ultimoTipo.id
        def caminho = "/sislegis/rest/tiposencaminhamentos/" + idTipoEncaminhamento
        def dados = [id: idTipoEncaminhamento, nome: "Tipo Encaminhamento ALTERADO"]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve excluir um tipo de encaminhamento"(){
        given:
        def tipos = listarTodosTipos()
        def ultimoTipo = tipos[tipos.size - 1]
        def idTipoEncaminhamento = ultimoTipo.id
        def caminho = "/sislegis/rest/tiposencaminhamentos/" + idTipoEncaminhamento

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def listarTodosTipos(){
        def caminho = "/sislegis/rest/tiposencaminhamentos"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }
}
