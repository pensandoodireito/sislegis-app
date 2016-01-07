package br.gov.mj.sislegis.app.parser.camara.xstream;

import br.gov.mj.sislegis.app.model.Votacao;
import br.gov.mj.sislegis.app.model.Voto;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@XStreamAlias("proposicao")
public class ProposicaoVotos {

    @XStreamAlias("Votacoes")
    private List<VotacaoBean> votacoesBean = new ArrayList<>();

    public static void configXstream(XStream xStream){
        xStream.processAnnotations(ProposicaoVotos.class);
        xStream.processAnnotations(VotacaoBean.class);
        xStream.processAnnotations(DeputadoBean.class);
    }

    public List<VotacaoBean> getVotacoesBean() {
        return votacoesBean;
    }

    public void setVotacoesBean(List<VotacaoBean> votacoesBean) {
        this.votacoesBean = votacoesBean;
    }

    public List<Votacao> toVotacaoList() throws ParseException {
        List<Votacao> votacoes = new ArrayList<>();

        for (VotacaoBean votacaoBean : votacoesBean){
            Votacao votacao = new Votacao();

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = format.parse(votacaoBean.getData());
            votacao.setData(date);

            votacao.setResumo(votacaoBean.getResumo());

            List<Voto> votos = new ArrayList<>();
            for (DeputadoBean deputadoBean : votacaoBean.getVotos()){
                Voto voto = new Voto();
                voto.setNomeParlamentar(deputadoBean.getNome());
                voto.setUfParlamentar(deputadoBean.getUf());
                voto.setPartidoParlamentar(deputadoBean.getPartido());
                voto.setDescricaoVoto(deputadoBean.getVoto());
                votos.add(voto);
            }

            votacao.setVotos(votos);
            votacoes.add(votacao);
        }

        return votacoes;
    }
}

@XStreamAlias("Votacao")
class VotacaoBean {

    @XStreamAsAttribute
    @XStreamAlias("Data")
    private String data;

    @XStreamAsAttribute
    @XStreamAlias("Resumo")
    private String resumo;

    private List<DeputadoBean> votos;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public List<DeputadoBean> getVotos() {
        return votos;
    }

    public void setVotos(List<DeputadoBean> votos) {
        this.votos = votos;
    }
}

@XStreamAlias("Deputado")
class DeputadoBean{

    @XStreamAsAttribute
    @XStreamAlias("Nome")
    private String nome;

    @XStreamAsAttribute
    @XStreamAlias("Partido")
    private String partido;

    @XStreamAsAttribute
    @XStreamAlias("UF")
    private String uf;

    @XStreamAsAttribute
    @XStreamAlias("Voto")
    private String voto;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }
}