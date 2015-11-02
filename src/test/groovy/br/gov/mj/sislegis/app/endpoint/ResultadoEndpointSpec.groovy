package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ResultadoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJkOWNmMDEyNS01ZWI1LTRjMjQtYWNiMS02ZmZkNDA0MGYzNjQiLCJleHAiOjE0NDY0NzM5MjYsIm5iZiI6MCwiaWF0IjoxNDQ2NDczNjI2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJhYzE1NDJlMy1hZGRjLTQxMzgtYTIxOC1iMTIxYjI3OWM1MmUiLCJjbGllbnRfc2Vzc2lvbiI6IjI1ZGQ3MTM0LTcwYTQtNGY0Yy1hNjVhLTE4Yjk0Njg3YmE3YiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.M9OzbRIHCN3dUf3cMG-xbFGa06C9RokuzCtBswBwr-cY2R4Cq3bETVIr95vrwAzrHiyfkaTrtLxuAqV2KoNZFJP_JoQhwrglc_g81XVuVWJdlfpZ1r0BfQjldiGgcdYEofkBqnaC-PcpzT_y4dCF6n-BR5xxvyV8zTBFxpE2TEazS2yQelzj4SOTS0T_lU--ao1vC7JnIoC9T5InaNXk1d3AOR_uY5McpwfTzAdIlD__h8AHSYO28llqZvOfUmbHW3okXOR8AU2DtD83qruY3hKKFrDd_BNJDYTuoEra3Jcs9oWBmq8AogyslKlpHOWuprBWdiTm25r3cvGMcd2S6g"
    def restClient = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]
    def idReuniao = 141
    def idProposicao = 142

    def "deve inserir um novo resultado"(){

        def caminho = "sislegis/rest/resultados/"
        def dados = [reuniaoProposicao: [reuniaoProposicaoPK: [idReuniao: idReuniao, idProposicao: idProposicao]], descricao: "Resultado adicionado pelo teste unitário"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        assert resp.status == 201 // status 201 = Created

    }

    def "deve retornar resultados pelo id de uma reuniao e de uma proposicao" (){

        def caminho = "sislegis/rest/resultados/byReuniaoProposicao"
        def dados = [idReuniao: idReuniao, idProposicao: idProposicao]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        println resp.data

        with (resp.data[0]) {
            assert descricao == "Resultado adicionado pelo teste unitário"
        }
    }
}
