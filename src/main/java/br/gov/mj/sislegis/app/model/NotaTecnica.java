package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "proposicao_notatecnica")
public class NotaTecnica extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901057708852072015L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@JsonIgnore
	@ManyToOne(optional = false)
	private Proposicao proposicao;

	@ManyToOne(optional = false)
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao = new Date();

	@Column(length = 20000)
	private String nota;

	protected NotaTecnica() {

	}

	public NotaTecnica(Proposicao p, Usuario u) {
		this.proposicao = p;
		this.usuario = u;
		dataCriacao = new Date();
	}

	@Override
	public Number getId() {
		return id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}
}
