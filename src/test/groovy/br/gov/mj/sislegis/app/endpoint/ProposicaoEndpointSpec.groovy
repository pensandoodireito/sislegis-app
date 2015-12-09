package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI1NzA0NDA4MC01MTE1LTQwY2QtYTNkYS0zY2ExYzYwZjE1MzQiLCJleHAiOjE0NDgwMjAyNDUsIm5iZiI6MCwiaWF0IjoxNDQ4MDE5OTQ1LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI2ZDcyZjM5MS02NWI5LTRiYWEtODU5OS04OTBiYWFlMWE3YzciLCJjbGllbnRfc2Vzc2lvbiI6ImFmNTQ1ZDI3LTlmZjItNDI4MS1iYWZkLWJmNDJlZWRhOGZiNSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.Q7mXvnZvlueteSmZvVPBYzeSt2Ybaf2YouL2W-rEdAWlVoIs0Q-4kZVoAkaEwC9DhNNggjkG5K84g9YMHLHhBMtRD2XGuDNDXJObZnNiyYyi2g_f6rmAI7DlOaiaoWEJgpupDy02AxF-zcxhlfydpkY9AHkbkE1qbmRkpPD_duMJxuJQN8FOyoBSdWrX8C44fh9CkDmVK0z1kQagHrhK-qsuIIQcrS2giEKAHArFXN7H3brj4Mo0ZJhK9jXA9gd1-xvdwZ7lQmMTY52k26POT43hOZbnolj4wf_zgqNJZmIiXT0hD6pysaCRXEyTSYnsHk81jKBabCj8qtv8zomKLA"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"(){

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

    def "deve listar o historico de alteracoes de posicionamento"(){

        given:
        def id = 35
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = client.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each{
            println it
        }

    }

}
