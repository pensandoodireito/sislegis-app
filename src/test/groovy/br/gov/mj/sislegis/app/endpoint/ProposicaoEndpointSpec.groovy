package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJiMDliMDMxMi00YmM4LTRmOTItOGQ4Zi1kMTVkNTViN2QzMzAiLCJleHAiOjE0NDk3NTAxNDAsIm5iZiI6MCwiaWF0IjoxNDQ5NzQ5ODQwLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJmZWY1YzdmYS1lOTZmLTQ1NjktYTFmYS01N2ExMTY3ZWY3ODYiLCJjbGllbnRfc2Vzc2lvbiI6ImVmMDY3MWNiLTA1M2EtNGZiNC04NWNhLTlmYTg4NjE4OTI2NyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.FlwCgKdibsbbaGkBRZUgc8z-JxzeOzsrwgRoaRT8VGlUtVG5ywN3AzP8Tmn0HZNjOqfvYMuu4D44rh_XdaU9hdeyci1VyOJxyqhDX-B3dT8xibo3FlvKq7HGYZkIRzsXuyAbJenfaqJJ15xA1bTtr2n_5Q0Lt7bE3Mp12N8_xAkAg95u7gJk1LndgzNkE82VPGk3ShoNEBHtJdfkO1XKn2_5LkAnp8veB5VC50Ip1SgIZcoUAVIsSeGZaM4QXTFaWJ6d7aNpTNJiE1STFeTBJnUqugFgPs2UJoL2KQDqKwUtTJJG3D5jo1fjTqeOqXFD8sWRGBzTzNi66euHl5q9eQ"
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
        def dados = [idProposicao: 8584, comissoes: ['COMIS1', 'COM2', 'COM3']]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
