package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJkYmM1NGUxZC1kODljLTQ1ZjMtOWFiZS0wMzBhMTU4MjExNDYiLCJleHAiOjE0NDk2MDMyMDcsIm5iZiI6MCwiaWF0IjoxNDQ5NjAyOTA3LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIyOWRkYTI4YS1kOGM2LTRlNGMtYWY4Yi05ODNlOTViZjA1NmUiLCJjbGllbnRfc2Vzc2lvbiI6IjBhMjQ3MDM2LTAzNDYtNDVjYy04ZGM0LTdhNWY4MTIwMDg4YSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.rBXfTX2FjGBN60uU3AU5IKI7yTn0OMyTUBTLYx7F1BFr31EI5z6Q5uJvmVElrk7xsBtf8qSdOUu54SUfqU4LcxQiTeZRKb_32Fmd7K-Yjml8TZL4kFqmV9NttULMpnwD0XhSXLSPLWdyt07w08SR8-GHdrkut1GzxBmXxImDamqLsy_dnJ1brxZ4Os2TxI188CYu4Sz32DhA4Y-0Co7mql7lto9L-XmZ42LKRL4_NUg9gBO_gQOjuYmvtgUsX57f9SLjyj4Ulz1IGPYl8M0IwlasFFUOhCVJAFxaUFWiu91OvQidQzuc7sF-tD9vdFncVHAPer-OFmsMTzY0VIh0eA"
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
        def idComissao = 5
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
        def idEtapaRoadmap = 8592
        def caminho = "/sislegis/rest/proposicaos/removerEtapaRoadmap/" + idEtapaRoadmap

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

    def "deve reordenar o roadmap de comissoes de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarOrdemEtapaRoadmap/"
        def etapa1 = [id: 8589, ordem: 1]
        def etapa2 = [id: 8593, ordem: 10]
        def dados = [etapa1, etapa2]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok

    }

}
