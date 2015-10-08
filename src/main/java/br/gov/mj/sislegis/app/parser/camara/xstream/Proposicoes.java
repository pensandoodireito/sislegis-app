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
	List<Proposicao> proposicoes = new ArrayList<Proposicao>();

	public List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Proposicoes.class);
		xstream.processAnnotations(Autor.class);
		xstream.processAnnotations(OrgaoNumerador.class);
		xstream.alias("tipoProposicao", TipoProposicao.class);

		xstream.aliasField("ano", Proposicao.class, "ano");
		xstream.aliasField("id", Proposicao.class, "idProposicao");

		xstream.aliasField("numero", Proposicao.class, "numero");
		xstream.aliasField("nome", Proposicao.class, "sigla");
		xstream.aliasField("txtEmenta", Proposicao.class, "ementa");
		xstream.aliasField("tipoProposicao", Proposicao.class, "tipo");
		xstream.registerLocalConverter(Proposicao.class, "tipo", new TipoConverter());
		xstream.aliasField("autor1", Proposicao.class, "autor");
		xstream.registerLocalConverter(Proposicao.class, "autor", new AuthorConverter());

		xstream.aliasField("orgaoNumerador", Proposicao.class, "comissao");
		xstream.registerLocalConverter(Proposicao.class, "comissao", new ComissaoConverter());

	}
}