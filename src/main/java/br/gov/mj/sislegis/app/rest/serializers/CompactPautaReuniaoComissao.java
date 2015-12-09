package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CompactPautaReuniaoComissao extends JsonSerializer<PautaReuniaoComissao> {
	@Override
	public void serialize(PautaReuniaoComissao obj, JsonGenerator jgen, SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		if (obj.getId() != null) {
			jgen.writeNumberField("id", obj.getId());
		}
		jgen.writeNumberField("codigoReuniao", obj.getCodigoReuniao());
		jgen.writeStringField("linkPauta", obj.getLinkPauta());
		jgen.writeStringField("comissao", obj.getComissao());
		jgen.writeNumberField("data", obj.getData().getTime());
		if (obj.isManual()) {
			jgen.writeBooleanField("manual", obj.isManual());
		}
		jgen.writeEndObject();
	}
}
