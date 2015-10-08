package br.gov.mj.sislegis.app.parser.camara.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

class AuthorConverter implements Converter {

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
		if ("autor1".equals(reader.getNodeName())) {
			Autor au = (Autor) context.convertAnother(reader, Autor.class);
			if (au != null && au.txtNomeAutor != null && au.txtNomeAutor != null) {
				return au.txtNomeAutor;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}