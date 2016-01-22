package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJhMTI3MDVlOS0xMjQ1LTRkMzItODcyNS0yNThiYzM0OWNiMDgiLCJleHAiOjE0NTM0MjI2MTIsIm5iZiI6MCwiaWF0IjoxNDUzNDIyMzEyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiIxNDczNzViYy1jNGI3LTRmOGEtODU0YS1iNDg0MDZjZDlhMzciLCJjbGllbnRfc2Vzc2lvbiI6IjUzZWQxMTUwLWMyMDAtNDBiNi1iOGQzLWZmYmU1MmM2ODU5MCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.PaXNY2AG9udvQirTpNwt92ZobYozoK40uExerObj-EFw_HYmL-g7rionT11fyZbBOo9Ayeek6pdo63r9Wf9gi0Y2N8Y7V44vTWQQ50tuVGhLL_cpmfl4OP6KOUCgMIOSMEsKTSTbO_Oy0ocrMPAcniuBkHUW7mnZtklIos0yHoY4pqW8y8gfYKlm1LatVMpwVCw_MbwcdjNKt2rjvR9m0-mrd1e3pDL4uWRFEUyoPgr-P-CSCBj9EtW6WICgeCyA1mSHeDg_m4H5kVovKGCANbrcSSpPFnUez_LOPZo_ueGhj316jbgGIAq7KMJ6bIyesByvylOvEAaZ1nBxODeU7A"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def id = 47
        def idPosicionamento = 7
        def preliminar = true
        def dados = [id: id, idPosicionamento: idPosicionamento, preliminar: preliminar]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar o historico de alteracoes de posicionamento"() {

        given:
        def id = 35
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = client.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }

    }

    def "deve atualizar o roadmap completo de comissoes de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/setRoadmapComissoes"
        def dados = [idProposicao: 29, comissoes: ['PLEN', 'CAPADR', 'CCJC']]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve inserir um processo do sei a partir do protocolo"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/vincularProcessoSei"
        def dados = [id: 125, protocolo: "16.0.000000002-0"]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve excluir um processo SEI pelo seu id"(){
        given:
        def idProcessoSei = 129;
        def caminho = "/sislegis/rest/proposicaos/excluirProcessoSei/" + idProcessoSei;

        when:
        def resp = client.delete(path: caminho, headers: cabecalho)

        then:
        asser resp.status = 204 // status 204 = no content
    }

}
