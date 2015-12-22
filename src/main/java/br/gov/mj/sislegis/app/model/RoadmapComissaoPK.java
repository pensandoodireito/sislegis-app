package br.gov.mj.sislegis.app.model;

import java.io.Serializable;

import javax.persistence.Column;

public class RoadmapComissaoPK implements Serializable {

	private static final long serialVersionUID = 2558931643569722888L;
	@Column(nullable = false)
	Long proposicaoId;

	@Column(nullable = false)
	private String comissao;

	@Column(nullable = false)
	private Integer ordem;

	@Override
	public String toString() {

		return proposicaoId + "@" + comissao + "|" + ordem;
	}

	@Override
	public boolean equals(Object obj) {	
		if (obj == this) {
			return true;
		}
		if (proposicaoId == null || comissao == null || ordem == null) {
			return false;
		}

		if (obj instanceof RoadmapComissaoPK) {
			return proposicaoId == ((RoadmapComissaoPK) obj).proposicaoId && ordem == ((RoadmapComissaoPK) obj).ordem
					&& comissao.equals(((RoadmapComissaoPK) obj).comissao);
		}
		return false;

	}

	@Override
	public int hashCode() {
		return (int) (proposicaoId * 100 + ordem * 10 + comissao.hashCode());
	}

}
