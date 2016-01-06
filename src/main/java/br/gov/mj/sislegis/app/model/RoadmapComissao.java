package br.gov.mj.sislegis.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Roadmap (roteiro) de Comissoes por onde uma Proposicao deve tramitar
 */

@Entity
@IdClass(RoadmapComissaoPK.class)
@Table(name = "roadmap_comissao")
public class RoadmapComissao implements Serializable {

	@Id
	private Long proposicaoId;

	@Id
	private String comissao;

	@Id
	private Integer ordem;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "proposicaoId", updatable = false, insertable = false, referencedColumnName = "id", nullable = false)
	Proposicao proposicao;

	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public String getComissao() {
		return comissao;
	}

	public void setComissao(String comissao) {
		this.comissao = comissao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		RoadmapComissao that = (RoadmapComissao) o;
		if (proposicaoId != null ? !proposicaoId.equals(that.proposicaoId) : that.proposicaoId != null)
			return false;
		if (comissao != null ? !comissao.equals(that.comissao) : that.comissao != null)
			return false;
		return !(ordem != null ? !ordem.equals(that.ordem) : that.ordem != null);

	}

	@Override
	public int hashCode() {
		int result = proposicao != null ? proposicao.hashCode() : 0;
		result = 31 * result + (comissao != null ? comissao.hashCode() : 0);
		result = 31 * result + (ordem != null ? ordem.hashCode() : 0);
		return result;
	}
}