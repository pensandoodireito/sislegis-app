package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.CollectionLazyConverter;

public class ListMateriaClass extends CollectionLazyConverter<Proposicao, Materia> {

	public ListMateriaClass(List<Materia> materias) {
		super(materias);
	}

	@Override
	protected Proposicao convertKtoE(Materia next) {
		return next.toProposicao();
	}

}