package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComentarioEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost:8080/")
    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIzODU2MDRhOS01NDBjLTQ0MDItYmViOS0yODg3YjZiZmQ1MjYiLCJleHAiOjE0NDY0Nzk1OTAsIm5iZiI6MCwiaWF0IjoxNDQ2NDc5MjkwLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI3NmM0ZjE2NC1lMjVjLTQ0OGUtYmI4NC05ZTcyMDhmZWVkZDkiLCJjbGllbnRfc2Vzc2lvbiI6ImZhYjc2M2FkLWNkZGUtNDgyZC05Mjk2LTI1ZjBiYjhkYjgyNSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.NpKcxhurNfBMMC0_yizNODIj66b4hY-Fn7bVaIwafl9hRdAN3UK7NAbBNW9IH8Msq4HNO78lzkLYYHQSZk6eDrqGSE1h4W_8JYOYjaXMdff0BiGaQ9_vFsBjsI5c9WLgKFKABR2kmuyu_3okYWBSWfjb5QkLcPVPYiz6bEFbrykUZ2iDabxP6Z1ps7KxbZzyLIMdE6ck8h6-Ua6wAdT2s5SPkX0YVpZTje0zJuaP5XtSWF5pkYWIj946tlifVNDllSL9mB0buwRNvNEk7xc7qbLNoAQVm62svk2G3Wl8C1ilVDc6vj8VYKWMwWZ3h3Hm70TFE-DF83Bib0IVBCW-cg"
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
