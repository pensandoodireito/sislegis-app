package br.gov.mj.sislegis.app.model;

public class ProcessoSei {
    private Proposicao proposicao;
    private String nup;
    private String linkSei;

    public Proposicao getProposicao() {
        return proposicao;
    }

    public void setProposicao(Proposicao proposicao) {
        this.proposicao = proposicao;
    }

    public String getNup() {
        return nup;
    }

    public void setNup(String nup) {
        this.nup = nup;
    }

    public String getLinkSei() {
        return linkSei;
    }

    public void setLinkSei(String linkSei) {
        this.linkSei = linkSei;
    }
}
