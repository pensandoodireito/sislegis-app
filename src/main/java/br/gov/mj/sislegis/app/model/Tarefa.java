package br.gov.mj.sislegis.app.model;

import br.gov.mj.sislegis.app.enumerated.TipoTarefa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "tarefa")
@XmlRootElement
public class Tarefa extends AbstractEntity {

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

	private boolean isFinalizada;

	@OneToOne(fetch = FetchType.EAGER)
	private EncaminhamentoProposicao encaminhamentoProposicao;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@Transient
	private Proposicao proposicao;

	private boolean isVisualizada;

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

	public EncaminhamentoProposicao getEncaminhamentoProposicao() {
		return encaminhamentoProposicao;
	}

	public void setEncaminhamentoProposicao(EncaminhamentoProposicao encaminhamentoProposicao) {
		this.encaminhamentoProposicao = encaminhamentoProposicao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public boolean isVisualizada() {
		return isVisualizada;
	}

	public void setVisualizada(boolean isVisualizada) {
		this.isVisualizada = isVisualizada;
	}

}