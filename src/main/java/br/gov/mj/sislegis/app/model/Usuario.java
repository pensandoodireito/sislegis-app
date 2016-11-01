package br.gov.mj.sislegis.app.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

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

	@ElementCollection(targetClass = Papel.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "usuario_papel")
	@Column(name = "papel")
	private Set<Papel> papeis = new HashSet<Papel>();

	@Column
	private String nome;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idequipe", referencedColumnName = "id", nullable = true)
	private Equipe equipe = null;

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
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" ").append(getId()).append(":").append(email).append(" > ").append(equipe);

		return sb.toString();
	}

	public void addPapel(Papel p) {
		papeis.add(p);
	}

	public void removePapel(Papel p) {
		papeis.remove(p);
	}

	public Set<Papel> getPapeis() {
		return papeis;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("nome", this.nome);
		json.put("email", this.email);
		if (equipe != null) {
			json.put("equipe", this.equipe.toJson());
		}

		return json;
	}

}