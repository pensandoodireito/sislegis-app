package br.gov.mj.sislegis.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "notificacao")
//@formatter:off
@NamedQueries({
	@NamedQuery(
				name="getAllNotificacaoByUsuario", 
				query= "SELECT t FROM Notificacao t WHERE t.usuario.id = :idUsuario order by t.criadaEm"
			),
			@NamedQuery(
					name="getCategoriaNotificacaoByUsuario", 
					query= "SELECT t FROM Notificacao t WHERE t.usuario.id = :idUsuario and t.categoria=:categoria order by t.criadaEm"
			),
			@NamedQuery(
					name="getByCategoriaEntidade", 
					query= "SELECT t FROM Notificacao t WHERE t.identificacaoEntidade = :identificacaoEntidade and t.categoria=:categoria"
			)
})
//@formatter:on
public class Notificacao extends AbstractEntity {

	private static final long serialVersionUID = -806063711060116952L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "identificacao_entidade", length = 10)
	private String identificacaoEntidade;

	@Column(name = "descricao", length = 128)
	private String descricao;

	@Column(length = 30)
	private String categoria;

	@Column(name = "criada_em")
	@Temporal(TemporalType.TIMESTAMP)
	private Date criadaEm;

	@Column(name = "vista_em", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date vistaEm;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	Notificacao() {
		criadaEm = new Date();
	}

	public Notificacao(Usuario usuario, String descricao, String identificacaoEntidade, String categoria) {
		this();
		this.usuario = usuario;
		this.categoria = categoria;
		this.descricao = descricao;
		this.identificacaoEntidade = identificacaoEntidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public Number getId() {
		return id;
	}

	public boolean isVisualizada() {
		return vistaEm != null;
	}

	public void setVisualizada() {
		this.vistaEm = new Date();
	}

	public String getIdentificacaoEntidade() {
		return identificacaoEntidade;
	}

	public void setIdentificacaoEntidade(String identificacaoEntidade) {
		this.identificacaoEntidade = identificacaoEntidade;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Date getCriadaEm() {
		return criadaEm;
	}

	public Date getVistaEm() {
		return vistaEm;
	}

	public void setVistaEm(Date vistaEm) {
		this.vistaEm = vistaEm;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
