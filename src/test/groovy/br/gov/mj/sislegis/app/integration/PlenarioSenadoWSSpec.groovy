package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class PlenarioSenadoWSSpec extends Specification {

    def "deve validar a versao do ws, o retorno de sessoes e os campos relevantes de cada materia"() {

        given:
        def client = new RESTClient("http://legis.senado.leg.br")
        def dataInicial = "20150901"
        def caminho = "/dadosabertos/plenario/agenda/mes/" + dataInicial
        def versaoHomologadaWS = 2
        def xmlComissoes

        when:
        def response = client.get(path: caminho)
        xmlComissoes = response.data

        then:
        assert xmlComissoes.Metadados.VersaoServico == versaoHomologadaWS

        xmlComissoes.Sessoes.Sessao.each{
            it.Materias.Materia.each{
                assert !it.CodigoMateria.isEmpty()
                assert !it.SiglaMateria.isEmpty()
                assert !it.NumeroMateria.isEmpty()
                assert !it.AnoMateria.isEmpty()
                assert !it.Ementa.isEmpty()
            }
        }

    }
}
