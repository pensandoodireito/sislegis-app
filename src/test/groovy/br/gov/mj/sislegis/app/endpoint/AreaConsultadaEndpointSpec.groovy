package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class AreaConsultadaEndpointSpec extends Specification {
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve inserir uma nova area consultada"() {
        given:
        def caminho = "/sislegis/rest/areaconsultadas/"
        def random = new Random()
        def nomeAleatorio = random.nextInt()
        def dados = [descricao: nomeAleatorio]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve consultar uma area consultada pelo id"() {
        given:
        def areas = listarTodasAreas()
        def idAreaConsultada = areas[0].id
        def caminho = "/sislegis/rest/areaconsultadas/" + idAreaConsultada

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve listar todas as areas consultadas"() {
        when:
        def areas = listarTodasAreas()

        then:
        areas.each {
            println it
        }
    }

    def "deve alterar uma area consultada"() {
        given:
        def areas = listarTodasAreas()
        def idAreaConsultada = areas[0].id
        def random = new Random()
        def nomeAleatorio = random.nextInt()
        def caminho = "/sislegis/rest/areaconsultadas/" + idAreaConsultada
        def dados = [id: idAreaConsultada, descricao: "Area Consultada Teste ALTERADA!" + nomeAleatorio]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def listarTodasAreas() {
        def caminho = "/sislegis/rest/areaconsultadas/"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }
}
