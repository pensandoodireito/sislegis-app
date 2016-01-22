package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIxYWYxNGQwYy01YTMzLTQwMjctOTY2MC0yZjFkYmY1ZjA2ZWIiLCJleHAiOjE0NTM0MzA2MTIsIm5iZiI6MCwiaWF0IjoxNDUzNDMwMzEyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI5NzUzZTdmZC0wNGRhLTRlZWMtODhhZS00ZWYxZTc4YTJjOWUiLCJjbGllbnRfc2Vzc2lvbiI6ImRlMGRiZWZjLTNkZjEtNDAzNC05MzFjLWRmMzQzZDg0OThjOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.Sb2U-JiLVqUlILMNSomX5nDrsYUneUaLKKNiCqjoeUJmHstV24s8qU9hviOFtUnLVW85YXtfdblrBCgaJSDGQgXNp-Gl7P7mKHFQfoq_2evY_qnVL6w3ZfoPECg6_Nz5G25Negj6P13lippRBdictmAYKfDd_oow9bPY0tgovHxH2Zewnw6ay_6o9xSrt3c-lX9yYHj3NFdXOmspm4qbMwfHG5B3qzblpsTq9JCTICcNO4q_BPoPlTvPpOT4Jjw_6b2PV9fA3ZERAtiMWhlsU-rSViZK2xJQZe_XK31YtnHQs8qTasuWV2y6IBaFztw9LuxEkK0hc9iH00JpAbdmbQ"
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

    def "deve inserir um processo do sei a partir do protocolo"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/vincularProcessoSei"
        def dados = [id: 125, protocolo: "1600000000020"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        println resp.data
    }

    def "deve excluir um processo SEI pelo seu id"(){
        given:
        def idProcessoSei = 145;
        def caminho = "/sislegis/rest/proposicaos/excluirProcessoSei/" + idProcessoSei;

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = no content
    }

}
