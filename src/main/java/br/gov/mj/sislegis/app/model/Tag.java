package br.gov.mj.sislegis.app.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Tag extends AbstractEntity {

	private static final long serialVersionUID = 7949894944142814382L;

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String tag;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tag")
	private Set<TagProposicao> listaTagProposicoes = new HashSet<TagProposicao>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tag")
	private Set<TagElaboracaoNormativa> listaTagElaboracaoNormativa = new HashSet<TagElaboracaoNormativa>();

	@Override
	public Number getId() {
		return tag.hashCode();
	}

	@Override
	public String toString() {
		String result = tag;
		return result;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Set<TagProposicao> getListaTagProposicoes() {
		return listaTagProposicoes;
	}

	public void setListaTagProposicoes(Set<TagProposicao> listaTagProposicoes) {
		this.listaTagProposicoes = listaTagProposicoes;
	}

	public Set<TagElaboracaoNormativa> getListaTagElaboracaoNormativa() {
		return listaTagElaboracaoNormativa;
	}

	public void setListaTagElaboracaoNormativa(Set<TagElaboracaoNormativa> listaTagElaboracaoNormativa) {
		this.listaTagElaboracaoNormativa = listaTagElaboracaoNormativa;
	}

}
