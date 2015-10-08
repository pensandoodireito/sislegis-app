package br.gov.mj.sislegis.app.parser.camara.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

class ComissaoConverter implements Converter {

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
		if ("orgaoNumerador".equals(reader.getNodeName())) {
			OrgaoNumerador au = (OrgaoNumerador) context.convertAnother(reader, OrgaoNumerador.class);
			if (au != null && au.sigla != null && au.sigla != null) {
				return au.sigla;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}