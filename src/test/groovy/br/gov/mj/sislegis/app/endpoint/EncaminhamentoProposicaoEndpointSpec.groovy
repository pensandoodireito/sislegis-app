package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class EncaminhamentoProposicaoEndpointSpec extends Specification {

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir um novo encaminhamento"() {
        given:
        def caminho = "/sislegis/rest/encaminhamentoProposicao/"
        def dados = [proposicao         : [id: 162],
                     tipoEncaminhamento : [id: 179],
                     responsavel        : [id: 1],
                     detalhes: "Detalhes do novo encaminhamento inserido no teste"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created

    }

    def "deve finalizar um encaminhamento"() {

        given:
        def caminho = "/sislegis/rest/encaminhamentoProposicao/finalizar"
        def dados = [idEncaminhamentoProposicao: 181,
                     descricaoComentario       : "Cometário de finalização do encaminhamento"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
