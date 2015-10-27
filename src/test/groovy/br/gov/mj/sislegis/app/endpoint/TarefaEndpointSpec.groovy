package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class TarefaEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIyYmRjNjgyNy0xZjdmLTQxNjUtYjQwZC00NGE5NTk0ZjA0MmMiLCJleHAiOjE0NDQ4Nzk5OTEsIm5iZiI6MCwiaWF0IjoxNDQ0ODc5NjkxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvc2lzbGVnaXMiLCJhdWQiOiJzaXNsZWdpcyIsInN1YiI6ImRmMWIwMTkzLTdkNzYtNGYxNS1hYWU3LWFjY2U3NjZmMDY3YyIsImF6cCI6InNpc2xlZ2lzIiwic2Vzc2lvbl9zdGF0ZSI6IjE5ZDQ2NjE0LTM0OWItNGY0OC1iNDJlLTJiZTAxZmQ4YzkwYyIsImNsaWVudF9zZXNzaW9uIjoiZDE3MTBmMTItODY2MC00MDljLTgyOTEtNDMzYTg0NzRkYTBjIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9zaXNsZWdpcy5sb2NhbDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sIm5hbWUiOiJHdXN0YXZvIERlbGdhZG8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJndXN0YXZvIiwiZ2l2ZW5fbmFtZSI6Ikd1c3Rhdm8iLCJmYW1pbHlfbmFtZSI6IkRlbGdhZG8iLCJlbWFpbCI6ImdjZGVsZ2Fkb0BnbWFpbC5jb20ifQ.bw5rYhXiLYTRw19q-53VfYup5SMHOS3Ocng3T2z763S7hBCq4BmWyODsv7j3WveCOid39esOFBmsd5oAmpp_zgBwXTrD9sXNnCbMMKIXQi4D-iQrpQkbhI1gQksJgFDaiPnW59qwl12E3IjmFxLs57LSlfprfnK2tN3taad20r-eeDtt1GKfPV7U6M-DaSrJMbdhMbcgFuEU99B7CEVwT_2NNaSKeVLe7chbt1F8B8UzrlRxJJonv3ZmeO7NmRvSPWhROjymzZF08HK-cR5nW0btsCCRyPY8olav2mLOnOcw_ovnN-DoNaw3Qwd85Z7JNE_Ro2ukaNzCAQjdKpU95Q"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/146"
        def dados = [id: 146, data: 1444878216978, tipoTarefa: "ENCAMINHAMENTO", encaminhamentoProposicao: [id: 144, comentario: [id: 145, descricao: "asldfjasl", autor: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], dataCriacao: 1444878216936, proposicao: null], encaminhamento: [id: 18, nome: "Apenas Monitorar"], proposicao: [id: 142, idProposicao: null, tipo: null, ano: null, numero: null, autor: null, origem: null, resultadoASPAR: null, listaReuniaoProposicoes: null, comissao: null, seqOrdemPauta: null, sigla: "null null/null", ementa: null, linkProposicao: null, linkPauta: null, posicionamento: null, responsavel: null, tags: null, listaComentario: [], listaEncaminhamentoProposicao: [], reuniao: null, proposicoesPai: null, proposicoesFilha: null, elaboracoesNormativas: null, favorita: false], responsavel: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], dataHoraLimite: 1444964400000], usuario: [id: 140, nome: "Gustavo Delgado", email: "gcdelgado@gmail.com"], proposicao: null, visualizada: false]

        when:
        def response = client.put(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        println response
    }

    def "deve finalizar uma tarefa"(){

        given:
        def caminho = "/sislegis/rest/tarefas/finalizar"
        def comentario = "Finalizando tarefa executada"
        def dados = [idTarefa: idTarefa, comentario: comentario]

        when:
        client.post(path: caminho, body: dados, headers: cabecalho)

        then:
        println response;
    }
}
