package br.gov.mj.sislegis.app.model;

import br.gov.mj.sislegis.app.rest.serializers.EfetividadeSALDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = EfetividadeSALDeserializer.class)
public enum EfetividadeSAL {
	ACATADA, ACATADA_PONTOS_IMPORTANTES, ACATADA_PONTOS_NAO_IMPORTANTES, NAO_ACATADA
}
