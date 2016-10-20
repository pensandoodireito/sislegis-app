package br.gov.mj.sislegis.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "areamerito")
@NamedQueries({ @NamedQuery(name = "findAreaByName", query = "select p from AreaDeMerito p where p.nome=:nome") })
public class AreaDeMerito extends AbstractEntity {

	private static final long serialVersionUID = -2801342641242367391L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(unique = true)
	private String nome;

	@Column(name = "contato_nome")
	private String nomeContato;
	@Column(name = "contato_email")
	private String emailContato;

	@OneToOne
	@JoinColumn(name = "contato_id", referencedColumnName = "id")
	private Usuario contato;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "areaMerito", fetch = FetchType.LAZY)
	private List<AreaDeMeritoRevisao> revisoes = new ArrayList<AreaDeMeritoRevisao>();

	@Override
	public Number getId() {

		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getContato() {
		return contato;
	}

	public void setContato(Usuario contato) {
		this.contato = contato;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public List<AreaDeMeritoRevisao> getRevisoes() {
		return revisoes;
	}

	public String getNomeContato() {
		return nomeContato;
	}

	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}

}
