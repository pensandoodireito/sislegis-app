package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AuthorConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return String.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// Desnecessario, somente parseia XML->Objetos

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if ("Autoria".equals(reader.getNodeName())) {
			Autoria au = (Autoria) context.convertAnother(reader, Autoria.class);
			if (au != null && au.Autor != null && au.Autor.Nome != null) {
				return au.Autor.Nome;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}