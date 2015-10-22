package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * XStream mapper para o serviço XSD:
 * http://legis.senado.gov.br/dadosabertos/dados/PesquisaBasicaMateriav4.xsd<br>
 * Documentacao:
 * http://legis.senado.gov.br/dadosabertos/docs/path__materia_pesquisa_lista
 * .html<br>
 * 
 * @author coutinho
 *
 */
@XStreamAlias("PesquisaBasicaMateria")
public class PesquisaBasicaMateria {
	@XStreamAlias("Materias")
	Materias materias;
	@XStreamAlias("DescricaoDataSet")
	String descricaoDataSet;

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(PesquisaBasicaMateria.class);
		Materias.configXstream(xstream);
	}

	public List<Materia> getMaterias() {
		if (materias == null) {
			return new ArrayList<Materia>();
		}
		return materias.materias;
	}

	public String getDescricaoResposta() {
		return descricaoDataSet;
	}

}