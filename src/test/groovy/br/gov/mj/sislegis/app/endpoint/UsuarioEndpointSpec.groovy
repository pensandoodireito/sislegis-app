package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class UsuarioEndpointSpec extends Specification{
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir um novo usuario"(){
        given:
        def caminho = "/sislegis/rest/usuarios"
        def dados = [nome: "maria", email: "mariajose@mj.gov.br"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve excluir um usuario"(){
        given:
        def idusuario = 223
        def caminho = "/sislegis/rest/usuarios/" + idusuario

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve listar as proposicoes seguidas"(){
        given:
        def caminho = "/sislegis/rest/usuarios/proposicoesSeguidas"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        if (resp.data.size() > 0) {
            resp.data.each {
                println it
            }
        }
    }

    def "deve consultar um usuario pelo id"(){
        given:
        def idusuario = 2
        def caminho = "/sislegis/rest/usuarios/" + idusuario

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each{
            println it
        }
    }

    def "deve listar todos os usuarios"(){
        given:
        def caminho = "/sislegis/rest/usuarios"
        def query = [start: 0, max: 300]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it
        }
    }

    def "deve alterar um usuario"(){
        given:
        def idusuario = 1
        def caminho = "/sislegis/rest/usuarios/" + idusuario
        def dados = [id: idusuario,
                     nome: "Gustavo Cesar Delgado"]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }
}
