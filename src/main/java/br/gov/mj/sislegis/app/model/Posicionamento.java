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
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class Posicionamento extends AbstractEntity {

	private static final long serialVersionUID = -5843916678553628190L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(unique = true)
	private String nome;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "posicionamento", fetch = FetchType.LAZY)
	private List<PosicionamentoProposicao> posicoes = new ArrayList<PosicionamentoProposicao>();
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "posicionamento", fetch = FetchType.LAZY)
	private List<AreaDeMeritoRevisao> revisoes = new ArrayList<AreaDeMeritoRevisao>();

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

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (nome != null && !nome.trim().isEmpty())
			result += "nome: " + nome;
		return result;
	}

	@JsonIgnore
	public List<PosicionamentoProposicao> getPosicoes() {
		return posicoes;
	}
}