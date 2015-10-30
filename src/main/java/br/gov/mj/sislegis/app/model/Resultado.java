package br.gov.mj.sislegis.app.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@XmlRootElement
public class Resultado extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(length=2000)
    private String descricao;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    private ReuniaoProposicao reuniaoProposicao;

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date data) {
        this.dataCriacao = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ReuniaoProposicao getReuniaoProposicao() {
        return reuniaoProposicao;
    }

    public void setReuniaoProposicao(ReuniaoProposicao reuniaoProposicao) {
        this.reuniaoProposicao = reuniaoProposicao;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", usuario=" + usuario +
                ", reuniaoProposicao=" + reuniaoProposicao +
                '}';
    }
}
