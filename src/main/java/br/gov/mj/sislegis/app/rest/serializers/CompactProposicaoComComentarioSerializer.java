package br.gov.mj.sislegis.app.rest.serializers;

import java.io.IOException;
import java.util.List;

import br.gov.mj.sislegis.app.model.Comentario;
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
		List<Comentario> comentarios = value.getListaComentario();
		if (comentarios != null && !comentarios.isEmpty()) {
			Comentario ultimo = comentarios.get(comentarios.size() - 1);
			// remove a proposicao dele para não entrar em loop a serializacao
			ultimo.setProposicao(null);
			jgen.writeObjectField("ultimoComentario", ultimo);
		}

		jgen.writeEndObject();
	}
}
