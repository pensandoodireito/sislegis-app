package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class EquipeEndpointSpec extends Specification {
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir uma nova equipe"() {
        given:
        def caminho = "/sislegis/rest/equipes"
        def dados = [nome: "Nova equipe", listaEquipeUsuario: []]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve consultar uma equipe pelo id"() {
        given:
        def equipes = listarTodasEquipes()
        def ultimaEquipe = equipes[equipes.size - 1]
        def idEquipe = ultimaEquipe.id
        def caminho = "/sislegis/rest/equipes/" + idEquipe

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve listar todas as equipes"() {
        when:
        def equipes = listarTodasEquipes()

        then:
        equipes.each {
            println it
        }
    }

    def "deve alterar uma equipe"() {
        given:
        def equipes = listarTodasEquipes()
        def ultimaEquipe = equipes[equipes.size - 1]
        def idEquipe = ultimaEquipe.id
        def caminho = "/sislegis/rest/equipes/" + idEquipe
        def dados = [id: idEquipe, nome: "Equipe ALTERADA", listaEquipeUsuario: []]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve excluir uma equipe"() {
        given:
        def equipes = listarTodasEquipes()
        def ultimaEquipe = equipes[equipes.size - 1]
        def idEquipe = ultimaEquipe.id
        def caminho = "/sislegis/rest/equipes/" + idEquipe

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def listarTodasEquipes() {
        def caminho = "/sislegis/rest/equipes"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }
}
