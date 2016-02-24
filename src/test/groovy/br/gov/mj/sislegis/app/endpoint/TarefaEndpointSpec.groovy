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
        assert resp.status == 200 // status 200 = Ok

        if (resp.data.size > 0) {
            resp.data.each {
                println it
            }
        }
    }

    def "deve finalizar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/finalizar"
        def tarefas = listarTodasTarefas()
        def idTarefa = tarefas[0].id
        def dados = [idTarefa: idTarefa, descricaoComentario: "Cometário de finalização"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar todas as tarefas"(){
        when:
        def tarefas = listarTodasTarefas()

        then:
        tarefas.each{
            println it
        }
    }

    def listarTodasTarefas(){
        def caminho = "/sislegis/rest/tarefas/"
        def query = [start: 0, max: 300]
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)
        return resp.data
    }
}
