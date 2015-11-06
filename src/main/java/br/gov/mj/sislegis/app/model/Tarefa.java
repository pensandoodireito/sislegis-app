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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.mj.sislegis.app.enumerated.TipoTarefa;

@Entity
@Table(name = "tarefa")
@XmlRootElement
public class Tarefa extends AbstractEntity {
	public static Tarefa createTarefaEncaminhamento(Usuario usuario, EncaminhamentoProposicao encaminhamento) {
		Tarefa tarefa = new Tarefa(TipoTarefa.ENCAMINHAMENTO, usuario);
		tarefa.encaminhamento = encaminhamento;
		return tarefa;
	}

	private static final long serialVersionUID = -806063711060116952L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "data")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private TipoTarefa tipoTarefa;

	@Column
	private boolean isVisualizada;
	@Column
	private boolean isFinalizada;

	@ManyToOne(fetch = FetchType.EAGER)
	EncaminhamentoProposicao encaminhamento = new EncaminhamentoProposicao();

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@Transient
	private Proposicao proposicao;

	Tarefa() {
	}

	Tarefa(TipoTarefa tipo, Usuario user) {
		this.tipoTarefa = tipo;
		this.usuario = user;
		this.data = new Date();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoTarefa getTipoTarefa() {
		return tipoTarefa;
	}

	public void setTipoTarefa(TipoTarefa tipoTarefa) {
		this.tipoTarefa = tipoTarefa;
	}

	public boolean isFinalizada() {
		return isFinalizada;
	}

	public void setFinalizada(boolean isFinalizada) {
		this.isFinalizada = isFinalizada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isVisualizada() {
		return isVisualizada;
	}

	public void setVisualizada(boolean isVisualizada) {
		this.isVisualizada = isVisualizada;
	}

	public EncaminhamentoProposicao getEncaminhamentoProposicao() {
		if (!TipoTarefa.ENCAMINHAMENTO.equals(tipoTarefa)) {
			throw new IllegalArgumentException("Esta tarefa nao foi criada a partir de um encaminhamento");
		}
		return encaminhamento;
	}

	public void setEncaminhamentoProposicao(EncaminhamentoProposicao ent) {

		encaminhamento = ent;
	}

}