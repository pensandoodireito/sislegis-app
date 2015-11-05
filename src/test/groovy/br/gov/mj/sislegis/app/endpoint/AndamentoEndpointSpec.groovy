package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class AndamentoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzZDYyZmYwMy1hMWQ1LTRhZTktYWFkNS0yZGFmNWNkNThlM2MiLCJleHAiOjE0NDUwMjkxOTgsIm5iZiI6MCwiaWF0IjoxNDQ1MDI4ODk4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvc2lzbGVnaXMiLCJhdWQiOiJzaXNsZWdpcyIsInN1YiI6ImRmMWIwMTkzLTdkNzYtNGYxNS1hYWU3LWFjY2U3NjZmMDY3YyIsImF6cCI6InNpc2xlZ2lzIiwic2Vzc2lvbl9zdGF0ZSI6IjUzNzRmNjU5LTYxZjctNGJkOC1hN2NmLTk4MjZjMjYwYWRjZCIsImNsaWVudF9zZXNzaW9uIjoiOGM2NjU2NTctMzM3ZS00YmU0LTkzYzYtMmJmYTU2MWJkODUyIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zaXNsZWdpcy5sb2NhbDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJHdXN0YXZvIERlbGdhZG8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJndXN0YXZvIiwiZ2l2ZW5fbmFtZSI6Ikd1c3Rhdm8iLCJmYW1pbHlfbmFtZSI6IkRlbGdhZG8iLCJlbWFpbCI6ImdjZGVsZ2Fkb0BnbWFpbC5jb20ifQ.YKrT3PB44LT9sP0uS1mNk2GNiw_sAd_7MO3Ld3aADziJBRsNCdaCjEVq5suieL-HR4mxRoJWi7BRxRuswHJFdvWtDNBYIugDB-uOq6oRfOQu69NZUx6_PI1zAm42GPUROAdiTQ81InIzS6zkxi6Ql5P5N13yaWv1DIfxuSgXymjxXMpne8nlfPrLJY4FONRalDdB1ibTaJsDuKSvi-ayu2OPntbUWfVVJF_5UKjrc3BS5bWY4Fc-gLxe0rOFFpVIg0f9TbmvkHE7_yZskSVtRfom4nBzznllDQuMnD-9gB0IOnh_Ee4L-PJhxiOc9bS2IosT2svI4gtwwgYIN9Q1MA"
    def restClient = new RESTClient("http://localhost:8080/")

    def "deve inserir um novo andamento"(){

        def caminho = "sislegis/rest/andamentos/"
        def cabecalho = [Authorization: token]
        def dados = [proposicao:[id:"142"], descricao:"Andamento adicionado pelo teste unitário"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        assert resp.status == 201 // status 201 = Created

    }

    def "deve retornar andamentos pelo id de uma proposicao" (){

        def idProposicao = 142
        def caminho = "sislegis/rest/andamentos/proposicao/" + idProposicao
        def cabecalho = ['Authorization': token]

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data

        with (resp.data[0]) {
            assert descricao == "Andamento adicionado pelo teste unitário"
        }
    }
}
