package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "posicionamento_proposicao")
public class PosicionamentoProposicao extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    private Posicionamento posicionamento;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Proposicao proposicao;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();

    @Column
    private Boolean preliminar = false;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Posicionamento getPosicionamento() {
        return posicionamento;
    }

    public void setPosicionamento(Posicionamento posicionamento) {
        this.posicionamento = posicionamento;
    }

    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean isPreliminar() {
        return preliminar;
    }

    public void setPreliminar(Boolean preliminar) {
        this.preliminar = preliminar;
    }
}
