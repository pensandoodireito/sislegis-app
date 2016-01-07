package br.gov.mj.sislegis.app.parser.camara;

import br.gov.mj.sislegis.app.model.Votacao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.camara.xstream.ProposicaoVotos;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

public class ParserVotacaoCamara {

    public List<Votacao> votacoesPorProposicao(Integer idProposicao, String ano, String tipo) throws Exception {
        List<Votacao> votacoes = new ArrayList<>();
        StringBuilder wsURL = new StringBuilder("http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ObterVotacaoProposicao?");
        wsURL.append("numero=").append(idProposicao);
        wsURL.append("&ano=").append(ano);
        wsURL.append("&tipo=").append(tipo);

        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        ProposicaoVotos proposicaoVotos = new ProposicaoVotos();
        ProposicaoVotos.configXstream(xStream);

        ParserFetcher.fetchXStream(wsURL.toString(), xStream, proposicaoVotos);

        return proposicaoVotos.toVotacaoList();
    }
}