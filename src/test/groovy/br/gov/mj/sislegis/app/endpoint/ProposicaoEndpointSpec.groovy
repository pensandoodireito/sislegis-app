package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzNDI5NTc4Mi0xNTg5LTQyNjYtYTljOC1mYTU3YTAyZjBjNWMiLCJleHAiOjE0NDcyNzk3NjQsIm5iZiI6MCwiaWF0IjoxNDQ3Mjc5NDY0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI4MWU3NDFlNi1lMDkyLTQzNmYtOTY5Zi03YTI3NjkwMjNjMzQiLCJjbGllbnRfc2Vzc2lvbiI6Ijk1YzY1NWEyLWVhMjMtNDkyOC05ZWJmLWEzNGYzMjExNTkyNiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.Unl15wVxsk1SVzMsjeMH4Ed4zch2-cAgFClKdIJVEa47Yhq4CduxHQ2bfL1MKKdPx-Mbm8gom-lFGZbgjW48SOa4J2RQARHP3vSkmACa_32b_IgIiLSzu5u3kkKZKHFyicVMfoxEtkz7h8Vz6Znmg5D8uulX0YS2WlNkmedoS6F-g9jQAaxW2lFUF9nzF5I7ZSX8DMtykNra0m67ql2lcB5ti7g0MnrjS_cz81f_dDkgSUrhtfCp1l0jAkkavDRn0mBgbBLHP1oFtJbUyVLq-sHO9t-iQMCXYXgVavFWrHivA3FJMTZQLQlQo-UYWdzMI7BsqthL7uhpivDJ9_5H5A"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def id = 143
        def idPosicionamento = 5
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
