package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.mj.sislegis.app.enumerated.Origem;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "origem", "idExterno" }) })
@NamedQueries({ 
	@NamedQuery(
			name = "findByOrigem", 
			query = "select p from SituacaoLegislativa p where p.origem=:origem"),
			@NamedQuery(
					name = "findByIdExterno", 
					query = "select p from SituacaoLegislativa p where p.origem=:origem and p.idExterno=:idExterno")

})
@XmlRootElement
public class SituacaoLegislativa extends AbstractEntity {
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6022411264390324417L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column
	private Origem origem;

	@Column
	private Long idExterno;

	@Column
	private String descricao;

	@Column
	private String sigla;

	@Column
	private Boolean terminativa = false;

	@Column
	private Boolean obsoleta = false;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario atualizadoPor;

	@Column
	Date atualizadaEm;

	SituacaoLegislativa() {

	}

	SituacaoLegislativa(Origem origem, Long idExterno, String descricao) {
		this.origem = origem;
		this.idExterno = idExterno;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getTerminativa() {
		return terminativa;
	}

	public void setTerminativa(Boolean terminativa) {
		this.terminativa = terminativa;
	}

	public Usuario getAtualizadoPor() {
		return atualizadoPor;
	}

	public void setAtualizadoPor(Usuario atualizadoPor) {
		this.atualizadoPor = atualizadoPor;
	}

	public Date getAtualizadaEm() {
		return atualizadaEm;
	}

	public void setAtualizadaEm(Date atualizadaEm) {
		this.atualizadaEm = atualizadaEm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Origem getOrigem() {
		return origem;
	}

	public Long getIdExterno() {
		return idExterno;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Number getId() {
		return id;
	}

	public Boolean getObsoleta() {
		return obsoleta;
	}

	public void setObsoleta(Boolean obsoleta) {
		this.obsoleta = obsoleta;
	}
}
