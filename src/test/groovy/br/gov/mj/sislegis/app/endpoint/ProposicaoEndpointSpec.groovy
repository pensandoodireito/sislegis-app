package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzMGE4M2ZiNC0wY2NmLTQyZWQtYmM3OS1jYzNmMjE3MTU2NDEiLCJleHAiOjE0NDk3NzMxNjgsIm5iZiI6MCwiaWF0IjoxNDQ5NzcyODY4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJjMGI0NDBkYy0zZTYxLTRmMDMtYjM2My0xYzkzOTUxNjY1ZWMiLCJjbGllbnRfc2Vzc2lvbiI6IjNhZTcxZWQ4LWZmN2EtNDBlOC04ODk5LTM4YjM5ODgwNjdlYyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.NjZ73I5LeaZCz3prqF08E2_XHyXlIeloEpVg0Rn14rSl7wEEy_gSPthXVV2_roxuVaIsbeT8hnEK4zLiYVJvYABTo6qVR-EADLBl2ERzwUvoohX-LXiqKFStMEimFAqReF--ecTW5Yw7pDtkOIdF0zWHhcg5MfQl3UsgEIh7pO0E-Q4WX8YdZc9-8bKZIIWS6GEAHre9cmqFmge6pZTQkKRYUgKK4Ls918bamaIGxesvx8e6oH8MY5cL4trdLSVdfAj_87P1rnufIvYXl3DPCrP2aaDAuxdW32eeyIsRA_KtQ5Dp-zR6v-pI0XXJ6LRZSkbzf1xFhu3NqNVDasDq6g"
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
        def dados = [idProposicao: 8585, comissoes: ['PLEN', 'CAPADR', 'XXX']]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
