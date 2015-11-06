package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class EncaminhamentoProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI3YjkwNTFiOS00ZDA1LTQxOTAtYmY5ZS01NDZkMmJiMjlhMDAiLCJleHAiOjE0NDU5ODA0OTgsIm5iZiI6MCwiaWF0IjoxNDQ1OTgwMTk4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI5ZDA4ZjY1OS00MjlmLTRjNzktOWFhYi0wMmE2N2NjZmE5ZjQiLCJjbGllbnRfc2Vzc2lvbiI6ImI1MDFjODRhLTcwOTMtNGMyYi1hYmQ5LTIyYmYyY2VjZGEwOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWw6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiR3VzdGF2byBEZWxnYWRvIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZ3VzdGF2byIsImdpdmVuX25hbWUiOiJHdXN0YXZvIiwiZmFtaWx5X25hbWUiOiJEZWxnYWRvIiwiZW1haWwiOiJnY2RlbGdhZG9AZ21haWwuY29tIn0.K3gwOLBsK7B_Dy3yQASKy9RkeYtAP56e5oK-1o5KsjZgJWZ6qbfvpDbP_OUTFUnMQYEi0YLNGuz4_ByYAhZMQZpWBz8bLPog8vdaMQ70DPC46hXsQQ7nliKUTseK_rMhVJhPEDvZJtmk5eNul5TwXIqlnA2MVbwQGsp1HYIiw0DtQYxKW2aQGq08pbRCWkfY3V7EP9IawrwzG_oz-iZFxZA8lbzqtkL2xLL9d0UrpUoj2l1Eql20hMISzkAckUAByk-z6aN4vedNRvxCKa0W_a4ymp1p3uOJI8gLwflVk8k2s-pH1ijUfR-6hDww5NSTshBGmn04P35eGdqWppoolA"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve finalizar um encaminhamento"(){

        given:
        def caminho = "/sislegis/rest/encaminhamentoProposicao/finalizar"
        def dados = [idEncaminhamentoProposicao: 143, descricaoComentario: "Cometário de finalização do encaminhamento"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }
}
