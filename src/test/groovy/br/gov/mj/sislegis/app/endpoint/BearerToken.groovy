package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import spock.lang.Specification

/**
 * Para que o Bearer possa ser obtido automaticamente, e necessario habilitar "Direct Grant API" no Admin do Keycloak
 * Settings -> Login -> Direct Grant API
 */
class BearerToken extends Specification{

    def obterToken(){
        def restClient = new RESTClient("http://localhost")
        def pathToken = "/auth/realms/sislegis/tokens/grants/access"
        def dados = [client_id: "sislegis", username: "sislegis", password: "@dmin123"]

        def response = restClient.post(path: pathToken, body: dados, requestContentType: ContentType.URLENC)

        return "Bearer $response.data.access_token"
    }

    def "deve obter o token"(){
        when:
        def token = obterToken()

        then:
        println token
    }
}
