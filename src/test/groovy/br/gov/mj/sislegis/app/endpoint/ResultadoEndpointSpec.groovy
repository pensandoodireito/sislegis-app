package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ResultadoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjODYwNjI0Ny0xNTgzLTQ1ZGItYTJhYS0yYzdmZmZiZmVhMjciLCJleHAiOjE0NDYyMzc1MDgsIm5iZiI6MCwiaWF0IjoxNDQ2MjM3MjA4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI1MjJiYWE0ZS1hZDFlLTQzZDctYjdlZS04NTU1ZTNhYmJmZTYiLCJjbGllbnRfc2Vzc2lvbiI6ImIyMGNkMzgwLTNmY2QtNGI5My1hZTAxLTliNWZlNDJmZDc2ZiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.Dfhuj_dyhDKICNR4m0iCCBKJjr14-KKx1pN85jKreiNwUFmuIXyh0jMXLwmck3O7r9eTCYCPDdIOnbLMYlzniH4vGgpb_MyHFSpi0N98orLWvr12xYF54rrvvopUvH0WplHNnSERtsE7ptU_Y8YGPx0_JlQwu6E-guq4-HxUeRGZWF_VSjstn4gICdxLcNBHZ9Pcj35Ar9kcR95GrBsF-EmIp7jl1tcIpd2tcI25WKbbPgBusfzS0y8-70EW6shhHkqyX3PKsRem6RWrohdFCNadHtxbR-V3D5hi_yQ9VUv-k5Lbn3nBl3G99CQhE4SaGlXA_k_FDQ5U83oBsYnJAA"
    def restClient = new RESTClient("http://localhost:8080/")

    def "deve inserir um novo resultado"(){

        def caminho = "sislegis/rest/resultados/"
        def cabecalho = [Authorization: token]
        def dados = [reuniaoProposicao: [reuniaoProposicaoPK: [idReuniao: 141, idProposicao: 142]], descricao: "Resultado adicionado pelo teste unitário"]

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
