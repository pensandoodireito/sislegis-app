package br.gov.mj.sislegis.app.model.documentos;

import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;

public interface DocRelated {
	public Documento getDocumento();

	public void setDocumento(Documento documento);

	public Proposicao getProposicao();

	public Usuario getUsuario();

	public Number getId();
}
