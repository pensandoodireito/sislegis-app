package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamImplicitCollection;

@XStreamAlias("Relatoria")
public class Relatoria {
	@XStreamImplicit(itemFieldName = "Relator")
	List<Relator> relatores = new ArrayList<Relator>();

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Relatoria.class);
		xstream.processAnnotations(Relator.class);
		
	}

}
