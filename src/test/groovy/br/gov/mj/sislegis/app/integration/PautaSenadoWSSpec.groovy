package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class PautaSenadoWSSpec extends Specification {

    def client = new RESTClient("http://legis.senado.leg.br")

    def "deve validar o retorno da listagem de reunioes a partir de uma data"(){

        given:
        def datIni = "20150901"
        def siglaComissao = "CDH"
        def caminho = "/dadosabertos/agenda/" + datIni
        def parametros = [colegiado : siglaComissao]
        def xmlReunioes

        when:
        def response = client.get(path: caminho, query : parametros)
        xmlReunioes = response.data

        then: "validar apenas o codigo de cada reuniao, que eh o unico campo usado nesta consulta"
        xmlReunioes.Reuniao.each {
            assert !it.Codigo.isEmpty()
        }

    }

    def "deve validar os campos de uma unica reuniao, utilizados pelo SISLEGIS"() {

        given:
        def xmlReuniao

        when: "reuniao com codigo 3401 possui eventos"
        xmlReuniao = chamarWebService("3401")

        then: "valida reuniao e seus eventos"
        validarReuniao(xmlReuniao)
        xmlReuniao.Partes.Parte.each {
            assert !it.Eventos.isEmpty()
            it.Eventos.Evento.each {
                assert !it.MateriasRelacionadas.isEmpty()
                it.MateriasRelacionadas.Materia.each {
                    validarMateria(it)
                }
            }
        }

        when: "reuniao com codigo 3402 possui Itens"
        xmlReuniao = chamarWebService("3402")

        then: "valida reuniao e seu itens"
        validarReuniao(xmlReuniao)
        xmlReuniao.Partes.Parte.each {
            assert !it.Itens.isEmpty()
            it.Itens.Item.each {
                assert !it.Materia.isEmpty()
                validarMateria(it.Materia)
            }
        }
    }

    def chamarWebService(codigoReuniao){
        def caminho = "/dadosabertos/reuniao/" + codigoReuniao
        def response = client.get(path: caminho)
        return response.data
    }

    void validarReuniao(xmlReuniao) {
        with(xmlReuniao) {
            assert !Codigo.isEmpty()
            assert !Comissoes.isEmpty()

            Comissoes.Comissao.each {
                assert !it.Sigla.isEmpty()
                assert !it.Nome.isEmpty()
            }

            assert !Partes.isEmpty();
        }
    }

    void validarMateria(materia) {
        with(materia) {
            assert !Codigo.isEmpty()
            assert !Subtipo.isEmpty()
            assert !Ano.isEmpty()
            assert !Numero.isEmpty()
            assert !Ementa.isEmpty()
            assert !Autoria.isEmpty()

            Autoria.Autor.each {
                assert !it.Nome.isEmpty()
            }
        }
    }
}
