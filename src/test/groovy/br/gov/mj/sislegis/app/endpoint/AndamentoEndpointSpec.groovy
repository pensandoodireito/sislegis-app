package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class AndamentoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI1ZDE5MzMyOC04MzAwLTQ5NWMtOTJmMi0xY2Q0ZjUzNjRhYmEiLCJleHAiOjE0NDUwMjQxODQsIm5iZiI6MCwiaWF0IjoxNDQ1MDIzODg0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvc2lzbGVnaXMiLCJhdWQiOiJzaXNsZWdpcyIsInN1YiI6ImRmMWIwMTkzLTdkNzYtNGYxNS1hYWU3LWFjY2U3NjZmMDY3YyIsImF6cCI6InNpc2xlZ2lzIiwic2Vzc2lvbl9zdGF0ZSI6ImFmYmNhNzQzLTYzNDctNDIzMC05YjJiLWEzM2E0ZGM4M2UxOCIsImNsaWVudF9zZXNzaW9uIjoiM2ExYjBjNzMtYjk3My00Zjg5LWJkNTYtZGU4YjUyZjVlZDA4IiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zaXNsZWdpcy5sb2NhbDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJHdXN0YXZvIERlbGdhZG8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJndXN0YXZvIiwiZ2l2ZW5fbmFtZSI6Ikd1c3Rhdm8iLCJmYW1pbHlfbmFtZSI6IkRlbGdhZG8iLCJlbWFpbCI6ImdjZGVsZ2Fkb0BnbWFpbC5jb20ifQ.WIjmryY5RnJxDvz8dnKbb5bMgamBokMLQEjFoNUK0O5Zh0d-dB2egO_2QTN1uGrqqWIAyi0A9z3keQgxcf645wUmYNvM2L9YuSn26s_R73acUAAQK9C1phk7FfUEWHNL3nnfZ8Y2jRRhl56XYOK-Xyi_kSYSM_02ox1Ip93q9kAmuYI_GsVe1oaC4ZVP5gJhTV0CidsRGgAFleoyfS8gW7ohF-Z1VwxD3c5G_5_gP_v438RyPIud0Eb_3dIPftcqdH30D5kjGsFLaraqEWgwkr66BiqWMG2CKJ7dvFZ_wC1zwIB77qnZGEFKeMAewct2s1X4ESzbcXQ87moEypB1pw"
    def restClient = new RESTClient("http://localhost:8080/")

    def "deve inserir um novo andamento"(){

        def caminho = "sislegis/rest/andamentos/"
        def cabecalho = [Authorization: token]
        def dados = [proposicao:[id:"143"], descricao:"Andamento adicionado pelo teste unitário"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        println resp
    }

    def "deve retornar andamentos pelo id de uma proposicao" (){

        def idProposicao = 143
        def caminho = "sislegis/rest/andamentos/" + idProposicao
        def cabecalho = ['Authorization': token]

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data

        with (resp.data[0]) {
            id == 147
            descricao == "Andamento adicionado pelo teste unitário"
        }
    }
}
