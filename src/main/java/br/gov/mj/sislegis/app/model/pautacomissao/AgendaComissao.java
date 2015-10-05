package br.gov.mj.sislegis.app.model.pautacomissao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.mj.sislegis.app.model.AbstractEntity;
import br.gov.mj.sislegis.app.model.Casa;
import br.gov.mj.sislegis.app.model.Usuario;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "comissao", "casa" }))
//@formatter:off
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "getAllAgendasSeguidas",
            query   =   "SELECT * " +
                        "FROM AgendaComissao a where a.id in (select agendasseguidas_id from Usuario_Agendas)",
                        resultClass=AgendaComissao.class
    ), @NamedNativeQuery(
            name    =   "getSeguidoresAgenda",
            query   =   "SELECT * " +
                        "FROM Usuario a where a.id in (select usuario_id from Usuario_Agendas where agendasseguidas_id=:idAgenda)",
                        resultClass=Usuario.class
    )
})
@NamedQueries({
	@NamedQuery(
				name="getByCasaComissao", 
				query= "SELECT ag FROM AgendaComissao ag where ag.comissao=:comissao and ag.casa=:casa"
			)
})
//@formatter:on
public class AgendaComissao implements Serializable, AbstractEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column
	@Temporal(TemporalType.DATE)
	private Date dataReferencia;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaConsulta;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaAtualizacao;

	public Date getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	@Column
	private String comissao;

	@Column
	@Enumerated(EnumType.STRING)
	private Casa casa;

	@OneToMany(targetEntity = br.gov.mj.sislegis.app.model.pautacomissao.Sessao.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "agenda")
	private Set<Sessao> sessoes = new HashSet<Sessao>();

	@JsonIgnore
	public Set<Sessao> getSessoes() {
		return sessoes;
	}

	public Casa getCasa() {
		return casa;
	}

	public AgendaComissao() {

	}

	public AgendaComissao(Casa casa, String comissao, Date date) {
		this.casa = casa;
		this.comissao = comissao;
		this.dataReferencia = date;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public Date getUltimaConsulta() {
		return ultimaConsulta;
	}

	public String getComissao() {
		return comissao;
	}

	public void setConsultada() {
		ultimaConsulta = new Date();
	}

	public void setPautasAtualizadas() {
		ultimaAtualizacao = new Date();

	}

	public Sessao getSessao(String identificadorExterno) {
		if (sessoes != null) {
			for (Iterator iterator = sessoes.iterator(); iterator.hasNext();) {
				Sessao sessao = (Sessao) iterator.next();
				if (identificadorExterno.equals(sessao.getIdentificadorExterno())) {
					return sessao;
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof AgendaComissao) {
			AgendaComissao other = (AgendaComissao) obj;
			if (id != null) {
				if (id.equals(other.id)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void addSessao(Sessao sessaoWS) {
		sessaoWS.setAgenda(this);
		sessoes.add(sessaoWS);

	}

	public void setDataReferencia(Date time) {
		dataReferencia = time;
		sessoes.clear();
	}

}
