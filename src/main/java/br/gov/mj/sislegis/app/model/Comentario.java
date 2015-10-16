package br.gov.mj.sislegis.app.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@XmlRootElement
@JsonIgnoreProperties({ "idProposicao" })
public class Comentario extends AbstractEntity {
	
	private static final long serialVersionUID = 739840933885769688L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(length = 8000)
	private String descricao;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario autor;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@ManyToOne(fetch = FetchType.EAGER)
	private Proposicao proposicao;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (descricao != null && !descricao.trim().isEmpty())
			result += "descricao: " + descricao;
		if (autor != null)
			result += ", autor: " + autor;
		return result;
	}

	public Proposicao getProposicao() {
		if (!Objects.isNull(this.proposicao)) {
			Proposicao p = new Proposicao();
			p.setId(proposicao.getId());
			this.proposicao = p;
		}
		return this.proposicao;
	}

	public void setProposicao(final Proposicao proposicao) {
		this.proposicao = proposicao;
	}
}