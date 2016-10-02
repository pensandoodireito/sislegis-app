package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "areamerito_revisao")
@NamedQueries({
		@NamedQuery(name = "AreaDeMeritoRevisaoByProposicao", query = "SELECT ag FROM AreaDeMeritoRevisao ag where ag.proposicao.id=:prop"),
		@NamedQuery(name = "AreaDeMeritoRevisaoByArea", query = "SELECT ag FROM AreaDeMeritoRevisao ag where ag.areaMerito.id=:idArea") })
public class AreaDeMeritoRevisao extends AbstractEntity {

	private static final long serialVersionUID = -2801342641242367391L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "areamerito_id", referencedColumnName = "id")
	private AreaDeMerito areaMerito;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "posicionamento_id", referencedColumnName = "id", nullable = true)
	private Posicionamento posicionamento;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "proposicao_id", referencedColumnName = "id")
	private Proposicao proposicao;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao = new Date();

	@Column(name = "parecer")
	private String parecer;

	@Override
	public Number getId() {

		return id;
	}

	public AreaDeMerito getAreaMerito() {
		return areaMerito;
	}

	public void setAreaMerito(AreaDeMerito areaMerito) {
		this.areaMerito = areaMerito;
	}

	public Posicionamento getPosicionamento() {
		return posicionamento;
	}

	public void setPosicionamento(Posicionamento posicionamento) {
		dataAtualizacao = new Date();
		this.posicionamento = posicionamento;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public String getParecer() {
		return parecer;
	}

	public void setParecer(String parecer) {
		dataAtualizacao = new Date();
		this.parecer = parecer;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@JsonIgnore
	public void setPendente(boolean b) {

	}

	public boolean isPendente() {
		return posicionamento == null;
	}

}
