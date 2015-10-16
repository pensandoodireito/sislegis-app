package br.gov.mj.sislegis.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Reuniao extends AbstractEntity {

	private static final long serialVersionUID = -3187796439185752162L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	@Temporal(TemporalType.DATE)
	private Date data;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "reuniao")
	private Set<ReuniaoProposicao> listaReuniaoProposicoes = new HashSet<ReuniaoProposicao>();

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

	public Set<ReuniaoProposicao> getListaReuniaoProposicoes() {
		return listaReuniaoProposicoes;
	}

	public void setListaReuniaoProposicoes(Set<ReuniaoProposicao> listaReuniaoProposicoes) {
		this.listaReuniaoProposicoes = listaReuniaoProposicoes;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null)
			result += "id: " + id;
		if (data != null)
			result += ", data: " + data;
		return result;
	}

}