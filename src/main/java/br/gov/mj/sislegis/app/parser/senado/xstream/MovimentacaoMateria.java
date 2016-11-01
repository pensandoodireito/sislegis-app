package br.gov.mj.sislegis.app.parser.senado.xstream;

import br.gov.mj.sislegis.app.model.Proposicao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * XStream mapper para o serviço
 * http://legis.senado.leg.br/dadosabertos/dados/DetalheMateriav4.xsd
 * 
 * @author coutinho
 *
 */
@XStreamAlias("MovimentacaoMateria")
public class MovimentacaoMateria {
	@XStreamAlias("Metadados")
	Metadados metadados;

	@XStreamAlias("Materia")
	Materia materia;

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(MovimentacaoMateria.class);
		xstream.processAnnotations(Metadados.class);
		xstream.processAnnotations(LocalTramitacao.class);
		Materia.configXstream(xstream);


	}

	public Proposicao getProposicao() {
		return materia.toProposicao();
	}

	public Materia getMateria() {
		return materia;
		
	}
}