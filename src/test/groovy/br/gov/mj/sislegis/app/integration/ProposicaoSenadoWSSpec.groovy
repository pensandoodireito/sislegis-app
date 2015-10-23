package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ProposicaoSenadoWSSpec extends Specification{

    def "deve validar a versao do ws, o retorno de materias e os campos relevantes de cada materia"() {

        given:
        def client = new RESTClient("http://legis.senado.leg.br")
        def idProposicao = 24257
        def caminho = "/dadosabertos/materia/"+idProposicao
        def versaoHomologadaWS = 3
        def parametros = [v : versaoHomologadaWS]

        when:
        def response = client.get(path: caminho, query : parametros)
        def xmlComissoes = response.data

        then:
        assert xmlComissoes.Metadados.VersaoServico == versaoHomologadaWS

        with (xmlComissoes.Materias.Materia) {
            assert !Codigo.isEmpty()
            assert !Subtipo.isEmpty()
            assert !Numero.isEmpty()
            assert !Ano.isEmpty()
            assert !Ementa.isEmpty()
            assert !Autoria.isEmpty()

            // considerando que poderia existir mais de um autor dentro de Autoria
            Autoria.Autor.each {
                assert !it.Nome.isEmpty()
            }
        }
    }
}
