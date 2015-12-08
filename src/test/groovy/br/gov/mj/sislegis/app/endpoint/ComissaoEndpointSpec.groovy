package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComissaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI5ZDc3MzBkZC0yN2ExLTRmZGQtYTAzOC03NTgyMGViYWMxNDUiLCJleHAiOjE0NDk2MDQ1MTgsIm5iZiI6MCwiaWF0IjoxNDQ5NjA0MjE4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIyOWRkYTI4YS1kOGM2LTRlNGMtYWY4Yi05ODNlOTViZjA1NmUiLCJjbGllbnRfc2Vzc2lvbiI6IjBhMjQ3MDM2LTAzNDYtNDVjYy04ZGM0LTdhNWY4MTIwMDg4YSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.K149uojzJ57HTBJ60W3SgKhiJfRZA3wMkYinh3Z5U-_E3oum23A6YyGWHlks8w6SB2NO5EYUAqA8hwxqVIfl9CXMjkt6-smwsgga0bzLFTBtk4uFSEUKwhHXmIj5hlDPN9BbbOE04jCldCILzdz7inhE-N6pKyVqKbTPRLaHzkET_tZdP9VXtfG9T8f5CK2RKwvg9Z83U1wFtKfBDdyD-rFo21O8c6szzKgjnSdbgaSG8PyH1dpGMGb-Q-YCKeVM2SB69ieqcDEK4bhg_Cx5oQSEcdxzQv99jPfA7-ahBoa6QWP2SpR58B0M6d-jZ3xO-L7bDM4Qs4vzbtnErMxu2w"
    def restClient = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve listar todas as comissoes do banco de dados"(){
        def caminho = "sislegis/rest/comissaos/"

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
        response.data.each{
            println it
        }
    }

}
