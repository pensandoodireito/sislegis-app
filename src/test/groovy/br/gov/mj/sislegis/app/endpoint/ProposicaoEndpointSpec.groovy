package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI5OTMxNzhkMi1lYmI4LTQ1ZjctOTY4ZC1mMDViYWExNjk2NGIiLCJleHAiOjE0NTI2MzgwNzUsIm5iZiI6MCwiaWF0IjoxNDUyNjM3Nzc1LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJiYTRmNGZlNi1mYWJhLTQyYTYtYTgxMS0wMmI3ODVhNDI1OGUiLCJjbGllbnRfc2Vzc2lvbiI6ImYxNzExY2E4LWY1MjktNGUzYy05OTk4LWYyOGRiZjJiYTUxOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.SkAQoVRyW8gplEpI4l6xmndpwgk5Z95rULzdrl-V62kgAsiLfcGAckaTlF-U0wXbglKCZaSNydQ2I19DTrBmw_2YKDtCmeigujwB6wksDJcv-tkL30ps4kqOeq-flSBrwItRLn1cvjaHh6CRirZOqRfdt-3beKgNLarJ1k2dYJB6RJbhoSeBEjTQdofpmVhFoCGJMUkQbz0rZGkxIcfI-pTlo0u5ukf5wTdcsQxtRWN1HYd9Oqz7ITPWN2gTNQgmVjn9J0zEOw62CzRcYNPIJ0K1vsePxfdacaUI6BnSP_Q4nAb3rb2FeqoUrQ6tttQ-ypF9rX8kS8cEDeSh0GChkw"
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
        def caminho = "/sislegis/rest/proposicaos/inserirProcessoSei"
        def dados = [id: 7, nup: "123456"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
