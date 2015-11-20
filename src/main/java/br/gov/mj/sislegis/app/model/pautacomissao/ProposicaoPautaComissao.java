package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.rest.serializers.CompactPautaReuniaoComissao;
import br.gov.mj.sislegis.app.rest.serializers.CompactProposicaoComComentarioSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "Proposicao_PautaComissao")
@IdClass(PropostaPautaPK.class)
public class ProposicaoPautaComissao implements Serializable, Comparable<ProposicaoPautaComissao> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2853814379434569522L;

	@Id
	Long proposicaoId;
	@Id
	Long pautaReuniaoComissaoId;

	@Column
	Integer ordemPauta = 0;

	@Column
	String relator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proposicaoId", updatable = false, insertable = false, referencedColumnName = "id", nullable = false)
	Proposicao proposicao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pautaReuniaoComissaoId", updatable = false, insertable = false, referencedColumnName = "id", nullable = false)
	PautaReuniaoComissao pautaReuniaoComissao;

	@Column
	String resultado;

	ProposicaoPautaComissao() {

	}

	// isto é o id da tabela de proposicao, e não o idproposicao que vem do WS!
	public Long getProposicaoId() {
		return proposicaoId;
	}

	public Long getPautaReuniaoComissaoId() {
		return pautaReuniaoComissaoId;
	}

	public ProposicaoPautaComissao(PautaReuniaoComissao pc, Proposicao prop) {
		setProposicao(prop);
		setPautaReuniaoComissao(pc);
	}

	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	@JsonSerialize(using = CompactProposicaoComComentarioSerializer.class)
	public Proposicao getProposicao() {
		if (proposicao != null && proposicao.getId() != proposicaoId) {
			proposicaoId = proposicao.getId();
		}
		return proposicao;
	}

	public Integer getOrdemPauta() {
		return ordemPauta;
	}

	public void setOrdemPauta(Integer ordemPauta) {
		this.ordemPauta = ordemPauta;
	}

	@JsonSerialize(using = CompactPautaReuniaoComissao.class)
	public PautaReuniaoComissao getPautaReuniaoComissao() {
		if (pautaReuniaoComissao != null && pautaReuniaoComissao.getId() != pautaReuniaoComissaoId) {
			pautaReuniaoComissaoId = pautaReuniaoComissao.getId();
		}
		return pautaReuniaoComissao;
	}

	public void setPautaReuniaoComissao(PautaReuniaoComissao pautaReuniaoComissao) {
		this.pautaReuniaoComissao = pautaReuniaoComissao;
		if (pautaReuniaoComissao != null) {
			pautaReuniaoComissaoId = pautaReuniaoComissao.getId();
		}
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
		if (proposicao != null) {
			proposicaoId = proposicao.getId();
		}
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	@Override
	public int compareTo(ProposicaoPautaComissao o) {
		if (o.ordemPauta == null) {
			return 1;
		}
		if (ordemPauta == null) {
			return -1;
		}
		if (o.ordemPauta > ordemPauta) {
			return -1;
		} else if (o.ordemPauta < ordemPauta) {
			return 1;
		} else {

			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;

		} else {
			if (obj instanceof ProposicaoPautaComissao) {
				if (this.proposicao == null || proposicaoId == null) {
					return false;
				}
				if (this.pautaReuniaoComissao == null || pautaReuniaoComissaoId == null) {
					return false;
				}
				if (this.pautaReuniaoComissaoId.equals(((ProposicaoPautaComissao) obj).getPautaReuniaoComissaoId())
						&& this.proposicaoId.equals(((ProposicaoPautaComissao) obj).getProposicaoId())) {
					return true;
				}

			}
		}
		return false;
	}

	@Override
	public String toString() {

		return proposicaoId + ":" + pautaReuniaoComissaoId + " (" + resultado + ")@" + super.hashCode();
	}
}
