package br.gov.mj.sislegis.app.model;

import br.gov.mj.sislegis.app.rest.serializers.ResultadoCongressoDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ResultadoCongressoDeserializer.class)
public enum ResultadoCongresso {
	EM_TRAMITACAO, APROVADA, NAO_APROVADA, NAO_AVANCOU
}
