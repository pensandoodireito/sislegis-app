package br.gov.mj.sislegis.app.integration

import groovyx.net.http.RESTClient
import spock.lang.Specification

class SenadoWSIntegrationSpec extends Specification{

    def "deve mapear e listar o valor de todos os campos da consulta de reunioes do Senado"(){

        given:
        def client = new RESTClient("http://legis.senado.leg.br")
        def codigoReuniao = "3400"
        def caminho = "/dadosabertos/reuniao/" + codigoReuniao
        def response
        def xml

        when:
        response = client.get(path: caminho)
        xml = response.data

        // TODO validar todos os campos relevantes ao sistema
        then:
        println xml.Codigo
        println xml.TituloDaReuniao
        println xml.Data
        println xml.Hora
        println xml.Tipo
        println xml.Situacao
        println xml.Realizada
        println xml.Local
        println xml.CodigoDoLocalDaReuniao

        assert !xml.Comissoes.isEmpty()

        println "COMISSOES"
        xml.Comissoes.Comissao.each{
            println it.CodigoColegiado
            println it.NumeroReuniao
            println it.Sigla
            println it.Nome
            println it.Casa
            println it.CodigoTipoColegiado
            println it.NomeTipoColegiado
            println it.ComissaoSuperior
        }

        println "PARTES"
        xml.Partes.Parte.each{
            println it.Codigo
            println it.NumOrdem
            println it.CodigoTipo
            println it.Tipo
            println it.NomeFantasia
            println it.ExisteItemTerminativo
            println it.Itens

            println "EVENTOS"
            it.Eventos.Evento.each{
                println it.Finalidade

                println "CONVIDADOS"
                it.Convidados.Convidado.each{
                    println it.NumOrdem
                    println it.Tratamento
                    println it.Nome
                    println it.Sexo
                    println it.Cargo
                    println it.CodigoConvidado
                }

                println "PARTICIPANTES"
                it.Participantes.Participante.each{
                    println it.NumOrdem
                    println it.Tratamento
                    println it.Nome
                    println it.Sexo
                    println it.Cargo
                    println it.Documentos
                }

                println "MATERIAS RELACIONADAS"
                it.MateriasRelacionadas.Materia.each{
                    println it.Codigo
                    println it.Subtipo
                    println it.DescricaoSubtipo
                    println it.Numero
                    println it.Ano
                    println it.Ementa
                    println it.Indexacao
                    println it.SiglaCasaIniciadora
                    println it.Prazos

                    println "AUTORIA"
                    it.Autoria.Autor.each{
                        println it.@tipo
                        println it.Codigo
                        println it.Nome
                        println it.Tratamento
                        println it.Foto
                        println it.Pagina
                        println it.Partido
                        println it.UF
                        println it.Principal
                        println it.TemOutrosAutores
                    }

                    println it.Relatoria
                    println it.Despachos

                    println "SITUACOES"
                    it.Situacoes.SituacaoAtual.each{
                        println it.Data
                        println it.DataLocal
                        println it.Sigla
                        println it.Descricao
                        println it.SiglaLocal
                        println it.NomeLocal
                    }

                    println "TEXTOS. Total = " + it.Textos.Texto.size()
                    it.Textos.Texto.each{
                        println it.Tipo
                        println it.Tipodoc
                        println it.MimeType
                        println it.Data
                        println it.Link
                        println it.NomeLocal
                    }

                    println it.Anexadas
                    println it.Votacoes
                    println it.IsComplementar
                    println it.TipoRelacao

                }

                println it.Anexos
                println it.ResultadoTexto

            }
        }

        println xml.IdDaUltimaPublicacaoPautaSimples
        println xml.IdDaUltimaPublicacaoResultado

    }

}
