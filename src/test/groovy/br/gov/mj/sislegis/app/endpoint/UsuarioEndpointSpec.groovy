package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

class UsuarioEndpointSpec extends Specification{
    def restClient = new RESTClient("http://localhost/")
    def cabecalho = [Authorization: new BearerToken().obterToken()]
    def nomeUsuario = "teste123"

    def "deve inserir um novo usuario"(){
        given:
        def caminho = "/sislegis/rest/usuarios"
        def random = new Random()
        def emailAleatorio = random.nextInt()
        def email = emailAleatorio + "@mj.gov.br"
        def dados = [nome: nomeUsuario, email: email]

        when:
        def resp = restClient.post(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 201 // status 201 = Created
    }

    def "deve consultar um usuario pelo id"(){
        given:
        def usuario = selecionarUsuarioInseridoNoTeste()
        def caminho = "/sislegis/rest/usuarios/" + usuario.id

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        println resp.data
    }

    def "deve alterar o email do usuario inserido no teste anterior"(){
        given:
        def usuario = selecionarUsuarioInseridoNoTeste()
        def random = new Random()
        def emailAleatorio = random.nextInt()
        def email = emailAleatorio + "@mj.gov.br"
        def caminho = "/sislegis/rest/usuarios/" + usuario.id
        def dados = [id: usuario.id, nome: usuario.nome, email: email]

        when:
        def resp = restClient.put(path: caminho, body: dados, headers: cabecalho, requestContentType: ContentType.JSON)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve excluir usuario inserido no teste anterior"(){
        given:
        def caminho = "/sislegis/rest/usuarios/"
        def usuario = selecionarUsuarioInseridoNoTeste()
        def caminhoCompleto = caminho + usuario.id

        when:
        def resp = restClient.delete(path: caminhoCompleto, headers: cabecalho)

        then:
        assert resp.status == 204 // status 204 = No Content
    }

    def "deve listar as proposicoes seguidas"(){
        given:
        def caminho = "/sislegis/rest/usuarios/proposicoesSeguidas"

        when:
        def resp = restClient.get(path: caminho, headers: cabecalho)

        then:
        if (resp.data.size() > 0) {
            resp.data.each {
                println it
            }
        }
    }

    def "deve listar todos os usuarios"(){
        when:
        def usuarios = listarTodosUsuarios()

        then:
        usuarios.each{
            println it
        }
    }

    def listarTodosUsuarios(){
        def caminho = "/sislegis/rest/usuarios"
        def query = [start: 0, max: 300]

        def resp = restClient.get(path: caminho, query: query, headers: cabecalho)

        return resp.data
    }

    def selecionarUsuarioInseridoNoTeste(){
        def usuarios = listarTodosUsuarios()
        for (def usuario : usuarios){
            if (nomeUsuario.equals(usuario.nome)){
                return usuario
            }
        }
    }
}
