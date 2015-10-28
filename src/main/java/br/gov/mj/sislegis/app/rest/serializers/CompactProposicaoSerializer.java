package br.gov.mj.sislegis.app.rest.serializers;

import br.gov.mj.sislegis.app.model.Proposicao;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class CompactProposicaoSerializer extends JsonSerializer<Proposicao> {

    @Override
    public void serialize(Proposicao value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("ementa", value.getEmenta());
        jgen.writeStringField("tipo", value.getTipo());
        jgen.writeStringField("numero", value.getNumero());
        jgen.writeStringField("ano", value.getAno());
        jgen.writeEndObject();
    }
}
