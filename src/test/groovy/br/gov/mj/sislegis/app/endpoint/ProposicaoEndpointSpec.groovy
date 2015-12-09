package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIyMjQ2ZDNkZS0xNGU2LTQ1NmEtODcwMS1hOTUyNDk4N2E5NjQiLCJleHAiOjE0NDk2OTM4MzAsIm5iZiI6MCwiaWF0IjoxNDQ5NjkzNTMwLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI4MmJkN2ZmNi1jODQ0LTRkNTgtYWI1My01YmQzYWU1OWYwN2EiLCJjbGllbnRfc2Vzc2lvbiI6IjZjZmViNTU0LWQ2OGUtNGU4YS1iOGI4LWU5Y2U0Yzk4ZmI0ZiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.ZUgAdmozjEIWXGOizPLcKUg5RA_V7eXVpz5K_Qp7EOYkj9Khclsy8Gn7Mx1CBq7jE0u7deEqf4BbrU14Hky8CG92zvH1J4TvOISeoRegfKhkkZ6QqQ2cd5JwtL2M_7hfVpeNhRSlbcEfAZyXuG7dHsqTayGETuN7IWQb6CA106ivpJzgZ-PRrib2LMm_MZqX1cvpbNftRTcX1HwGRKovpiR3L1wkoCduRlvmzhXGOhnpYu09aReSV64ENr-6lc2GT3KN7Hn4xOmne70D1L6srVz2wAzVjC_hm1jgKBCZaEZgSD4gj4p9iyB--3BfTA0Ekh3TNlHSPmhZOr3l_nwm1w"
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
        def dados = [idProposicao: 8584, comissoes: []]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
