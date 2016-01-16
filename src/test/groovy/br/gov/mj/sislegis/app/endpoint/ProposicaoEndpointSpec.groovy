package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0OTgyODg1Zi0xZGFkLTRmMzctYjdjZC1jODRkODU0Y2RkYzkiLCJleHAiOjE0NTI5ODIxNDMsIm5iZiI6MCwiaWF0IjoxNDUyOTgxODQzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJiY2NjMDE0NC0yOGYyLTQ2MjgtYTM4Yy05ZGFiZDA2YzE0NzEiLCJjbGllbnRfc2Vzc2lvbiI6IjEzODU1MWE3LWFmZjEtNDdiNS04Y2JhLTgyMWFjNDlmOWNlZSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.qCxYy3iL0puf8wRcAxCmOYrUiEyh6wNvNFMBwL1OU3X8DY_p8yuSUBzJhfyoUnQSMjK0VVPxmehuTi76NKa_TWQe6xZnzHijQFGFkvLdigMnn7hmNWu1wIQAkzOUkAt7mwpn9T7CpvSwVnFLebjmOKBYkjhWgs6DThqMNFq67QSbg4InOeIZwKGhSTzbc-QdIxI-84zdRZbt4Yzk2UBiROyW4WHQVo6AOJp-iMo7w7PvKcKRbspJY7yoRPBQCUUd0Bxd3g946uyCUyJGi5aQ7b_gyrsHxC3ToSCWq2Y6kDjr2Wwp09NxgCtHi1ewGbV8Wub8HnQ4ztFrknnGc8TY5Q"
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
        def dados = [id: 125, protocolo: "16.0.000000002-0"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
