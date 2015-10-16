package br.gov.mj.sislegis.app.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.mj.sislegis.app.enumerated.ElaboracaoNormativaSubTipo;
import br.gov.mj.sislegis.app.enumerated.ElaboracaoNormativaTipo;

@Entity
@XmlRootElement
public class ElaboracaoNormativaTiposMarcados extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7722617248451501605L;

	public ElaboracaoNormativaTiposMarcados() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ElaboracaoNormativa elaboracaoNormativa;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private ElaboracaoNormativaTipo tipo;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private ElaboracaoNormativaSubTipo subTipo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ElaboracaoNormativaTipo getTipo() {
		return tipo;
	}

	public void setTipo(ElaboracaoNormativaTipo tipo) {
		this.tipo = tipo;
	}

	public ElaboracaoNormativa getElaboracaoNormativa() {
		return elaboracaoNormativa;
	}

	public void setElaboracaoNormativa(ElaboracaoNormativa elaboracaoNormativa) {
		this.elaboracaoNormativa = elaboracaoNormativa;
	}

	public ElaboracaoNormativaSubTipo getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(ElaboracaoNormativaSubTipo subTipo) {
		this.subTipo = subTipo;
	}

}