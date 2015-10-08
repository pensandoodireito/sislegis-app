package br.gov.mj.sislegis.app.parser.camara.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("autor1")
class Autor {
	String txtNomeAutor;
	String idecadastro;
	String codPartido;
	String txtSiglaPartido;
	String txtSiglaUF;
}