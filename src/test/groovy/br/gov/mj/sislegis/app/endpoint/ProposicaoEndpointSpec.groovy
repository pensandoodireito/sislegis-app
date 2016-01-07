package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJkZTE5MjExOS0xYzJhLTQ4ZTAtYTI2Mi1mOTA5ZmFmMTQ0NTEiLCJleHAiOjE0NTAxMDg4NTAsIm5iZiI6MCwiaWF0IjoxNDUwMTA4NTUwLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJjNmZkMjhmMi0yMDU0LTRmMGEtOWU4My0yMjQzMzJlM2ZmNDAiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIzYmRiNjhjZi1iN2YzLTRlNzktYmVjZS1iNWVlMzE2YmNiZWEiLCJjbGllbnRfc2Vzc2lvbiI6IjA0ODcwNTVmLTlhNDEtNDRiOC04MGViLWU3MDMyNWIwMGJhYyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.ZZqEbRqDqITOc7vFw8hVJHj0lg8SOTF2jw7_uPYoSKhfi40SjzJU7Sq1A1jX6QaQgocLa_RzLB9j2DrUP-buRTJalIzZzh354RdsqzAxbFB_LWFkbZc_jHA0EHkVWAPfe8PhRXyZtfVYrjseRsYxO6LIdd71gF34EjgRAvU26R9kKu8ufkbg_tylgQ3f9p8ttjz9HLlTW3dyEC36OC7Bt5p0KOUS0LEfPJDIJwR559xZArm7qni0cfTjW_ZJUn2tVIIzGv8PPm4_mk0X0-tjjipEkFZun5XvDrzOIlNGTUzyru5RCo19_jOXXWxuAIs4j0WbJ-_MoUhNpwjo2_PsUw"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def id = 47
        def idPosicionamento = 7
        def preliminar = true
        def dados = [id: id, idPosicionamento: idPosicionamento, preliminar: preliminar]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar o historico de alteracoes de posicionamento"() {

        given:
        def id = 35
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = client.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }

    }

    def "deve atualizar o roadmap completo de comissoes de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/setRoadmapComissoes"
        def dados = [idProposicao: 29, comissoes: ['PLEN', 'CAPADR', 'CCJC']]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar as votacoes de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/listarVotacoes"
        def query = [idProposicao: null, tipo: "PL", numero: "1992", ano: "2007", origem: "CAMARA"]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it.data
        }

    }

}
