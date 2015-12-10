package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Roadmap (roteiro) de Comissoes por onde uma Proposicao deve tramitar
 */

@Entity
@IdClass(RoadmapComissao.class)
@XmlRootElement
@Table(name = "roadmap_comissao")
public class RoadmapComissao implements Serializable {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "proposicao_id")
    private Proposicao proposicao;

    @Id
    private String comissao;

    @Id
    @JsonIgnore
    private Integer ordem;

    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    public String getComissao() {
        return comissao;
    }

    public void setComissao(String comissao) {
        this.comissao = comissao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoadmapComissao that = (RoadmapComissao) o;

        if (proposicao != null ? !proposicao.equals(that.proposicao) : that.proposicao != null) return false;
        if (comissao != null ? !comissao.equals(that.comissao) : that.comissao != null) return false;
        return !(ordem != null ? !ordem.equals(that.ordem) : that.ordem != null);

    }

    @Override
    public int hashCode() {
        int result = proposicao != null ? proposicao.hashCode() : 0;
        result = 31 * result + (comissao != null ? comissao.hashCode() : 0);
        result = 31 * result + (ordem != null ? ordem.hashCode() : 0);
        return result;
    }
}