package br.gov.mj.sislegis.app.model.documentos;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.mj.sislegis.app.model.AbstractEntity;
import br.gov.mj.sislegis.app.model.Documento;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.Usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "proposicao_notatecnica")
@NamedQueries({ 
	@NamedQuery(name = "listNotatecnicaProposicao", query = "select n from NotaTecnica n where n.proposicao.id=:idProposicao"),
	@NamedQuery(name = "listNotaTecnicaByUser", query = "select n from NotaTecnica n where n.documento.usuario.id=:userId"),
	@NamedQuery(name = "listNotasProposicaoPorUsuarioData", query = "select n from NotaTecnica n where n.usuario.id=:userId and n.dataCriacao>:s and n.dataCriacao<=:e and n.proposicao.id=:idProposicao")

})
public class NotaTecnica extends AbstractEntity implements DocRelated {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901057708852072015L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "documento_id", referencedColumnName = "id", nullable = true)
	private Documento documento;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "proposicao_id", referencedColumnName = "id", nullable = false)
	private Proposicao proposicao;

	@ManyToOne(optional = false)
	private Usuario usuario;

	@Column(name="criacao")
	private Long dataCriacao;

	@Column(length = 20000)
	private String nota;

	@Column(length = 256)
	private String url_arquivo;

	protected NotaTecnica() {
	}
	

	public NotaTecnica(Proposicao p, Usuario u) {
		this.proposicao = p;
		this.usuario = u;
	}

	@Override
	public Number getId() {
		return id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Proposicao getProposicao() {
		return proposicao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Long getDataCriacao() {
		return dataCriacao;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setProposicao(Proposicao proposicao) {
		this.proposicao = proposicao;
	}

	public String getUrl_arquivo() {
		return url_arquivo;
	}

	public void setUrl_arquivo(String url_arquivo) {
		this.url_arquivo = url_arquivo;
	}

	@PrePersist
	protected void onCreate() {
		dataCriacao = System.currentTimeMillis();
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
