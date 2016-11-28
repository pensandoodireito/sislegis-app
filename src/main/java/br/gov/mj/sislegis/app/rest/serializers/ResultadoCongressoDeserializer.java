package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;

import br.gov.mj.sislegis.app.model.ResultadoCongresso;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ResultadoCongressoDeserializer extends JsonDeserializer<ResultadoCongresso> {
	@Override
	public ResultadoCongresso deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		try {
			if (jsonParser.getValueAsString() == null) {
				return null;
			}
			return ResultadoCongresso.valueOf(jsonParser.getValueAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
