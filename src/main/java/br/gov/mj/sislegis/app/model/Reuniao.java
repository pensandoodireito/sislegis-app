package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "reuniao_proposicao", joinColumns = { @JoinColumn(name = "reuniao_id") })
	private Set<Proposicao> proposicoes = new HashSet<Proposicao>();

	public Long getId() {
		return this.id;
	}

	public void addProposicao(Proposicao proposicao) {
		proposicoes.add(proposicao);
	}

	public Set<Proposicao> getProposicoes() {
		return proposicoes;
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