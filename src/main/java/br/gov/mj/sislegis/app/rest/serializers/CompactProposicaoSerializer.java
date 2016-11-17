package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CompactProposicaoSerializer extends JsonSerializer<Proposicao> {

	@Override
	public void serialize(Proposicao value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		writeProposicao(value, jgen);

		jgen.writeEndObject();
	}

	protected void writeProposicao(Proposicao value, JsonGenerator jgen) throws IOException, JsonGenerationException {
		if (value.getId() != null) {
			jgen.writeNumberField("id", value.getId());
		}
		jgen.writeStringField("ementa", value.getEmenta());
		jgen.writeStringField("tipo", value.getTipo());
		jgen.writeStringField("numero", value.getNumero());
		jgen.writeStringField("ano", value.getAno());
		jgen.writeStringField("sigla", value.getSigla());
		jgen.writeStringField("comissao", value.getComissao());
		jgen.writeNumberField("idProposicao", value.getIdProposicao());
		jgen.writeStringField("origem", value.getOrigem().name());
		jgen.writeStringField("linkProposicao", value.getLinkProposicao());
	}
}
