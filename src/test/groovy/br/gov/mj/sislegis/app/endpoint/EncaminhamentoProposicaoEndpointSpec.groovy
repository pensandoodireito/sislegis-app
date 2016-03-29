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
        def encaminhamentos = listarTodosEncaminhamentos()
        def encaminhamento = encaminhamentos[0]
        def idProposicao = encaminhamento.proposicao.id
        def idResponsavel = encaminhamento.responsavel.id
        def idTipoEncaminhamento = encaminhamento.tipoEncaminhamento.id
        def dados = [proposicao         : [id: idProposicao],
                     tipoEncaminhamento : [id: idTipoEncaminhamento],
                     responsavel        : [id: idResponsavel],
                     detalhes: "Novo encaminhamento inserido no teste"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created

    }

    def "deve finalizar um encaminhamento"() {

        given:
        def caminho = "/sislegis/rest/encaminhamentoProposicao/finalizar"
        def encaminhamentos = listarTodosEncaminhamentos()
        def idEncaminhamento = encaminhamentos[0].id
        def dados = [idEncaminhamentoProposicao: idEncaminhamento,
                     descricaoComentario       : "Cometário de finalização do encaminhamento"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar todos os encaminhamentos"(){
        when:
        def encaminhamentos = listarTodosEncaminhamentos()

        then:
        encaminhamentos.each {
            println it
        }
    }

    def listarTodosEncaminhamentos(){
        def caminho = "/sislegis/rest/encaminhamentoProposicao"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }
}
