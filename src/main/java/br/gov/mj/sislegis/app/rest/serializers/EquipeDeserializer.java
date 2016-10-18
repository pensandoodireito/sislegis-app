package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.Equipe;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class EquipeDeserializer extends JsonDeserializer<Equipe> {

	@Override
	public Equipe deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		try {
			Equipe eq = new Equipe();
			boolean populou = false;
			while (jsonParser.nextValue() != null) {
				if ("id".equals(jsonParser.getCurrentName())) {
					eq.setId(jsonParser.getLongValue());
				} else if ("nome".equals(jsonParser.getCurrentName())) {
					populou = true;
					eq.setNome(jsonParser.getValueAsString());
				}
			}
			if (!populou) {
				return null;
			}
			return eq;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
