package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJhOTc3OGVjZS01OTJkLTRlYzEtOTQ2ZS03MWM3MzQ0NzU5NzEiLCJleHAiOjE0NDkwMjMyMTUsIm5iZiI6MCwiaWF0IjoxNDQ5MDIyOTE1LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI4NDNjNzE2YS0yODE0LTQ5NTQtYjVjNS0zMDNmMmY1YTgwYzAiLCJjbGllbnRfc2Vzc2lvbiI6ImE0ZmMxOTcyLTY5ZTktNGViMC05MDhhLWM3MWI1ZTBlYjc4YiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.DMtAsA8ZixhdpN2GSf2ry9CHw3H0nKnQpgS_pqfFb6nWUyq06WVh2G7J4hcbLmsOT-4w0HOnWky_43Y6dvf2aN_0n4GqQ5ipj9VVBQLTVKJjHC0rEmzPC0yBLAIsZUagymL82gKF9e29hVe7ycDwu4h1Cqiptt2gw9-ANGV9Lq5a_QYrFlBi2HpuvtOpwCi4dF6WpmyFGMrG3V11-SxN1zUNYHrayU9QlkszvxVNPkCA14XK8jTp6b3X7Gex4Gne8i1Gsnclit09rbXYyvtlQLiYJI4MK7sOOehlEnvY2aUjdXxCF3cugs9jHkEkFJeiXRsJpm1wEtZaf56ryV8MVA"
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

    def "deve inserir uma nova etapa no roadmap de comissoes"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/inserirEtapaRoadmap"
        def idProposicao = 3168
        def idComissao = 1
        def dados = [idProposicao: idProposicao, idComissao: idComissao]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each {
            println it
        }

    }

    def "deve remover uma etapa no roadmap de comissoes"() {

        given:
        def idEtapaRoadmap = 8574
        def caminho = "/sislegis/rest/proposicaos/removerEtapaRoadmap/" + idEtapaRoadmap

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

    def "deve reordenar o roadmap de comissoes de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarOrdemEtapaRoadmap/"
        def etapa1 = [id: 8575, ordem: 1]
        def etapa2 = [id: 8576, ordem: 2]
        def dados = [etapa1, etapa2]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

}
