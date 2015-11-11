package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CompactProposicaoComComentarioSerializer extends CompactProposicaoSerializer {
	@Override
	public void serialize(Proposicao value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		writeProposicao(value, jgen);
		if (value.getListaComentario() != null && !value.getListaComentario().isEmpty()) {
			jgen.writeObjectField("ultimoComentario",
					value.getListaComentario().get(value.getListaComentario().size() - 1));
		}

		jgen.writeEndObject();
	}
}
