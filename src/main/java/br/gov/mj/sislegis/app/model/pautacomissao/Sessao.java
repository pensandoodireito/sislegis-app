package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "getByIdExterno", query = "SELECT s FROM Sessao s where s.identificadorExterno=:idExterno") })
public class Sessao implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private SituacaoSessao situacao;

	@ManyToOne
	@JoinColumn(name = "agenda_id", referencedColumnName = "id", nullable = false)
	private AgendaComissao agenda;

	@Column
	private String titulo;

	@Column
	private String identificadorExterno;

	public void popula(Sessao sessaoWS) {
		data = sessaoWS.getData();
		situacao = sessaoWS.getSituacao();
		titulo = sessaoWS.getTitulo();

	}

	public String getIdentificadorExterno() {
		return identificadorExterno;
	}

	public void setIdentificadorExterno(String identificadorExterno) {
		this.identificadorExterno = identificadorExterno;
	}

	public AgendaComissao getAgenda() {
		return agenda;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public SituacaoSessao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoSessao situacao) {
		this.situacao = situacao;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Long getId() {
		return id;
	}

	public void setAgenda(AgendaComissao agenda) {
		this.agenda = agenda;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Sessao) {
			Sessao other = (Sessao) obj;
			if (id != null) {
				if (id.equals(other.id)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
