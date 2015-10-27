package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.gov.mj.sislegis.app.model.AbstractEntity;
import br.gov.mj.sislegis.app.model.Comissao;

@Entity
@Table(name = "PautaReuniaoComissao", uniqueConstraints = @UniqueConstraint(columnNames = { "comissao", "date",
		"codigoReuniao" }))
public class PautaReuniaoComissao extends AbstractEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3945232017168732404L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column
	String comissao;
	@Column
	Date date;

	@Column
	protected Integer codigoReuniao;

	@Column
	String linkPauta;

	@Column
	protected String titulo;
	@Column
	protected String situacao;
	@Column
	protected String tipo;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pautaReuniaoComissao")
	@OrderBy("ordemPauta")
	SortedSet<ProposicaoPautaComissao> proposicoes = new TreeSet<ProposicaoPautaComissao>();

	PautaReuniaoComissao() {
	}

	public PautaReuniaoComissao(Date data, Comissao comissao, Integer codigoReuniao) {
		date = data;
		this.comissao = comissao.getSigla();
		this.codigoReuniao = codigoReuniao;
	}

	public String getComissao() {
		return comissao;
	}

	public Date getData() {
		return date;
	}

	public String getLinkPauta() {
		return linkPauta;
	}

	public void setLinkPauta(String linkPauta) {
		this.linkPauta = linkPauta;
	}

	public Integer getCodigoReuniao() {
		return codigoReuniao;
	}

	public void setCodigoReuniao(Integer codigoReuniao) {
		this.codigoReuniao = codigoReuniao;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public SortedSet<ProposicaoPautaComissao> getProposicoes() {
		return proposicoes;
	}

	public void setProposicoes(SortedSet<ProposicaoPautaComissao> proposicoes) {
		this.proposicoes = proposicoes;
	}

	public void addProposicaoPauta(ProposicaoPautaComissao ppc) {
		proposicoes.add(ppc);
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(comissao);
		sb.append(" Código:{").append(codigoReuniao).append("}").append(" contém ").append(proposicoes.size())
				.append(" proposicoes na pauta");
		return sb.toString();
	}

}
