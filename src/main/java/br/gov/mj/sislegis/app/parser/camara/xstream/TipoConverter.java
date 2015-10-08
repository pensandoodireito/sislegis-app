package br.gov.mj.sislegis.app.parser.camara.xstream;

import br.gov.mj.sislegis.app.parser.TipoProposicao;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

class TipoConverter implements Converter {

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
		if ("tipoProposicao".equals(reader.getNodeName())) {
			TipoProposicao au = (TipoProposicao) context.convertAnother(reader, TipoProposicao.class);
			if (au != null && au.getNome() != null && au.getNome() != null) {
				return au.getNome();
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}