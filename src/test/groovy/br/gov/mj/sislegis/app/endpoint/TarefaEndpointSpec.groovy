package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class TarefaEndpointSpec extends Specification{

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve listar tarefas de um usuario"(){
        given:
        def caminho = "/sislegis/rest/tarefas/usuario"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve finalizar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/finalizar"
        def dados = [idTarefa: 186, descricaoComentario: "Cometário de finalização"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }
}
