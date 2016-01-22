package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjMjY0NjAyNi03MzMxLTQ2NzYtYmQ2Yy04NjRkMDdjYzE5NDgiLCJleHAiOjE0NTM0MjY5MTYsIm5iZiI6MCwiaWF0IjoxNDUzNDI2NjE2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIxNDczNzViYy1jNGI3LTRmOGEtODU0YS1iNDg0MDZjZDlhMzciLCJjbGllbnRfc2Vzc2lvbiI6Ijg4YTM3OWU0LTkyYWQtNDRlNy1iZGNkLTViZWNmYWNiNDg0YiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.GG0t3IGLJx0AEYiroz8yF82fmsVM33nXj5iDg-GHfI66BiTMLdUBhiF-mujn4Sucl15GUorYhtmfzefCjKvl1278fvbuq8TZ0Cqv2eWlT6YjxEI-MZ-Pdv7ffpY1cYAFVphTwUMO6M3VHBoItD3PSAPyejFcYpHauugp-FAJGlJqSCuwSUr7yyL5UoEigEanZps12W7m-umE9agfKBScMxLbKbg6eyaUM_iciShPUNhdOh6ju-s22_xuLaILnfBGjPa1N61KnjMlMmpc3Mn76xfRfAVDWvZw6_wQy3J8lTF4CbMr_PnMLfHQU1v6gah02wcT9VsMy6e9DYE9beVX1Q"
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
        assert resp.data != null
    }

    def "deve excluir um processo SEI pelo seu id"(){
        given:
        def idProcessoSei = 141;
        def caminho = "/sislegis/rest/proposicaos/excluirProcessoSei/" + idProcessoSei;

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = no content
    }

}
