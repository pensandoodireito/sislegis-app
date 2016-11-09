package br.gov.mj.sislegis.app.model;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@Entity
@Table(name = "documento")
@NamedQueries({ 
	@NamedQuery(name = "getAllDocumentos4Usuario", query = "select c from Documento c where c.usuario.id=:userId")

})
@XmlRootElement
public class Documento extends AbstractEntity {

	private static final long serialVersionUID = -9103342334603175569L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", nullable = false)
	private Date updated;

	@Column(name = "path")
	private String path;
	@Column(name = "nome")
	private String nome;
	@ManyToOne(optional = true)
	private Usuario usuario;

	protected Documento() {

	}

	public Documento(String fileName) {

		this.nome = fileName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String toString() {

		return nome + "@" + id;
	}

	@PrePersist
	protected void onCreate() {
		updated = created = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("created", created.getTime());
		json.put("nome", this.nome);
		if (usuario != null) {
			json.put("usuario", this.usuario.toJson());
		}

		return json;
	}

	@PreRemove
	public void removeArquivo() {
		try {
			new File(path).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
