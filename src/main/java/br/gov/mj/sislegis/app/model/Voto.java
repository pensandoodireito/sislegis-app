package br.gov.mj.sislegis.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Voto {

    @JsonIgnore
    private Votacao votacao;

    private String nomeParlamentar;

    private String partidoParlamentar;

    private String ufParlamentar;

    private String descricaoVoto;

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
