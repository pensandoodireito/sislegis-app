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
@XStreamAlias("DetalheMateria")
public class DetalheMateriaV4 {
	@XStreamAlias("Metadados")
	Metadados metadados;

	@XStreamAlias("Materia")
	Materia materia;

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(DetalheMateriaV4.class);
		xstream.processAnnotations(Metadados.class);
		Materia.configXstream(xstream);

		// Por algum motivo o Detalha materia retorna autoria ao inves de
		// autoresprincipais. Temos que fazer o alias por isso.
		xstream.aliasField("Autoria", Materia.class, "autoresPrincipais");

		xstream.aliasField("Autor", AutoresPrincipais.class, "autores");

		// xstream.aliasField("Autoria", Materia.class, "AutoresPrincipais");
		//
		// xstream.aliasField("Autor", AutoresPrincipais.class, "Autoria");
		// xstream.alias("Autoria", AutoresPrincipais.class);
		// xstream.alias("Autor", AutorPrincipal.class);

	}

	public Proposicao getProposicao() {
		return materia.toProposicao();
	}
}