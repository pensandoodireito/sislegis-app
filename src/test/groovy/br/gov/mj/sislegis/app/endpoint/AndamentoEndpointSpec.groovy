package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class AndamentoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJhNmNmZjI5Ny05NjNlLTQ3ZTgtODQxMS1kODZjMjAyNzEwOWIiLCJleHAiOjE0NDQ4NzE5NjQsIm5iZiI6MCwiaWF0IjoxNDQ0ODcxNjY0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvc2lzbGVnaXMiLCJhdWQiOiJzaXNsZWdpcyIsInN1YiI6ImRmMWIwMTkzLTdkNzYtNGYxNS1hYWU3LWFjY2U3NjZmMDY3YyIsImF6cCI6InNpc2xlZ2lzIiwic2Vzc2lvbl9zdGF0ZSI6IjY0ZjdlNGYyLTg5NTktNDZiMC05NDZiLTU1NDcyOGZhMWIxOSIsImNsaWVudF9zZXNzaW9uIjoiZTMyZWI1ZjEtMTIwMi00OWZlLWI1OGUtZmRjYTU1ODUxMmU1IiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zaXNsZWdpcy5sb2NhbDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJHdXN0YXZvIERlbGdhZG8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJndXN0YXZvIiwiZ2l2ZW5fbmFtZSI6Ikd1c3Rhdm8iLCJmYW1pbHlfbmFtZSI6IkRlbGdhZG8iLCJlbWFpbCI6ImdjZGVsZ2Fkb0BnbWFpbC5jb20ifQ.HmcAEI9UWyBthGVDHR9m2gc7t9ECEZtzZQl9zYEKUU2ZSQ8EsA3enK0uhF5NKtjeluuOLffWH8s5HuP6ogQBIl-Ja7osm35YpJL7fF44CxTPJc7C0QG2zAT8FROK_EyJLlUX6BxS5GdcvieQaH_lVktbaJSISORvaic8YEdrH-moVyrM81KhgwQJ5jkWSybYLXG1cf55oRcv6AZFmejJZ-y9xD6OP2_XJjjBr6YQO4mUkIud6w2QfB4hNEEoWEZ02qyJ63f-y2zL6OWg_18h8lvijz4QBL_Ou03OmzqKcIivcuU8UkPVQ6iE30x_TTmzaKiHB8n2NI9jK9pOwJ5fIg"
    def restClient = new RESTClient("http://localhost:8080/")

    def "deve inserir um novo andamento"(){

        def caminho = "sislegis/rest/andamentos/"
        def cabecalho = [Authorization: token]
        def dados = [proposicao:[id:"144"], descricao:"Andamento adicionado pelo teste unitário"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        println resp
    }

    def "deve retornar andamentos pelo id de uma proposicao" (){

        def idProposicao = 142
        def caminho = "sislegis/rest/andamentos/" + idProposicao
        def cabecalho = ['Authorization': token]

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data

        with (resp.data[0]) {
            id == 1
            descricao == "andamento x"
        }
    }
}
