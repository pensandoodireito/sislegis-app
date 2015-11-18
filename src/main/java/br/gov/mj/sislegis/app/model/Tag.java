package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Tag extends AbstractEntity {

	private static final long serialVersionUID = 7949894944142814382L;

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	@JsonIgnore
	// Apenas existe por ser um metodo obrigatorio de AbstractEntity
	public Number getId() {
		return null;
	}

	@Override
	public String toString() {
		return tag;
	}
}
