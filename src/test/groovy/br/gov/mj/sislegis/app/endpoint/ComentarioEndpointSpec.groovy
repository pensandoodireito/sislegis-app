package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComentarioEndpointSpec extends Specification {

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve salvar um novo comentario"() {
        given:
        def caminho = "sislegis/rest/comentarios/"
        def comentarios = listarTodosComentarios()
        def idProposicao = comentarios[0].proposicao.id

        def dados = [descricao: "novo comentario teste", proposicao: [id: idProposicao]]

        when:
        def response = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert response.status == 201 // status 201 = Created
    }

    def "deve listar os comentarios de uma proposicao"() {
        given:
        def comentarios = listarTodosComentarios()
        def idProposicao = comentarios[0].proposicao.id
        def caminho = "sislegis/rest/comentarios/proposicao/" + idProposicao

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        response.data.each {
            println it
        }
    }

    def "deve alterar um comentario"() {
        given:
        def comentarios = listarTodosComentarios()
        def idProposicao = comentarios[0].proposicao.id
        def idComentario = comentarios[0].id
        def caminho = "sislegis/rest/comentarios/" + idComentario

        def dados = [id        : idComentario,
                     descricao : "comentario alterado pelo teste!",
                     proposicao: [id: idProposicao]]

        when:
        def response = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert response.status == 204 // status 204 = No Content
    }

    def "deve ocultar um comentario"() {
        def comentarios = listarTodosComentarios()
        def idComentario = comentarios[0].id
        def caminho = "sislegis/rest/comentarios/ocultar/" + idComentario

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
    }

    def "deve listar todos os comentarios"() {
        when:
        def comentarios = listarTodosComentarios()

        then:
        comentarios.each {
            println it
        }
    }

    def listarTodosComentarios() {
        def caminho = "sislegis/rest/comentarios/"
        def query = [start: 0, max: 300]
        def response = restClient.get(path: caminho, query: query, headers: cabecalho)

        return response.data
    }
}
