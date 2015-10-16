package br.gov.mj.sislegis.app.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class ReuniaoProposicao extends AbstractEntity {

	private static final long serialVersionUID = 7949894944142814382L;

	@EmbeddedId
	private ReuniaoProposicaoPK reuniaoProposicaoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idReuniao")
	private Reuniao reuniao;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idProposicao")
	private Proposicao proposicao;

	@Column
	private String siglaComissao;

	@Column
	private Integer seqOrdemPauta;

	@Column
	private String linkPauta;

	public String getSiglaComissao() {
		return siglaComissao;
	}

	public void setSiglaComissao(String siglaComissao) {
		this.siglaComissao = siglaComissao;
	}

	public Number getId() {
		return reuniaoProposicaoPK.hashCode();
	}

	public Reuniao getReuniao() {
		return reuniao;
	}

	public void setReuniao(Reuniao reuniao) {
		this.reuniao = reuniao;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public Integer getSeqOrdemPauta() {
		return seqOrdemPauta;
	}

	public String getLinkPauta() {
		return linkPauta;
	}

	public void setSeqOrdemPauta(Integer seqOrdemPauta) {
		this.seqOrdemPauta = seqOrdemPauta;
	}

	public void setLinkPauta(String linkPauta) {
		this.linkPauta = linkPauta;
	}

	public ReuniaoProposicaoPK getReuniaoProposicaoPK() {
		return reuniaoProposicaoPK;
	}

	public void setReuniaoProposicaoPK(ReuniaoProposicaoPK reuniaoProposicaoPK) {
		this.reuniaoProposicaoPK = reuniaoProposicaoPK;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		return result;
	}

}
