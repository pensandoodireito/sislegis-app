package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class SituacaoLegislativaEndpointSpec extends Specification {
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve consultar uma situacao legislativa pelo id"() {
        given:
        def idSituacao = 10
        def caminho = "/sislegis/rest/situacoes/" + idSituacao

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each {
            println it
        }
    }

    def "deve listar as situacao legislativas do senado"() {
        given:
        def caminho = "/sislegis/rest/situacoes/SENADO"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve listar as situacao legislativas da camara"() {
        given:
        def caminho = "/sislegis/rest/situacoes/CAMARA"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve alterar uma situacao legislativa"() {
        given:
        def idSituacao = 10
        def caminho = "/sislegis/rest/situacoes/" + idSituacao
        def dados = [id       : idSituacao,
                     descricao: "Situação ALTERADA 1",
                     origem   : "CAMARA",
                     idExterno: 1052]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }
}
