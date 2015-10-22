package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("IdentificacaoMateria")
class IdentificacaoMateria {

	Integer CodigoMateria;

	String IndicadorTramitando;
	String SiglaCasaIdentificacaoMateria;
	String NomeCasaIdentificacaoMateria;
	String SiglaSubtipoMateria;
	String DescricaoSubtipoMateria;
	String NumeroMateria;
	String AnoMateria;

	@Override
	public String toString() {

		return CodigoMateria + " " + IndicadorTramitando + " " + SiglaCasaIdentificacaoMateria + " " + AnoMateria + " "
				+ NumeroMateria;
	}
}
