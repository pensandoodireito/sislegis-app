package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Registro de uma alteracao de uma proposicao
 * 
 * @author rafael.coutinho
 *
 */
@Entity
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlteracaoProposicao extends AbstractEntity implements Comparable<AlteracaoProposicao> {

	private static final long serialVersionUID = 7949894944142814382L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Proposicao proposicao;

	@Column(length = 2000)
	private String descricaoAlteracao;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	AlteracaoProposicao() {

	}

	public AlteracaoProposicao(Proposicao proposicao, String descricaoAlteracao, Date data) {
		super();
		this.proposicao = proposicao;
		this.descricaoAlteracao = descricaoAlteracao;
		if (descricaoAlteracao.length() > 2000) {
			this.descricaoAlteracao = descricaoAlteracao.substring(0, 1999);
		}
		this.data = data;
	}

	@Override
	public Number getId() {
		return id;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public String getDescricaoAlteracao() {
		return descricaoAlteracao;
	}

	public Date getData() {
		return data;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;

	}

	@Override
	public int compareTo(AlteracaoProposicao o) {
		if (data == null) {
			return -1;
		} else if (o.data == null) {
			return 1;
		}
		return data.compareTo(o.data);
	}

}
