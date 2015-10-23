package br.gov.mj.sislegis.app.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.mj.sislegis.app.model.pautacomissao.AgendaComissao;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class Usuario extends AbstractEntity {

	private static final long serialVersionUID = -8092650497855683601L;

	public Usuario() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column
	private String nome;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Usuario_ProposicaoSeguida")
	private Set<Proposicao> proposicoesSeguidas = new HashSet<Proposicao>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Usuario_Agendas")
	private Set<AgendaComissao> agendasSeguidas = new HashSet<AgendaComissao>();

	@JsonIgnore
	public Set<Proposicao> getProposicoesSeguidas() {
		if (proposicoesSeguidas == null) {
			return new HashSet<Proposicao>();
		} else {
			return proposicoesSeguidas;
		}
	}

	@JsonIgnore
	public Set<AgendaComissao> getAgendasSeguidas() {
		if (agendasSeguidas == null) {
			return new HashSet<AgendaComissao>();
		} else {
			return agendasSeguidas;
		}
	}

	public void addAgendaSeguida(AgendaComissao ag) {
		agendasSeguidas.add(ag);
	}

	public void removeAgendaSeguida(AgendaComissao ag) {
		agendasSeguidas.remove(ag);
	}

	@Column(unique = true)
	private String email;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " " + getId() + ":";
		if (nome != null && !nome.trim().isEmpty())
			result += "nome: " + nome;
		if (email != null && !email.trim().isEmpty())
			result += ", email: " + email;
		return result;
	}

}