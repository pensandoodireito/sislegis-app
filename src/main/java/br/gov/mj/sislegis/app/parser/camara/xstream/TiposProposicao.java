package br.gov.mj.sislegis.app.parser.camara.xstream;

import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.app.parser.TipoProposicao;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("siglas")
class TiposProposicao {
	@XStreamImplicit(itemFieldName = "sigla")
	List<TipoProposicao> sigla = new ArrayList<TipoProposicao>();

	protected List<TipoProposicao> getSiglas() {
		return sigla;
	}
}