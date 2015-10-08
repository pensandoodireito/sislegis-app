package br.gov.mj.sislegis.app.parser.camara;

public class OrgaoCamara {
	Long id;
	String idTipodeOrgao;
	String sigla;
	String descricao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdTipodeOrgao() {
		return idTipodeOrgao;
	}
	public void setIdTipodeOrgao(String idTipodeOrgao) {
		this.idTipodeOrgao = idTipodeOrgao;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}