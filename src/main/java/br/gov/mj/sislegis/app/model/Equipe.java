package br.gov.mj.sislegis.app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "equipe")
@XmlRootElement
public class Equipe extends AbstractEntity {

	private static final long serialVersionUID = 8516082010865687791L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column
	private String nome;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "equipe", cascade = { CascadeType.MERGE })
	private Set<Usuario> listaEquipeUsuario;

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

	@JsonIgnore
	public Set<Usuario> getListaEquipeUsuario() {
		return listaEquipeUsuario;
	}

	public void setListaEquipeUsuario(Set<Usuario> listaEquipeUsuario) {
		this.listaEquipeUsuario = listaEquipeUsuario;
	}

	@Override
	public String toString() {
		return new StringBuilder(getClass().getSimpleName()).append(" ").append(nome).append("@").append(id).toString();

	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("nome", this.nome);

		return json;
	}
}