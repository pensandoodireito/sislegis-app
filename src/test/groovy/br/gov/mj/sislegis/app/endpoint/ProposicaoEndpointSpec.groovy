package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIyM2UyMjAyOS02OTE5LTQ3N2ItYjhjMi1mZjI4NWRiMzY2NGEiLCJleHAiOjE0NTM0MjMzMzQsIm5iZiI6MCwiaWF0IjoxNDUzNDIzMDM0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIxNDczNzViYy1jNGI3LTRmOGEtODU0YS1iNDg0MDZjZDlhMzciLCJjbGllbnRfc2Vzc2lvbiI6IjUzZWQxMTUwLWMyMDAtNDBiNi1iOGQzLWZmYmU1MmM2ODU5MCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.Bp7ig_Lay4MPphpzbTzngRVZhYxL6muBiyaeuWb93mQ80RI5A7uENWa0LeJaIVtVri7CaCkIURBp8frC_H4rbYBY9kM2op4ye4UdTuQhU2VNwdEz820Okj-uQUcqWECNSFsfXj5jxoAfw-PipA6qLYjPMkozk9q5Yg_x_ZQLxZTaMS0iiZ1VHm1wWpX4ZHupypGVslTtfEm7KXaEQskkWODbgvrs-O5ToiOQkUTnrgRXcg3wnCPxWrdsqyhSB8VHdu6g28WXxtycX6hrSrPiTQf0O10bF8oNQBCPyP9Ig0pCZCOjv4fv8WGRlUI5CKH5ssVzjuSXLlmeW3F2WGRsDg"
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

    def "deve excluir um processo SEI pelo seu id"(){
        given:
        def idProcessoSei = 129;
        def caminho = "/sislegis/rest/proposicaos/excluirProcessoSei/" + idProcessoSei;

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        asser resp.status = 204 // status 204 = no content
    }

}
