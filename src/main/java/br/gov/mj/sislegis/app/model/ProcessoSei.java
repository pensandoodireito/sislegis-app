package br.gov.mj.sislegis.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "processo_sei")
public class ProcessoSei extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Proposicao proposicao;

    private String protocolo;

    private String linkSei;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String nup) {
        this.protocolo = nup;
    }

    public String getLinkSei() {
        return linkSei;
    }

    public void setLinkSei(String linkSei) {
        this.linkSei = linkSei;
    }
}
