package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJjYjY5MTdiNS1kNjlkLTQ3MDMtOTY1Ni0xODRmNTFhMTRmZDQiLCJleHAiOjE0NDcyNzIwMjgsIm5iZiI6MCwiaWF0IjoxNDQ3MjcxNzI4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI4ZjRiMDE3Ny01N2ViLTQ1YWItOWFiZi1lYThjZTNiZjUxZGMiLCJjbGllbnRfc2Vzc2lvbiI6ImQ2M2RiYjM2LWFlMGUtNDU1Yy1hMDk1LTZlZjg2YjU4ODNhMiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.rHLJAyCZnC03BvK1XbXSFL5NUarjAs_TGLI3lPrEJydjX_wUXibC5ep_UI5UwfXT8mD_LPuqvYv0iaOB4DUrclexN2FSXOhXM5CFszndwVXadrsGiTJBXzxkHz4Ee4JC5-cmEJ4KyByJq1PpxDAies-XsdUtOSRvjIL5BfsRrMpNZpZ_my2e_jUPh_iG4jKyL0VZtv27WZrNVzxHjXBjGcJuLnBvMdrQtJX8kyHBcZrsQLjbESpJvZYH0VLaALRQub3KsNWVXPuaht_LmT99Qdgg0iSrFW3RGuS6PO1uptp0Lcbb-ivnFpX5C9bDGa3J25uCPyjjGeyx-iLCPZrQTQ"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve alterar o posicionamento de uma proposicao"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def idProposicao = 143
        def idPosicionamento = 6
        def dados = [idProposicao: idProposicao, idPosicionamento: idPosicionamento]

        when:
        def resp = client.post(path: caminho, body: dados, headers: cabecalho, requestContentType: "application/x-www-form-urlencoded; charset=utf-8")

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}
