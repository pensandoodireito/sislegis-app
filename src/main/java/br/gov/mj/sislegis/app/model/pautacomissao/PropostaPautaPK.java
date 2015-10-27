package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class PropostaPautaPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2558931693569722888L;
	@Column(nullable = false)
	Long proposicaoId;
	@Column(nullable = false)
	Long pautaReuniaoComissaoId;

	public long getProposicaoId() {
		return proposicaoId;
	}

	public void setProposicaoId(Long proposicaoId) {
		this.proposicaoId = proposicaoId;
	}

	public long getPautaReuniaoComissaoId() {
		return pautaReuniaoComissaoId;
	}

	public void setPautaReuniaoComissaoId(Long pautaReuniaoComissaoId) {
		this.pautaReuniaoComissaoId = pautaReuniaoComissaoId;
	}

	@Override
	public String toString() {

		return proposicaoId + "@" + pautaReuniaoComissaoId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropostaPautaPK) {
			return proposicaoId == ((PropostaPautaPK) obj).proposicaoId
					&& pautaReuniaoComissaoId == ((PropostaPautaPK) obj).pautaReuniaoComissaoId;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) (pautaReuniaoComissaoId * 100 + proposicaoId);
	}
}