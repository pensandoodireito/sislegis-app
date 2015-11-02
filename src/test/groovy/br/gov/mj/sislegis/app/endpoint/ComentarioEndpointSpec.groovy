package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComentarioEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost:8080/")
    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI0MjAzMDRiZi04MzVkLTQyMzAtOTkwYy0zYzNiMjViNDY2YTQiLCJleHAiOjE0NDY0Nzg4NjQsIm5iZiI6MCwiaWF0IjoxNDQ2NDc4NTY0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI3NmM0ZjE2NC1lMjVjLTQ0OGUtYmI4NC05ZTcyMDhmZWVkZDkiLCJjbGllbnRfc2Vzc2lvbiI6IjMxMzhiYTcyLTQ5YzgtNDAyZC1hNWRhLTk1MWYzYmI3NzQ5MSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.kzzFfHH5WGFs3y6m0lLzeJs1mwPcITgHhO8fdj3As6QNlpF6FYTqQ5vrfy15uQAGxk7ZjXqVMtHFbvPAXwI_1w6ZyIRefsbTSTtb6YnME48oh4ZA2zsdjMOFDF7y4Ol6Lhhsdyp_Bmdc1IhEZxVFiI5kgY7aY_qutjvNGcTtRX0G1y5kmVMThrvSh-HObELTutnaYxcnjGN7qunYb_ZL5Fua7_1k0gIR95a4gxWTB1Ojh-87eFitz9GOn5K2KuAT3kBWmDnO_gygUvJ7J5shX-5DsFQzT3Ac1AHSSprWh-zIsh7cSCwZHIxocLWRGhAeajI16j9IXxSDGLBobe13kg"
    def cabecalho = [Authorization: token]

    def "deve ocultar um comentario"(){
        def idComentario = 143
        def caminho = "sislegis/rest/comentarios/ocultar/" + idComentario

        when:
        def response = restClient.put(path: caminho, headers: cabecalho)

        then:
        assert response.status == 200 // status 200 = Ok
    }

}
