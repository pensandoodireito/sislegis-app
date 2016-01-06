package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "voto")
@XmlRootElement
public class Voto extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Votacao votacao;

    private String nomeParlamentar;

    private String partidoParlamentar;

    private String ufParlamentar;

    private String descricaoVoto;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Votacao getVotacao() {
        return votacao;
    }

    public void setVotacao(Votacao votacao) {
        this.votacao = votacao;
    }

    public String getNomeParlamentar() {
        return nomeParlamentar;
    }

    public void setNomeParlamentar(String nomeParlamentar) {
        this.nomeParlamentar = nomeParlamentar;
    }

    public String getPartidoParlamentar() {
        return partidoParlamentar;
    }

    public void setPartidoParlamentar(String partidoParlamentar) {
        this.partidoParlamentar = partidoParlamentar;
    }

    public String getUfParlamentar() {
        return ufParlamentar;
    }

    public void setUfParlamentar(String ufParlamentar) {
        this.ufParlamentar = ufParlamentar;
    }

    public String getDescricaoVoto() {
        return descricaoVoto;
    }

    public void setDescricaoVoto(String dscricaoVoto) {
        this.descricaoVoto = dscricaoVoto;
    }
}
