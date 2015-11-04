package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CompactProposicao extends JsonSerializer<Proposicao> {

	@Override
	public void serialize(Proposicao arg0, JsonGenerator jgen, SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		if (arg0.getId() != null) {
			jgen.writeNumberField("id", arg0.getId());
		}
		jgen.writeNumberField("idProposicao", arg0.getIdProposicao());
		jgen.writeStringField("ementa", arg0.getEmenta());
		jgen.writeStringField("sigla", arg0.getSigla());
		jgen.writeStringField("origem", arg0.getOrigem().name());

		jgen.writeEndObject();
	}

}
