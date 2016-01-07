package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComissaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0ZTAzY2EzYS01OTRjLTQ4YzQtOTE0Mi1hMTc2YzBkZTI0M2QiLCJleHAiOjE0NDk2MDY3MTgsIm5iZiI6MCwiaWF0IjoxNDQ5NjA2NDE4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIyOWRkYTI4YS1kOGM2LTRlNGMtYWY4Yi05ODNlOTViZjA1NmUiLCJjbGllbnRfc2Vzc2lvbiI6IjI0Y2Q0ZDBmLTM2YTYtNGUxZS05NDZkLTUyYTdmNzc0ZTYzZiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.KmUw-tui8pzkPW_AREV31Bm7PHNGjscvyaDmAJaaiygcfM9rxfDXapFi6232lXHslBzxxtcpVd6NZamEmYyikmKqJI_YAcaRh89SQVhtEIHphBjmxdUKPqaaSHMz4MNhfWGmSfYvww66mmbH-kwkcdZTGxqGzHqkjpFL4YqOQv7g3GL4LE5ehLYe5tF6EKzcpnEqlrQ_xgNuFfu8f2NUdd8kEkPNshu-kRIZgyVVTaflPNZjoRpDXUg-8ZfZdG3PEyjc3unzGTNQ5or2BdMhZe3pav88XQsrd9Wf6vupNdgwSVSr_3D28hx78ymkhQ7qdhT6haFlFNwe7F_xSf0UEQ"
    def restClient = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve listar todas as comissoes da camara"(){
        def caminho = "sislegis/rest/comissaos/comissoesCamara"

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
        response.data.each{
            println it
        }
        println "Total: " + response.data.size()
    }

    def "deve listar todas as comissoes do senado"(){
        def caminho = "sislegis/rest/comissaos/comissoesSenado"

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
        response.data.each{
            println it
        }
        println "Total: " + response.data.size()
    }

}
