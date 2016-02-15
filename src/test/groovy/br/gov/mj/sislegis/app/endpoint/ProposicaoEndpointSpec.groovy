package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
class ProposicaoEndpointSpec extends Specification {

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI4OWZkZDNhZi05YWU1LTRjYzItOWQyYS05YjAyMzg5ZjEyYzciLCJleHAiOjE0NTU1NjI1MzgsIm5iZiI6MCwiaWF0IjoxNDU1NTYyMjM4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiI1ZWU3Y2U3Ni1lMjEwLTRlYjYtOTY1NS00MzE5ZWIyNjg2NjQiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiI3NzQwNDM4My0xZTExLTQ5MmUtOGJhNS0wY2E0NzgzMThlNWYiLCJjbGllbnRfc2Vzc2lvbiI6ImNjODViZThlLThkNTYtNDUyNy05MTM5LWEyZWY3NDQ5MTFlNCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.bmqtmAvfDLpycVECaBS1l34XFjwF6qx2pS9vTzub9KVqNJT8rjrPLOX-sbwcufGV1RfZnhKFpFhq_GOz_lEuYtsJcmSGoZ7NLhCH9Zf2ey-SNLiaeE4mV1_2If6HG4PKiR71Fr2TsVI2lJPHAI5sbyTKYv-4MyDIKYWPUeiXP3mDPzRnIhbKncrsdTmCj0kVfPojDmcN1Na_R-VobDdjfnMgcsk9VUt0ffouxTQ7Q7l9mVgptuTAWOx1hPdG8Lt8xXuBIk6UTejmdamq_KdD6Ad9P_gsP0D7CX37BeKyqFvw9hDrcuT1Gix-PkrQ53iLNfZlZ1dzMjaCEDzY8TWzNw"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve buscar proposicoes por pauta - Camara"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/proposicoesPautaCamara/"
        def idComissao = 2001 //CAPADR
        def data = "02/15/2016"
        def query = [idComissao: idComissao, data: data]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve buscar proposicoes por pauta - Senado"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/proposicoesPautaSenado/"
        def siglaComissao = "CAE"
        def data = "02/15/2016"
        def query = [siglaComissao: siglaComissao, data: data]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve trazer dados detalhados da proposicao - Camara"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/detalharProposicaoCamaraWS/"
        def idProposicao = 1786728
        def query = [id: idProposicao]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve trazer dados detalhados da proposicao - Senado"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/detalharProposicaoSenadoWS/"
        def id = 120529
        def query = [id: id]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

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

}

import spock.lang.Specification
