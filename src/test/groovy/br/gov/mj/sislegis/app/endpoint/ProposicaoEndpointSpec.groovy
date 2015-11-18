package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJkZTgxMmRmNi0zYWM1LTQ0ZDEtOTMxNC1mYjJhOGNhNDk1MzQiLCJleHAiOjE0NDc0NDczNzgsIm5iZiI6MCwiaWF0IjoxNDQ3NDQ3MDc4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJkNzMyM2FkMC0zYzM4LTQzMDItOTBkZi1jYzgyY2Q3MjIwZWMiLCJjbGllbnRfc2Vzc2lvbiI6IjZkYjU4M2MzLTA2MzEtNDU1NC1hYWVhLTM1NGVlNTEwNGFlNyIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.MN1zxKLobtcaBUTvGmO1Ke4LuxP-5wI5NiSdBdNyImag0NN1kYvyC91nzMN_q8jlWBlO7EU3Lk0Z4RNg70LmH8toZZmXX5inmfRL70U_0uwLH1sjJ7vHgmJfWivP3nIOndC423Vr5jFwrN9NLYAfYnzPBvVaFS7U1A8Nuzrp_JSi4hL2vS1LDeNwVbFgUzVD8yp-HEKFN-lVDyc4KbOXNQ4Iz6sMckEy9-GHKhI5S8cuW04UkPVpbuze44xHrqNSKohaEr2yE6UurFDmDPMKAjMYkGqWOLVpp0nspEWY6JnYrGBYOITfxWYo76-k4cw6F1YMJyVAkyjp0R_Q7rHGag"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def id = 35
        def idPosicionamento = 5
        def dados = [id: id, idPosicionamento: idPosicionamento]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

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
