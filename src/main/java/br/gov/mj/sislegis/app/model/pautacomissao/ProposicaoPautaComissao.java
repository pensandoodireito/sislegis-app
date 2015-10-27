package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import br.gov.mj.sislegis.app.model.Proposicao;

@Entity
@Table(name = "Proposicao_PautaComissao")
public class ProposicaoPautaComissao implements Serializable, Comparable<ProposicaoPautaComissao> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2853814379434569522L;

	public PautaReuniaoComissao getPautaReuniaoComissao() {
		return pautaReuniaoComissao;
	}

	public void setPautaReuniaoComissao(PautaReuniaoComissao pautaReuniaoComissao) {
		this.pautaReuniaoComissao = pautaReuniaoComissao;
	}

	public void setKey(PropostaPautaPK key) {
		this.key = key;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	@EmbeddedId
	PropostaPautaPK key = new PropostaPautaPK();

	@Column
	Integer ordemPauta = 0;

	@Column
	String relator;

	@MapsId("proposicaoId")
	@ManyToOne(fetch = FetchType.LAZY)
	Proposicao proposicao;

	@MapsId("pautaReuniaoComissaoId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pautaReuniaoComissaoId", referencedColumnName = "id", nullable = false)
	PautaReuniaoComissao pautaReuniaoComissao;

	ProposicaoPautaComissao() {

	}

	public PropostaPautaPK getKey() {
		return key;
	}

	public ProposicaoPautaComissao(PautaReuniaoComissao pc, Proposicao proposicao) {
		this.pautaReuniaoComissao = pc;
		this.proposicao = proposicao;
	}

	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	public PautaReuniaoComissao getPautaComissao() {
		return pautaReuniaoComissao;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public Integer getOrdemPauta() {
		return ordemPauta;
	}

	public void setOrdemPauta(Integer ordemPauta) {
		this.ordemPauta = ordemPauta;
	}

	@Override
	public int compareTo(ProposicaoPautaComissao o) {
		if (o.ordemPauta > ordemPauta) {
			return -1;
		} else if (o.ordemPauta < ordemPauta) {
			return 1;
		} else {

			return 0;
		}
	}

	@Override
	public String toString() {
		if (key != null) {
			StringBuilder sb = new StringBuilder(key.toString());
			sb.append(" ordem:" + ordemPauta + " Relator:{" + relator + "}");
			return sb.toString();
		}
		return super.toString();
	}
}
