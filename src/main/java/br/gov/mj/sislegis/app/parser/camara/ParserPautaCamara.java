package br.gov.mj.sislegis.app.parser.camara;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ParserFetcher;

import com.thoughtworks.xstream.XStream;

public class ParserPautaCamara {

	public static void main(String[] args) throws Exception {
		ParserPautaCamara parser = new ParserPautaCamara();
		List<OrgaoCamara> orgaosCamara = parser.listOrgaos();
		for (Iterator iterator = orgaosCamara.iterator(); iterator.hasNext();) {
			OrgaoCamara orgaoCamara = (OrgaoCamara) iterator.next();
			System.out.println(orgaoCamara.sigla + " " + orgaoCamara.id);
		}

		// TODO: Informação que vem do filtro
		Long idComissao = 2003L;
		String datIni = "20140702";
		String datFim = "20140702";

		System.out.println(parser.getProposicoes(idComissao, datIni, datFim).toString());
	}

	public List<OrgaoCamara> listOrgaos() throws IOException {
		String wsURL = new StringBuilder("http://www.camara.gov.br/SitCamaraWS/Orgaos.asmx/ObterOrgaos").toString();
		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		OrgaosBean pauta = new OrgaosBean();

		xstream.alias("orgaos", OrgaosBean.class);
		xstream.alias("orgao", OrgaoCamara.class);

		// Utilizamos o implicit quando os filhos já tem os dados que queremos
		// buscar. Ou seja, não tem um pai e vários filhos do mesmo tipo.
		xstream.addImplicitCollection(OrgaosBean.class, "orgaos");
		xstream.aliasAttribute(OrgaoCamara.class, "id", "id");
		xstream.aliasAttribute(OrgaoCamara.class, "idTipodeOrgao", "idTipodeOrgao");
		xstream.aliasAttribute(OrgaoCamara.class, "descricao", "descricao");
		xstream.aliasAttribute(OrgaoCamara.class, "sigla", "sigla");
		// xstream.aliasAttribute(PautaBean.class, "dataInicial",
		// "dataInicial");
		// xstream.aliasAttribute(PautaBean.class, "dataFinal", "dataFinal");

		ParserFetcher.fetchXStream(wsURL, xstream, pauta);
		return pauta.orgaos;

	}

	public List<ReuniaoBeanCamara> getReunioes(Long idComissao, String datIni, String datFim) throws IOException {
		return getPauta(idComissao, datIni, datFim).getReunioes();
	}

	public PautaBean getPauta(Long idComissao, String datIni, String datFim) throws IOException {
		String wsURL = new StringBuilder("http://www.camara.gov.br/SitCamaraWS/Orgaos.asmx/ObterPauta?IDOrgao=")
				.append(idComissao).append("&datIni=").append(datIni).append("&datFim=").append(datFim).toString();

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		PautaBean pauta = new PautaBean();

		config(xstream);

		ParserFetcher.fetchXStream(wsURL, xstream, pauta);
		return pauta;
	}

	public List<Proposicao> getProposicoes(Long idComissao, String datIni, String datFim) throws Exception {
		List<Proposicao> proposicoes = new ArrayList<Proposicao>();
		PautaBean pauta = getPauta(idComissao, datIni, datFim);
		for (ReuniaoBeanCamara reuniao : pauta.getReunioes()) {
			// adiciona dados da comissao
			int seqOrdemPauta = 1;
			for (Proposicao proposicao : reuniao.getProposicoes()) {
				proposicao.setSeqOrdemPauta(seqOrdemPauta++);
				proposicao.setComissao(pauta.getOrgao());
				proposicao.setOrigem(Origem.CAMARA);
				proposicao.setLinkProposicao("http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao="
						+ proposicao.getIdProposicao());
				proposicao
						.setLinkPauta("http://www.camara.leg.br/internet/ordemdodia/ordemDetalheReuniaoCom.asp?codReuniao="
								+ reuniao.getCodigo().toString());
			}

			proposicoes.addAll(reuniao.getProposicoes());
		}

		return proposicoes;
	}

	private void config(XStream xstream) {
		xstream.alias("pauta", PautaBean.class);
		xstream.alias("reuniao", ReuniaoBeanCamara.class);
		xstream.alias("proposicao", Proposicao.class);

		// Utilizamos o implicit quando os filhos já tem os dados que queremos
		// buscar. Ou seja, não tem um pai e vários filhos do mesmo tipo.
		xstream.addImplicitCollection(PautaBean.class, "reunioes");
		xstream.aliasAttribute(PautaBean.class, "orgao", "orgao");
		xstream.aliasAttribute(PautaBean.class, "dataInicial", "dataInicial");
		xstream.aliasAttribute(PautaBean.class, "dataFinal", "dataFinal");

		xstream.aliasField("horario", ReuniaoBeanCamara.class, "hora");
		xstream.aliasField("data", ReuniaoBeanCamara.class, "data");
		xstream.aliasField("codReuniao", ReuniaoBeanCamara.class, "codigo");
		xstream.aliasField("tipo", ReuniaoBeanCamara.class, "tipo");
		xstream.aliasField("estado", ReuniaoBeanCamara.class, "situacao");
		xstream.aliasField("tituloReuniao", ReuniaoBeanCamara.class, "titulo");
	}
}

class OrgaosBean {

	protected List<OrgaoCamara> orgaos = new ArrayList<OrgaoCamara>();

}

class PautaBean {
	protected String orgao;
	protected String dataInicial;
	protected String dataFinal;

	protected List<ReuniaoBeanCamara> reunioes = new ArrayList<ReuniaoBeanCamara>();

	protected List<ReuniaoBeanCamara> getReunioes() {
		return reunioes;
	}

	protected String getOrgao() {
		return orgao;
	}
}
