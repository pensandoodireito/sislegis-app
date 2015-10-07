package br.gov.mj.sislegis.app.parser.camara.xstream;

import java.util.List;

import br.gov.mj.sislegis.app.parser.TipoProposicao;

/**
 * XStream Mapper para o endpoint:<br>
 * http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx?op=ListarSiglasTipoProposicao
 * 
 * @author coutinho
 *
 */
public class ListaSigla {
	protected List<TipoProposicao> siglas;

	public List<TipoProposicao> getSiglas() {
		return siglas;
	}
}