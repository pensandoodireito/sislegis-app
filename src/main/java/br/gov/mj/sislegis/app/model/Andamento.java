package br.gov.mj.sislegis.app.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@XmlRootElement
public class Andamento extends AbstractEntity{

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
    private Proposicao proposicao;

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

    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    @Override
    public String toString() {
        return "Andamento{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", usuario=" + usuario +
                ", proposicao=" + proposicao +
                '}';
    }
}
