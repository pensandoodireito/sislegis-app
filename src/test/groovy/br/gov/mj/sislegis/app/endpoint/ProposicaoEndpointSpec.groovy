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
                idProposicao                 : 49293,
                tipo                         : "SF",
                ano                          : "2001",
                numero                       : "00811",
                situacao                     : "Processado",
                autor                        : "CPI - Futebol - 2000",
                origem                       : "SENADO",
                resultadoASPAR               : null,
                comissao                     : "CE",
                seqOrdemPauta                : null,
                sigla                        : "SF 49293/2001",
                ementa                       : "Requer seja criada, no âmbito da Comissão de Educação, uma Subcomissão de Desportos, de caráter permanente, destinada a apreciar programas, planos e políticas governamentais instituídas para o setor desportivo no País.",
                linkProposicao               : "http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=49293",
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
        assert resp.status == 201 // status 201 = Created
    }

    def "deve buscar a proposicao inserida no teste anterior pelo id"() {
        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/" + id

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data
    }

    def "deve consultar por filtros a comissao inserida no teste anterior"() {
        when:
        def proposicoes = consultarPorFiltros()

        then:
        proposicoes.each {
            println it
        }
    }

    def "deve sincronizar dados da proposicao com a Camara ou Senado"(){
        given:
        def proposicoes = consultarPorFiltros()
        def id = proposicoes[0].id
        def caminho = "/sislegis/rest/proposicaos/check4updates/" + id

        when:
        def resp = restClient.post(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 202 || resp.status == 304; // Status 202 = Accepted; 304 = Not Modified
    }

    def "deve atualizar os dados da proposicao inserida no teste anterior"() {
        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/" + id

        def dados = [id          : id,
                     idProposicao: proposicao.idProposicao,
                     tipo        : proposicao.tipo,
                     ano         : proposicao.ano,
                     numero      : proposicao.numero,
                     ementa      : proposicao.ementa,
                     autor       : proposicao.autor,
                     comissao    : "ATA-PLEN",
                     situacao    : "TPRS",
                     origem      : "SENADO",
                     responsavel : [id   : 1]
        ]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve marcar a proposicao inserida no teste anterior para ser seguida"(){
        given:
        def proposicoes = consultarPorFiltros()
        def id = proposicoes[0].id
        def caminho = "/sislegis/rest/proposicaos/follow/" + id

        when:
        def resp = restClient.post(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No content
    }

    def "deve marcar a proposicao inserida no teste anterior para nao ser mais seguida"(){
        given:
        def proposicoes = consultarPorFiltros()
        def id = proposicoes[0].id
        def caminho = "/sislegis/rest/proposicaos/follow/" + id

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No content
    }

    def "deve alterar o posicionamento da proposicao inserida no teste anterior"() {

        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def idPosicionamento = null
        def caminho = "/sislegis/rest/proposicaos/alterarPosicionamento"
        def dados = [id: id, idPosicionamento: idPosicionamento, preliminar: true]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve listar o historico de alteracoes de posicionamento"() {

        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/historicoPosicionamentos/" + id

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data

    }

    def "deve listar pautas da proposicao inserida no teste anterior"(){
        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/" + id + "/pautas"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        println resp.data
    }

    def "deve atualizar o roadmap de comissoes da proposicao inserida no teste anterior"() {

        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/setRoadmapComissoes"
        def dados = [idProposicao: id, comissoes: ['PLEN', 'CAPADR', 'CCJC']]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

<<<<<<< HEAD
    def "deve limpar o roadmap de comissoes da proposicao inserida no teste anterior"() {

        given:
        def proposicoes = consultarPorFiltros()
        def proposicao = proposicoes[0]
        def id = proposicao.id
        def caminho = "/sislegis/rest/proposicaos/setRoadmapComissoes"
        def dados = [idProposicao: id, comissoes: []]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 200 // status 200 = Ok
    }

    def "deve excluir a proposicao inserida no teste anterior"(){
        given:
        def proposicoes = consultarPorFiltros()
        def id = proposicoes[0].id
        def caminho = "/sislegis/rest/proposicaos/" + id

        when:
        def resp = restClient.delete(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No content
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

    def consultarPorFiltros(){
        def caminho = "/sislegis/rest/proposicaos/consultar"

        def query = [ementa    : "Requer seja criada, no âmbito da Comissão de Educação, uma Subcomissão de Desportos, de caráter permanente, destinada a apreciar programas, planos e políticas governamentais instituídas para o setor desportivo no País.",
                     autor     : "CPI - Futebol - 2000",
                     sigla     : "RQS 00811/2001",
                     limit     : 5,
                     offset    : 0]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }

}
=======
    def "deve listar as votacoes de uma proposicao da CAMARA"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/listarVotacoes"
        def query = [idProposicao: "", tipo: "PL", numero: "1992", ano: "2007", origem: "CAMARA"]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it.dataHora
        }

    }

    def "deve listar as votacoes de uma proposicao do SENADO"(){

        given:
        def caminho = "/sislegis/rest/proposicaos/listarVotacoes"
        def query = [idProposicao: "112464", tipo: "", numero: "", ano: "", origem: "SENADO"]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        resp.data.each{
            println it.getDataHora
        }

    }

}
>>>>>>> 8084cb5cff4e7651040f49da04f1c4ca380f1aa8
