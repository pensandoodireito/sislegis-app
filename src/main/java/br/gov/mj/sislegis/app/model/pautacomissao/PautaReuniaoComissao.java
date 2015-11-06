package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.AbstractEntity;
import br.gov.mj.sislegis.app.model.Comissao;

@Entity
@Table(name = "PautaReuniaoComissao", uniqueConstraints = @UniqueConstraint(columnNames = { "comissao", "data",	"codigoReuniao" }))
@NamedQueries({
		@NamedQuery(name = "findByCodigoReuniao", query = "select p from PautaReuniaoComissao p where p.codigoReuniao=:codigoReuniao"),
		@NamedQuery(name = "findByComissaoDataOrigem", query = "select p from PautaReuniaoComissao p where p.comissao=:comissao and p.data=:data and p.codigoReuniao=:codigoReuniao  "),
		@NamedQuery(name = "findByIntervaloDatas", query = "select p from PautaReuniaoComissao p where p.data between :dataInicial and :dataFinal"),
		@NamedQuery(name = "findPendentes", query = "select p from PautaReuniaoComissao p where p.data < current_date() and situacao in :situacoesPendente ")

})
public class PautaReuniaoComissao extends AbstractEntity implements Serializable, Comparable<PautaReuniaoComissao> {
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
	Date data;

	@Enumerated(EnumType.STRING)
	@Column
	private Origem origem;

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
	@Transient
	private boolean manual = false;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pautaReuniaoComissao")
	@OrderBy("ordemPauta")
	SortedSet<ProposicaoPautaComissao> proposicoesDaPauta = new TreeSet<ProposicaoPautaComissao>();

	PautaReuniaoComissao() {
	}

	public PautaReuniaoComissao(Date data, Comissao comissao, Integer codigoReuniao) {
		this.data = data;
		this.comissao = comissao.getSigla();
		this.codigoReuniao = codigoReuniao;
	}

	public PautaReuniaoComissao(Date data, String siglaComissao, Integer codigoReuniao) {
		this.data = data;
		this.comissao = siglaComissao;
		this.codigoReuniao = codigoReuniao;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public String getComissao() {
		return comissao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = date;
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

	public SortedSet<ProposicaoPautaComissao> getProposicoesDaPauta() {
		return proposicoesDaPauta;
	}

	public void setProposicoesDaPauta(SortedSet<ProposicaoPautaComissao> proposicoes) {
		this.proposicoesDaPauta = proposicoes;
	}

	public void addProposicaoPauta(ProposicaoPautaComissao ppc) {
		proposicoesDaPauta.add(ppc);
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(comissao);
		sb.append(" Código:{").append(codigoReuniao).append("}").append(" contém ").append(proposicoesDaPauta.size())
				.append(" proposicoes na pauta");
		return sb.toString();
	}

	@Override
	public int compareTo(PautaReuniaoComissao o) {
		if (o == null) {
			System.err.println("O nulo");
			return 1;
		}
		if (o.data == null) {
			System.err.println("O.data nulo");
			return 1;
		}
		if (data == null) {
			System.err.println("data nulo");
			return 1;
		}
		if (o.data.after(data)) {
			return -1;
		} else if (o.data.before(data)) {
			return 1;
		}
		return 0;
	}

}
