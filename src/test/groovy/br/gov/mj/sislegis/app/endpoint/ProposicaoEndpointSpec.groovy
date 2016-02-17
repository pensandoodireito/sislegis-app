package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoEndpointSpec extends Specification {

    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]

    def "deve buscar proposicoes por pauta - Camara"() {
        given:
        def caminho = "/sislegis/rest/proposicaos/proposicoesPautaCamara/"
        def idComissao = 2001 //CAPADR
        def data = "10/20/2015"
        def query = [idComissao: idComissao, data: data]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve buscar proposicoes por pauta - Senado"() {
        given:
        def caminho = "/sislegis/rest/proposicaos/proposicoesPautaSenado/"
        def siglaComissao = "CAE"
        def data = "02/15/2016"
        def query = [siglaComissao: siglaComissao, data: data]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve trazer dados detalhados da proposicao - Camara"() {
        given:
        def caminho = "/sislegis/rest/proposicaos/detalharProposicaoCamaraWS/"
        def idProposicao = 1786728
        def query = [id: idProposicao]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve trazer dados detalhados da proposicao - Senado"() {
        given:
        def caminho = "/sislegis/rest/proposicaos/detalharProposicaoSenadoWS/"
        def id = 120529
        def query = [id: id]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve salvar proposicao extra"() {
        given:

        def caminho = "/sislegis/rest/proposicaos/salvarProposicaoExtra"
        def dados = [
                id                           : null,
                idProposicao                 : 120529,
                tipo                         : "MSF",
                ano                          : "2015",
                numero                       : "00011",
                situacao                     : "INPAUTA",
                autor                        : "Presidente da República",
                origem                       : "SENADO",
                resultadoASPAR               : null,
                comissao                     : "SACAE",
                seqOrdemPauta                : null,
                sigla                        : "MSF 00011/2015",
                ementa                       : "Encaminha, nos termos do art. 6º da Lei nº 9.069, de 29 de junho de 1995, a Programação Monetária para o 1º trimestre e para o ano de 2015, contendo estimativas das faixas de variação dos principais agregados monetários, análise da evolução da economia nacional e justificativa da programação monetária.",
                linkProposicao               : "http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=120529",
                linkPauta                    : null,
                posicionamentoAtual          : null,
                posicionamentoPreliminar     : null,
                responsavel                  : null,
                tags                         : null,
                listaComentario              : [],
                listaEncaminhamentoProposicao: [],
                listaPautasComissao          : [],
                totalComentarios             : 0,
                totalEncaminhamentos         : 0,
                totalPautasComissao          : 0,
                proposicoesPai               : null,
                proposicoesFilha             : null,
                elaboracoesNormativas        : null,
                roadmapComissoes             : null,
                processosSei                 : null,
                favorita                     : false,
                pautaComissaoAtual           : null
        ]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 201 = Created
    }

    def "deve buscar a proposicao pelo id"() {
        given:
        def id = 150
        def caminho = "/sislegis/rest/proposicaos/" + id

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve consultar proposicoes pelos filtros"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/consultar"

        def query = [ementa    : "Programação Monetária para o 1º trimestre e para o ano de 2015",
                     autor     : "",
                     sigla     : "",
                     origem    : "",
                     isFavorita: "",
                     limit     : 5,
                     offset    : 0]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve atualizar os dados de uma proposicao"() {
        given:
        def id = 165
        def caminho = "/sislegis/rest/proposicaos/" + id

        def dados = [id          : 165,
                     idProposicao: 115949,
                     tipo        : "MSF",
                     ano         : 2014,
                     numero      : 00001,
                     autor       : "Presidente da República",
                     comissao    : "ATA-PLEN",
                     situacao    : "TPRS",
                     origem      : "SENADO",
                     responsavel : [id   : 1,
                                    nome : "Gustavo Delgado",
                                    email: "gcdelgado@gmail.com"]
        ]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve realizar uma busca independente de proposicao"() {
        given:
        def origem = "SENADO"
        def tipo = "MSF"
        def numero = "11"
        def ano = 2015
        def caminho = "/sislegis/rest/proposicaos/buscaIndependente/" + origem + "/" + tipo + "/" + ano

        def query = [numero: numero]

        when:
        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve listar os tipos de proposicao - Camara"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/listTipos/CAMARA"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve listar os tipos de proposicao - Senado"(){
        given:
        def caminho = "/sislegis/rest/proposicaos/listTipos/SENADO"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve marcar uma proposicao para ser seguida"(){
        given:
        def id = 165
        def caminho = "/sislegis/rest/proposicaos/follow/" + id

        when:
        def resp = restClient.post(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No content
    }

    def "deve marcar uma proposicao para nao ser mais seguida"(){
        given:
        def id = 165
        def caminho = "/sislegis/rest/proposicaos/follow/" + id

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No content
    }

    def "deve sincronizar dados da proposicao com a Camara ou Senado"(){
        given:
        def id = 165
        def caminho = "/sislegis/rest/proposicaos/check4updates/" + id

        when:
        def resp = restClient.post(path: caminho, headers: cabecalho)

        then:
        println resp;
    }

    def "deve alterar o posicionamento de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def dados = [id: 165, idPosicionamento: 7, preliminar: true]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar o historico de alteracoes de posicionamento"() {

        given:
        def id = 165
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }

    }

    def "deve listar pautas de uma proposicao"(){
        given:
        def id = 150
        def caminho = "/sislegis/rest/proposicaos/" + id + "/pautas"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        resp.data.each {
            println it
        }
    }

    def "deve atualizar o roadmap completo de comissoes de uma proposicao"() {

        given:
        def caminho = "/sislegis/rest/proposicaos/setRoadmapComissoes"
        def dados = [idProposicao: 165, comissoes: ['PLEN', 'CAPADR', 'CCJC']]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

}