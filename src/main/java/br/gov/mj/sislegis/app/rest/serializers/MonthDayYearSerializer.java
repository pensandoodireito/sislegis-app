package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MonthDayYearSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator jgen, SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
		String formattedDate = sdf.format(date);
		jgen.writeString(formattedDate);
	}

}
