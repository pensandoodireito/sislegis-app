package br.gov.mj.sislegis.app.parser.camara.xstream;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * XStream mapper de http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx?op=
 * ObterProposicaoPorID
 * 
 * @author rafael.coutinho
 *
 */
@XStreamAlias("proposicao")
public class ObterProposicaoPorID {

	@XStreamAsAttribute
	String ano;
	@XStreamAsAttribute
	String tipo;
	@XStreamAsAttribute
	String numero;

	@XStreamAlias("idProposicao")
	Integer idProposicao;

	@XStreamAlias("idProposicaoPrincipal")
	String idProposicaoPrincipal;
	@XStreamAlias("tipoProposicao")
	String tipoProposicaoDesc;
	@XStreamAlias("Ementa")
	String ementa;

	@XStreamAlias("Autor")
	String autor;

	@XStreamAlias("Situacao")
	String situacao;

	@XStreamAlias("LinkInteiroTeor")
	String linkInteiroTeor;

	@XStreamAlias("tema")
	String tema;

	public static void config(XStream xstream) {
		xstream.processAnnotations(ObterProposicaoPorID.class);
	}

	public Proposicao toProposicao() {
		Proposicao proposicao = new Proposicao();
		proposicao.setAno(ano);
		proposicao.setAutor(autor);
		// proposicao.setComissao(comissao);
		proposicao.setEmenta(ementa.trim());
		proposicao.setIdProposicao(idProposicao);
		// proposicao.setLinkProposicao(linkInteiroTeor);
		proposicao.setLinkProposicao("http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao="
				+ proposicao.getIdProposicao());
		proposicao.setNumero(numero);
		proposicao.setOrigem(Origem.CAMARA);
		proposicao.setTipo(tipo.trim());
		proposicao.setSituacao(situacao.trim());
		return proposicao;
	}

}
