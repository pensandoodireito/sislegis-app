package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJmNWEwNDdkZC03NjgwLTRlNzEtYWJkNi0wZjZjM2U2NDdmY2YiLCJleHAiOjE0NDk2MTA1NzUsIm5iZiI6MCwiaWF0IjoxNDQ5NjEwMjc1LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI1M2VlN2NjMC0wNDI0LTQ4YzMtOTk3ZC04Zjk2ZTZkMGJkMWMiLCJjbGllbnRfc2Vzc2lvbiI6IjViNjY0ZGUzLWU1YWUtNGRkZC1hYzVkLThjMTljNTAzNDRjMiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.qzuxDoKgguzJ7URAavKERGHlNbwwU35KurKrnT2x56B4AhSDI1vOdtR9mYgoaNGlBGIsdjZ4ln8UufvEdHQyeO2DU69-UprW5K3JRexYvgihL6Jd8BCTFBihaecVwF8I9SZG4_kT_XPzTwdv431N-kY94rFx-xQFUxOA4rulfOcZdh5AXWuU2xCAvd1lKawIUBvqXiYgA0xNpl9bIZjIgdRWgX6-kSId-xDZ98S0GDMfev54M96W_j1SkvzR_9j6Tjptk-vYpyKDBdlBuDxTc2idY-rv07PIvzmCnm8Vk3R-jeHv88fX0d7mOW1z4ruWS8E5arhC-qIUj4otOF4MaA"
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
        def idProposicao = 8585
        def comissao = "PLEN"
        def dados = [idProposicao: idProposicao, comissao: comissao]

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
        def idEtapaRoadmap = 8594
        def caminho = "/sislegis/rest/proposicaos/removerEtapaRoadmap/" + idEtapaRoadmap

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

    def "deve reordenar o roadmap de comissoes de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/reordenarRoadmap/"
        def etapa1 = [id: 8595, ordem: 9]
        def etapa2 = [id: 8596, ordem: 10]
        def dados = [etapa1, etapa2]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

}
