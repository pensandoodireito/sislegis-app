package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CompactSetProposicaoSerializer extends JsonSerializer<Set<Proposicao>> {

	@Override
	public void serialize(Set<Proposicao> values, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		CompactProposicaoSerializer propCompact = new CompactProposicaoSerializer();
		jgen.writeStartArray();
		for (Iterator<Proposicao> iterator = values.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			propCompact.serialize(proposicao, jgen, provider);
		}

		jgen.writeEndArray();
	}

}
