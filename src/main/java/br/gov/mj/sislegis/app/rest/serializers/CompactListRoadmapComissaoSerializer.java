package br.gov.mj.sislegis.app.rest.serializers;

import br.gov.mj.sislegis.app.model.RoadmapComissao;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class CompactListRoadmapComissaoSerializer extends JsonSerializer<List<RoadmapComissao>> {

    @Override
    public void serialize(List<RoadmapComissao> roadmapComissoes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        CompactRoadmapComissaoSerializer compactRoadmapComissaoSerializer = new CompactRoadmapComissaoSerializer();
        jsonGenerator.writeStartArray();
        for (RoadmapComissao roadmapComissao : roadmapComissoes){
            compactRoadmapComissaoSerializer.serialize(roadmapComissao, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndArray();
    }
}
