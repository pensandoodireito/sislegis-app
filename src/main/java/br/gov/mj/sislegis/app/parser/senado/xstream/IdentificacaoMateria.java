package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("IdentificacaoMateria")
class IdentificacaoMateria {

	Integer CodigoMateria;
	String SiglaCasaIdentificacaoMateria;
	String NomeCasaIdentificacaoMateria;
	String SiglaSubtipoMateria;
	String DescricaoSubtipoMateria;
	String NumeroMateria;
	String AnoMateria;
}
