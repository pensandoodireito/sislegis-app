package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class TarefaEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI3MmM1ZmY0OC1mNmE4LTQxODMtYjM3Mi04MzY4YmU2ZGVmMjMiLCJleHAiOjE0NDU5NzY5ODQsIm5iZiI6MCwiaWF0IjoxNDQ1OTc2Njg0LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJmZTRkMjQ3NC03ZmI2LTRmMmUtOGFkZC1hNjA2ZGU0YTE0YjEiLCJjbGllbnRfc2Vzc2lvbiI6IjFiY2EyNmY4LWJjOGMtNGE1Mi1iMDBmLTk4NjVkMGM2NGJhOSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWw6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiR3VzdGF2byBEZWxnYWRvIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZ3VzdGF2byIsImdpdmVuX25hbWUiOiJHdXN0YXZvIiwiZmFtaWx5X25hbWUiOiJEZWxnYWRvIiwiZW1haWwiOiJnY2RlbGdhZG9AZ21haWwuY29tIn0.bfanAeFtBNG8PatwLnu0Nl6USYt9iFh4jWUECoXJD-BP6bLSqhk-RdDN5MAQ0LjLy5JiVuXuF5MdsdPWj7OJFMfY36veyaAbkFotmcsfmZjfXTcAe-YP8-eK90o6cxEOa4D4MGlkxg_v4QTBfEOmty44NnVXLOm4Kc_63Ma2kw55mgAzuYq-IbSoG0fOyVXfqg1H8Ugr6_MXcq7iK99CnY3o3mQeVNQ75xrAt_T_UnuJAPtFFXcXmadoaJrNZVbJLRgW9AloxrWuJY-yBu3azDOY0qZbp-YvoylrDjEaJtVBhRm09_NXq9j-JiuiNjewPRWlMCExywfs7T0CbX_fNA"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/146"
        def dados = [id: 146, data: 1444878216978, tipoTarefa: "ENCAMINHAMENTO", encaminhamentoProposicao: [id: 144, comentario: [id: 145, descricao: "asldfjasl", autor: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], dataCriacao: 1444878216936, proposicao: null], encaminhamento: [id: 18, nome: "Apenas Monitorar"], proposicao: [id: 142, idProposicao: null, tipo: null, ano: null, numero: null, autor: null, origem: null, resultadoASPAR: null, listaReuniaoProposicoes: null, comissao: null, seqOrdemPauta: null, sigla: "null null/null", ementa: null, linkProposicao: null, linkPauta: null, posicionamento: null, responsavel: null, tags: null, listaComentario: [], listaEncaminhamentoProposicao: [], reuniao: null, proposicoesPai: null, proposicoesFilha: null, elaboracoesNormativas: null, favorita: false], responsavel: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], dataHoraLimite: 1444964400000], usuario: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], proposicao: null, visualizada: false]

        when:
        def resp = client.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        println resp
    }

    def "deve finalizar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/finalizar"
        def dados = [idTarefa: 145, descricaoComentario: "Cometário de finalização"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }
}
