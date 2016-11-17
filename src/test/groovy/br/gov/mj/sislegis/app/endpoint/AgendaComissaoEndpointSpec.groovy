package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class AgendaComissaoEndpointSpec extends Specification {
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve listar todas as comissoes seguidas do usuario"() {
        given:
        def caminho = "/sislegis/rest/agendacomissao"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        if (resp.data.size() > 0) {
            resp.data.each {
                println it
            }
        }
    }

    def "deve comecar a seguir a agenda pela casa e comissao (follow)"() {
        given:
        def casa = "CAMARA"
        def comissao = "CAPADR"
        def caminho = "/sislegis/rest/agendacomissao/" + casa + "/" + comissao

        when:
        def resp = restClient.post(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve deixar de seguir a agenda pela casa e comissao (unfollow)"() {
        given:
        def casa = "CAMARA"
        def comissao = "CAPADR"
        def caminho = "/sislegis/rest/agendacomissao/" + casa + "/" + comissao

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

}
