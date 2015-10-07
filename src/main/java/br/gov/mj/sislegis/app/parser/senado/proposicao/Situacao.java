package br.gov.mj.sislegis.app.parser.senado.proposicao;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Situacao")
class Situacao {
	String DataSituacao;
	Integer CodigoSituacao;
	String SiglaSituacao;
	String DescricaoSituacao;
}
