package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComentarioEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve salvar um novo comentario"(){
        given:
        def caminho = "sislegis/rest/comentarios/"

        def dados = [descricao  : "novo comentario teste teste",
                     proposicao : [id: 162]]

        when:
        def response = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert response.status == 201 // status 201 = Created
    }

    def "deve listar os comentarios de uma proposicao"(){
        given:
        def idProposicao = 162
        def caminho = "sislegis/rest/comentarios/proposicao/" + idProposicao

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        response.data.each {
            println it
        }
    }

    def "deve alterar um comentario"(){
        given:
        def idComentario = 174
        def caminho = "sislegis/rest/comentarios/" + idComentario

        def dados = [id: idComentario,
                     descricao  : "comentario alterado pelo teste xxxxx",
                     proposicao : [id: 162]]

        when:
        def response = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert response.status == 204 // status 204 = No Content
    }

    def "deve ocultar um comentario"(){
        def idComentario = 166
        def caminho = "sislegis/rest/comentarios/ocultar/" + idComentario

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
    }
}
