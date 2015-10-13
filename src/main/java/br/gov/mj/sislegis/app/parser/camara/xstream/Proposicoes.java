package br.gov.mj.sislegis.app.parser.camara.xstream;

import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.TipoProposicao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Mapper Xstream da entidade retornada pelo serviço:<br>
 * http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx?op=ListarProposicoes
 * 
 * @author coutinho
 *
 */
@XStreamAlias("proposicoes")
public class Proposicoes {
	@XStreamImplicit(itemFieldName = "proposicao")
	List<ProposicaoWS> proposicoes = new ArrayList<ProposicaoWS>();

	public List<ProposicaoWS> getProposicoes() {
		return proposicoes;
	}

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Proposicoes.class);
		xstream.processAnnotations(Autor.class);
		xstream.processAnnotations(OrgaoNumerador.class);
		xstream.processAnnotations(ProposicaoWS.class);
		xstream.processAnnotations(TipoProposicao.class);
		xstream.processAnnotations(Autor.class);

		Situacao.configXstream(xstream);

	}
}

@XStreamAlias("proposicao")
class ProposicaoWS {
	TipoProposicao tipoProposicao;
	String ano;
	Integer id;
	String numero;
	String nome;
	String txtEmenta;
	Situacao situacao;
	Autor autor1;
	String orgaoNumerador;

	public Proposicao toProposicao() {
		Proposicao proposicao = new Proposicao();
		proposicao.setAno(ano);
		proposicao.setIdProposicao(id);
		proposicao.setNumero(numero);
		proposicao.setSigla(nome);
		proposicao.setTipo(tipoProposicao.getNome());
		proposicao.setEmenta(txtEmenta);
		proposicao.setAutor(autor1.txtNomeAutor);
		proposicao.setComissao(orgaoNumerador);
		proposicao.setSituacao(situacao.orgao.siglaOrgaoEstado);
		return proposicao;
	}
}