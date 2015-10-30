package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ResultadoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJkYzhiYjEzYi1jMjJmLTQwOTUtYWJjYS1mMmNhNTQzOGIwZjQiLCJleHAiOjE0NDYyMzQwNjMsIm5iZiI6MCwiaWF0IjoxNDQ2MjMzNzYzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI1MDRmYzgxNC1iYTllLTQ3ZDMtYjdiNy0wMjcyZDZmOGRmMTYiLCJjbGllbnRfc2Vzc2lvbiI6ImI0N2JhMjJjLWY5ZjItNDRkYi05ZWVmLTY5MDEyNjBjNWYwNiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.fTAu3UcFYGawtkY2MHBrCh_au6ESBb2NCRs3GCl1zYBnlQCy_e2W4JMWwmdu3flbuHLSWDFiyaAbJc_9RlPaRAS_L0pbwGm9IXpjoYymfsOE31Iml_ESg46T4rAi6j05uQdaeqIDe9Y1-sL-021F5pZNeyRLPGgjt5B65r0PovDgAbcGpKqVtZ-YdToQ61PEwirDcd9IjjnTQVoYMCRtoagldOZ6lr0q8sFMVrP-drpJ8yGxzN79jAir94XUmOKVy-2Us6YVv4Gk1lwNjDagSqpsCIrO5o8hVAqZBEKjT2ncoal84o4DznXngF3pSPCGaHx4P45HImAcV-voa-kOvQ"
    def restClient = new RESTClient("http://localhost:8080/")

    def "deve inserir um novo resultado"(){

        def caminho = "sislegis/rest/resultados/"
        def cabecalho = [Authorization: token]
        def dados = [proposicao:[id:"142"], descricao:"Resultado adicionado pelo teste unitário"]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/json")

        then:
        assert resp.status == 201 // status 201 = Created

    }

    def "deve retornar resultados pelo id de uma proposicao" (){

        def idProposicao = 142
        def caminho = "sislegis/rest/resultados/proposicao/" + idProposicao
        def cabecalho = ['Authorization': token]

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data

        with (resp.data[0]) {
            assert descricao == "Resultado adicionado pelo teste unitário"
        }
    }
}
