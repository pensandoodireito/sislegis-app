package br.gov.mj.sislegis.app.endpoint

import groovyx.net.http.RESTClient
import spock.lang.Specification

class ReuniaoEndpointSpec extends Specification{

    def token = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiJlY2ViMzBlYS01NmUwLTQ4YzItOGUyMi0yZWI1NzEzODhhYzMiLCJleHAiOjE0NDk2NzEwMjksIm5iZiI6MCwiaWF0IjoxNDQ5NjcwNzI5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2F1dGgvcmVhbG1zL3Npc2xlZ2lzIiwiYXVkIjoic2lzbGVnaXMiLCJzdWIiOiJkZjFiMDE5My03ZDc2LTRmMTUtYWFlNy1hY2NlNzY2ZjA2N2MiLCJhenAiOiJzaXNsZWdpcyIsInNlc3Npb25fc3RhdGUiOiJkM2UzZTdmYS1iMTE0LTQ4YzctYjE1NC1jNjI0YjJlZTY0NGQiLCJjbGllbnRfc2Vzc2lvbiI6IjJjZTIyODMzLTBlMzgtNDQzOC04YWQ3LTczYjg5NWU0MGE5MiIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vc2lzbGVnaXMubG9jYWwiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6Ikd1c3Rhdm8gRGVsZ2FkbyIsInByZWZlcnJlZF91c2VybmFtZSI6Imd1c3Rhdm8iLCJnaXZlbl9uYW1lIjoiR3VzdGF2byIsImZhbWlseV9uYW1lIjoiRGVsZ2FkbyIsImVtYWlsIjoiZ2NkZWxnYWRvQGdtYWlsLmNvbSJ9.F4-QMwKpgcS7L_Q0xsrYw4aL2DToCqv13GEcpaO-tAVhDCizjPmbe_U6dKCJuznGKXLjJEzDl20aUxaP1R7aOqP8lxcaKU816n93bJ48q2Eo-3_IbxvYv8-h8ORwTe7zsKGgY9kXDz18IpVIve2J5u9jiTKL0p-NVLeNpRTDmAJM2m9rCaaHutW_DvQewFKJSql2G1SDBb88BySyioTuT3kB67o_rTb5J3j0f65oY5EzvSABAmhDTtEoymini1We4Me2GrADxR-bq2SVgMAV9BelhJGJgRJSMP7Grew4X43sOswegOj-Rdir0PyzBsKcCbH74_HXkpM9qVV3kF_qCA"
    def client = new RESTClient("http://localhost:8080/")
    def cabecalho = [Authorization: token]

    def "deve listar reunioes por mes"(){

        given:
        def caminho = "/sislegis/rest/reuniaos/reunioesPorMes"
        def mes = 10
        def ano = 2015
        def query = [mes: mes, ano: ano]

        when:
        def resp = client.get(path: caminho, query: query, headers: cabecalho)

        then:
        assert resp.status == 200 // status 200 = Ok
        resp.data.each(){
            println it
        }
    }
}
