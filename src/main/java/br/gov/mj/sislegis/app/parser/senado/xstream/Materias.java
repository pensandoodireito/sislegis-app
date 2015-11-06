package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Materias")
public class Materias {
	@XStreamImplicit(itemFieldName = "Materia")
	public List<Materia> materias;

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Materias.class);
		Materia.configXstream(xstream);

	}
}