package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ComissoesSenadoWSSpec extends Specification{

    def "deve validar a versao do ws, o retorno de comissoes e os campos relevantes"() {

        given:
        def client = new RESTClient("http://legis.senado.leg.br")
        def caminho = "/dadosabertos/comissao/lista/colegiados"
        def versaoHomologadaWS = 2
        def xmlComissoes

        when:
        def response = client.get(path: caminho)
        xmlComissoes = response.data

        then:
        assert xmlComissoes.Metadados.VersaoServico == versaoHomologadaWS

        xmlComissoes.Colegiados.Colegiado.each{
            assert !it.Codigo.isEmpty()
            assert !it.Sigla.isEmpty()
        }
    }
}
