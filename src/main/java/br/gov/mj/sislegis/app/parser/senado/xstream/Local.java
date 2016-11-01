package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Local")
class Local {
	String DataLocal;
	String CodigoLocal;
	String SiglaCasaLocal;
	String NomeCasaLocal;
	String SiglaLocal;
	String NomeLocal;
	
	@Override
	public String toString() {
		return NomeLocal+" Sigla: "+SiglaCasaLocal+ " Codigo:"+CodigoLocal;
	}

}
