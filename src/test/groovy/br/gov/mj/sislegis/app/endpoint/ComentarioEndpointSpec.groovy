package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComentarioEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost:8080/")
    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzY2JhOGVmNy1iNGFlLTRiODAtYWU1YS00YTE4MjQ1NWI2NTQiLCJleHAiOjE0NDY0OTMyNTQsIm5iZiI6MCwiaWF0IjoxNDQ2NDkyOTU0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJlNDlmOWZhOC1kMTMzLTQyMWYtODExZi1kMDQ0ZjM2YjIyNTAiLCJjbGllbnRfc2Vzc2lvbiI6ImQwNDFkYWUyLTRkNWYtNGY2OC05YmRlLTMwZWJjZTE1OWFmOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.B5slMUohJip6OINOHfEDLvvZWyi5BbhcA2egmd5UbaMZdLUHlfr3euUjNLoxqcyVkct3uOLBm-IXjUVPMc1v6c6lL5LfuPkY2xxlaOmmP3cTcOgU2VGnRaWp56LAr4Sz2Ew_INs-SEAFadGD6e4BuAhOvssfyiykfssci7c4Auo3h9GrPBLjGjSceOPAvgXvVhKtO-7glOSBBDKtXzP3NlU9D_xeRhrgTmpjdJ8CdYVhmlk7bQ-90yzGYvQ77A1OrTuSgEzuT8qP3J6zzs5d0eVud_L2UeDFqLSPO-4d8cCgG1-rrEez_eYPzUN1-Y2NzzsKnZBK20cF3apXbO46Rg"
    def cabecalho = [Authorization: token]

    def "deve ocultar um comentario"(){
        def idComentario = 147
        def caminho = "sislegis/rest/comentarios/ocultar/" + idComentario

        when:
        def response = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
    }

}
