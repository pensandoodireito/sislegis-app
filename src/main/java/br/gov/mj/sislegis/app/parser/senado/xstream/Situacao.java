package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Situacao")
class Situacao {
	String DataSituacao;
	Integer CodigoSituacao;
	String SiglaSituacao;
	String DescricaoSituacao;
}
