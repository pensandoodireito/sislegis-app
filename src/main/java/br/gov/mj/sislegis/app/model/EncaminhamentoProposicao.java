package br.gov.mj.sislegis.app.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.mj.sislegis.app.rest.serializers.CompactProposicaoSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@XmlRootElement
@JsonIgnoreProperties({ "idProposicao" })
public class EncaminhamentoProposicao extends AbstractEntity {

	private static final long serialVersionUID = 7949894944142814382L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Comentario comentario;

	@OneToOne(fetch = FetchType.EAGER)
	private Encaminhamento encaminhamento;

	@ManyToOne(fetch = FetchType.EAGER)
	private Proposicao proposicao;

	@OneToOne(fetch = FetchType.EAGER)
	private Usuario responsavel;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraLimite;

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}

	public Encaminhamento getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(Encaminhamento encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@JsonSerialize(using = CompactProposicaoSerializer.class)
	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataHoraLimite() {
		return dataHoraLimite;
	}

	public void setDataHoraLimite(Date dataHoraLimite) {
		this.dataHoraLimite = dataHoraLimite;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null)
			result += "id: " + id;
		if (comentario != null)
			result += ", comentario: " + comentario;
		if (encaminhamento != null)
			result += ", encaminhamento: " + encaminhamento;
		if (proposicao != null)
			result += ", proposicao: " + proposicao;
		if (responsavel != null)
			result += ", responsavel: " + responsavel;
		if (dataHoraLimite != null)
			result += ", dataHoraLimite: " + dataHoraLimite;
		return result;
	}
}
