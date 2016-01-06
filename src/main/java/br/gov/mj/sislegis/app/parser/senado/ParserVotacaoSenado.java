package br.gov.mj.sislegis.app.parser.senado;

import br.gov.mj.sislegis.app.model.Votacao;
import br.gov.mj.sislegis.app.model.Voto;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import com.thoughtworks.xstream.XStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParserVotacaoSenado {

    public List<Votacao> votacoesPorProposicao(Integer idProposicao) throws Exception {
        List<Votacao> votacoes = new ArrayList<>();
        StringBuilder wsURL = new StringBuilder("http://legis.senado.leg.br/dadosabertos/materia/votacoes/").append(idProposicao);


        XStream xstream = new XStream();
        xstream.ignoreUnknownElements();

        xstream.alias("VotacaoMateria", VotacaoMateriaBean.class);
        xstream.alias("Materia", MateriaBean.class);
        xstream.alias("Votacao", VotacaoBean.class);
        xstream.alias("VotoParlamentar", VotoParlamentarBean.class);
        xstream.alias("IdentificacaoParlamentar", IdentificacaoParlamentarBean.class);

        xstream.aliasField("Materia", VotacaoMateriaBean.class, "materia");

        xstream.aliasField("Votacoes", MateriaBean.class, "votacoes");

        xstream.aliasField("SessaoPlenaria", VotacaoBean.class, "sessaoPlenaria");
        xstream.aliasField("DescricaoVotacao", VotacaoBean.class, "descricaoVotacao");
        xstream.aliasField("DescricaoResultado", VotacaoBean.class, "descricaoResultado");
        xstream.aliasField("Votos", VotacaoBean.class, "votos");

        xstream.aliasField("DataSessao", SessaoPlenariaBean.class, "dataSessao");

        xstream.aliasField("IdentificacaoParlamentar", VotoParlamentarBean.class, "identificacaoParlamentar");
        xstream.aliasField("DescricaoVoto", VotoParlamentarBean.class, "descricaoVoto");

        xstream.aliasField("NomeCompletoParlamentar", IdentificacaoParlamentarBean.class, "nomeCompletoParlamentar");
        xstream.aliasField("SiglaPartidoParlamentar", IdentificacaoParlamentarBean.class, "siglaPartidoParlamentar");
        xstream.aliasField("UfParlamentar", IdentificacaoParlamentarBean.class, "ufParlamentar");

        VotacaoMateriaBean votacaoMateriaBean = new VotacaoMateriaBean();
        ParserFetcher.fetchXStream(wsURL.toString(), xstream, votacaoMateriaBean);

        for(VotacaoBean votacaoBean : votacaoMateriaBean.getMateria().getVotacoes()){
            Votacao votacao = new Votacao();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = format.parse(votacaoBean.getSessaoPlenaria().getDataSessao());
            votacao.setData(date);

            votacao.setDescricao(votacaoBean.getDescricaoVotacao());
            votacao.setResultado(votacaoBean.getDescricaoResultado());

            List<Voto> votos = new ArrayList<>();
            for (VotoParlamentarBean votoParlamentarBean : votacaoBean.getVotos()){
                Voto voto = new Voto();
                voto.setNomeParlamentar(votoParlamentarBean.getIdentificacaoParlamentar().getNomeCompletoParlamentar());
                voto.setDescricaoVoto(votoParlamentarBean.getDescricaoVoto());
                voto.setPartidoParlamentar(votoParlamentarBean.getIdentificacaoParlamentar().getSiglaPartidoParlamentar());
                voto.setUfParlamentar(votoParlamentarBean.getIdentificacaoParlamentar().getUfParlamentar());
                votos.add(voto);
            }

            votacao.setVotos(votos);

            votacoes.add(votacao);
        }

        return votacoes;
    }

}

class VotacaoMateriaBean{
    private MateriaBean materia;

    public MateriaBean getMateria() {
        return materia;
    }

    public void setMateria(MateriaBean materia) {
        this.materia = materia;
    }
}

class MateriaBean{
    private List<VotacaoBean> votacoes;

    public List<VotacaoBean> getVotacoes() {
        return votacoes;
    }

    public void setVotacoes(List<VotacaoBean> votacoes) {
        this.votacoes = votacoes;
    }
}

class VotacaoBean{
    private SessaoPlenariaBean sessaoPlenaria;
    private String descricaoVotacao;
    private String descricaoResultado;
    private List<VotoParlamentarBean> votos;

    public SessaoPlenariaBean getSessaoPlenaria() {
        return sessaoPlenaria;
    }

    public void setSessaoPlenaria(SessaoPlenariaBean sessaoPlenaria) {
        this.sessaoPlenaria = sessaoPlenaria;
    }

    public String getDescricaoVotacao() {
        return descricaoVotacao;
    }

    public void setDescricaoVotacao(String descricaoVotacao) {
        this.descricaoVotacao = descricaoVotacao;
    }

    public String getDescricaoResultado() {
        return descricaoResultado;
    }

    public void setDescricaoResultado(String descricaoResultado) {
        this.descricaoResultado = descricaoResultado;
    }

    public List<VotoParlamentarBean> getVotos() {
        return votos;
    }

    public void setVotos(List<VotoParlamentarBean> votos) {
        this.votos = votos;
    }
}

class SessaoPlenariaBean{
    private String dataSessao;

    public String getDataSessao() {
        return dataSessao;
    }

    public void setDataSessao(String dataSessao) {
        this.dataSessao = dataSessao;
    }
}

class VotoParlamentarBean{
    private IdentificacaoParlamentarBean identificacaoParlamentar;
    private String descricaoVoto;

    public IdentificacaoParlamentarBean getIdentificacaoParlamentar() {
        return identificacaoParlamentar;
    }

    public void setIdentificacaoParlamentar(IdentificacaoParlamentarBean identificacaoParlamentar) {
        this.identificacaoParlamentar = identificacaoParlamentar;
    }

    public String getDescricaoVoto() {
        return descricaoVoto;
    }

    public void setDescricaoVoto(String descricaoVoto) {
        this.descricaoVoto = descricaoVoto;
    }
}

class IdentificacaoParlamentarBean{
    private String nomeCompletoParlamentar;
    private String siglaPartidoParlamentar;
    private String ufParlamentar;

    public String getNomeCompletoParlamentar() {
        return nomeCompletoParlamentar;
    }

    public void setNomeCompletoParlamentar(String nomeCompletoParlamentar) {
        this.nomeCompletoParlamentar = nomeCompletoParlamentar;
    }

    public String getSiglaPartidoParlamentar() {
        return siglaPartidoParlamentar;
    }

    public void setSiglaPartidoParlamentar(String siglaPartidoParlamentar) {
        this.siglaPartidoParlamentar = siglaPartidoParlamentar;
    }

    public String getUfParlamentar() {
        return ufParlamentar;
    }

    public void setUfParlamentar(String ufParlamentar) {
        this.ufParlamentar = ufParlamentar;
    }
}
