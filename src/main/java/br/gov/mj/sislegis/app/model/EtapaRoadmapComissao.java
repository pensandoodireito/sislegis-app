package br.gov.mj.sislegis.app.model;

import br.gov.mj.sislegis.app.rest.serializers.CompactProposicaoSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Etapa do Roadmap (roteiro) de Comissoes por onde uma Proposicao deve tramitar
 */

@Entity
@XmlRootElement
@Table(name = "etapa_roadmap_comissao")
@JsonIgnoreProperties({ "proposicao" })
public class EtapaRoadmapComissao extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @JoinColumn(name = "proposicao_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Proposicao proposicao;

    @JoinColumn(name = "comissao_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Comissao comissao;

    private Integer ordem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonSerialize(using = CompactProposicaoSerializer.class)
    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    public Comissao getComissao() {
        return comissao;
    }

    public void setComissao(Comissao comissao) {
        this.comissao = comissao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}