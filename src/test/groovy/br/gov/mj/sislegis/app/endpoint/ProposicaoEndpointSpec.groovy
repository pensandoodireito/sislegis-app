package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIxNDAyN2RiZi0xMmJiLTQwZDMtOWI5MC1iODE1OGFlYjZlZjQiLCJleHAiOjE0NDcyNzgxMzYsIm5iZiI6MCwiaWF0IjoxNDQ3Mjc3ODM2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI4MWU3NDFlNi1lMDkyLTQzNmYtOTY5Zi03YTI3NjkwMjNjMzQiLCJjbGllbnRfc2Vzc2lvbiI6IjE3MzI3YWNmLTg0N2QtNDgyOC04MTU1LWNiZjcyOTQ4NzUyOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.U6TUlcjwp6gMLeEwGC9-1hgl7HHbuCS3Xg0de2aPi1BVtrnrttF_v4CpVEty7a4F_LNOVCfViFTEJFjsMJ3O5gS8YHfGri1WRXzeZBCOH_GOh3ZnVXYsnJzz89h-Kgj_6Wiki5tcwEiDhH0HD_JFkM2r77HAxVjTczM4Kjksh5uI35aP90Xvt1_AN8xMDm0Bh30KJxzx9yk6js72Lc9FiXSUb7ycaCbEsuly5GKxSDrUty5wvsryEW7LgTxIfYqEqnZPlUdnCM8stnj7CZbWiPT5Sp-D1BjK9QzTxzRzAcPpmoLYM6DVSKfxdLNXecEZspdDBzN-lPslF21hg3_3ag"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def id = 143
        def idPosicionamento = 4
        def dados = [id: id, idPosicionamento: idPosicionamento]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar o historico de alteracoes de posicionamento"(){

        given:
        def id = 143
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = client.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each{
            println it
        }

    }

}
