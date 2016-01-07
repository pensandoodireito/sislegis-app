package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJmYWIyMzEzYy02MDcxLTQ3OGUtOGNkNC0yZDhjZTkzM2NmZDQiLCJleHAiOjE0NTIxODg5ODUsIm5iZiI6MCwiaWF0IjoxNDUyMTg4Njg1LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI3NmMwMzFjZS1lMjMyLTQzZDYtYmU3NS0yZjgxZDliNDQ3YjciLCJjbGllbnRfc2Vzc2lvbiI6IjcxMTUzNTRhLWU1NmYtNDE5Mi1iMjcxLTRhYmFkNTRlOGJjNyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.UXImKOkRY2sZ3YdQFNPjqyu7iyP6z19Dce0-l_qKBGuD6ykwxFNWtDbpAFOZnCD5LLAIDuOor59EgVaCIbKpu4yXHRAlC09Ecx3QeyB0KVm7rQHBeRGogg8VXaC8etQtTOLSRA8m3Yr6edM7eGfqgLtzqxpKCezSjM4d6KlVLAoBrDzNLAzBpZbMBy2QXvFPhDsWwred-ba6IOQNSnjHQXRE3lVszFpOsfw5phTy0DK1Ja9Slkje9PeoIqIOAUI18DPaEP3KDun8kQxiGv8v9O_bzK2r2nbDK2myEh3RGxLkgsE8Hpk-pMddiAEh_8ycWadbDI46tgqmq22bm_H6ZA"
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

    def "deve listar as votacoes de uma proposicao da CAMARA"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/listarVotacoes"
        def query = [idProposicao: "", tipo: "PL", numero: "1992", ano: "2007", origem: "CAMARA"]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it.getDataHora
        }

    }

    def "deve listar as votacoes de uma proposicao do SENADO"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/listarVotacoes"
        def query = [idProposicao: "112464", tipo: "", numero: "", ano: "", origem: "SENADO"]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it.getDataHora
        }

    }

}
