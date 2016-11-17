package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class SituacaoLegislativaEndpointSpec extends Specification {
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve consultar uma situacao legislativa pelo id"() {
        given:
        def situacoesSenado = listarTodasDoSenado()
        def idSituacao = situacoesSenado[0].id
        def caminho = "/sislegis/rest/situacoes/" + idSituacao

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve listar as situacao legislativas do senado"() {
        when:
        def situacoes = listarTodasDoSenado()

        then:
        situacoes.each {
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
        def situacoesSenado = listarTodasDoSenado()
        def idSituacao = situacoesSenado[0].id
        def random = new Random()
        def idExterno = random.nextInt()
        def caminho = "/sislegis/rest/situacoes/" + idSituacao
        def dados = [id       : idSituacao,
                     descricao: "Situação ALTERADA",
                     origem   : "SENADO",
                     idExterno: idExterno]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def listarTodasDoSenado() {
        def caminho = "/sislegis/rest/situacoes/SENADO"
        def resp = restClient.get(path: caminho, headers: cabecalho)
        return resp.data
    }
}
