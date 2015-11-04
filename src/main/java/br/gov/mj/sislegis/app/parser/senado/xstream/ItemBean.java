package br.gov.mj.sislegis.app.parser.senado.xstream;

import br.gov.mj.sislegis.app.model.*;

public class ItemBean {
	protected Integer seqOrdemPauta;
	protected Proposicao proposicao;
	protected String tipo;
	protected Materia materia;
	protected Resultado resultado;

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public Proposicao getProposicao() {
		proposicao.setSeqOrdemPauta(seqOrdemPauta);
		return proposicao;
	}

	public Integer getSeqOrdemPauta() {
		return seqOrdemPauta;
	}
}