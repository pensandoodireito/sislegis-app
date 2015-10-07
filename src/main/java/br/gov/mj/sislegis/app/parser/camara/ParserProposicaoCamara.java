package br.gov.mj.sislegis.app.parser.camara;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.CollectionLazyConverter;
import br.gov.mj.sislegis.app.parser.ParserFetcher;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
import br.gov.mj.sislegis.app.parser.TipoProposicao;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ParserProposicaoCamara implements ProposicaoSearcher {

	public static void main(String[] args) throws Exception {
		ParserProposicaoCamara parser = new ParserProposicaoCamara();
		Long idProposicao = 562039L; // TODO: Informação que vem do filtro
		System.out.println(parser.getProposicao(idProposicao).toString());
		System.out.println(parser.listaTipos());

		Collection<Proposicao> prop = parser.searchProposicao("DIS", 41, 2015);
		for (Iterator iterator = prop.iterator(); iterator.hasNext();) {
			Proposicao proposicao = (Proposicao) iterator.next();
			System.out.println(proposicao);
			System.out.println(proposicao.getIdProposicao());
			System.out.println(proposicao.getComissao());
			System.out.println(proposicao.getLinkProposicao());
		}

	}

	/**
	 * Busca proposicao da camara por parametros<br>
	 * veja endpoint da camara aqui:<br>
	 * http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx?op=
	 * ListarProposicoes
	 */
	public Collection<Proposicao> searchProposicao(String sigla, Integer numero, Integer ano) throws IOException {
		// http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla=DIS&numero=&ano=2015&datApresentacaoIni=&datApresentacaoFim=&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=
		StringBuilder wsURL = new StringBuilder(
				"http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?");
		wsURL.append("sigla=").append(sigla);
		wsURL.append("&numero=").append(numero);
		wsURL.append("&ano=").append(ano);
		wsURL.append("&datApresentacaoIni=&datApresentacaoFim=&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=&v=4");
		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		Proposicoes proposicoes = new Proposicoes();
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

		Collection<Proposicao> listProposicao = new ArrayList<Proposicao>();
		try {
			ParserFetcher.fetchXStream(wsURL.toString(), xstream, proposicoes);

			listProposicao = new ListProposicaoLazy(proposicoes.getProposicoes());

		} catch (FileNotFoundException e) {

			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.INFO, "Nenhum resultado encontrado");
		}

		return listProposicao;
	}

	public Proposicao getProposicao(Long idProposicao) throws Exception {
		String wsURL = "http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ObterProposicaoPorID?idProp="
				+ idProposicao;

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();

		Proposicao proposicao = new Proposicao();

		config(xstream);
		ParserFetcher.fetchXStream(wsURL, xstream, proposicao);

		proposicao.setOrigem(Origem.CAMARA);
		proposicao.setLinkProposicao("http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao="
				+ proposicao.getIdProposicao());
		return proposicao;
	}

	private static void config(XStream xstream) {
		xstream.alias("proposicao", Proposicao.class);

		xstream.aliasAttribute(Proposicao.class, "tipo", "tipo");
		xstream.aliasAttribute(Proposicao.class, "numero", "numero");
		xstream.aliasAttribute(Proposicao.class, "ano", "ano");

		xstream.aliasField("nomeProposicao", Proposicao.class, "sigla");
		xstream.aliasField("Ementa", Proposicao.class, "ementa");
		xstream.aliasField("Autor", Proposicao.class, "autor");
	}

	public List<TipoProposicao> listaTipos() throws IOException {
		String wsUrl = "http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarSiglasTipoProposicao";

		XStream xstream = new XStream();
		xstream.ignoreUnknownElements();
		ListaSigla list = new ListaSigla();
		xstream.alias("siglas", ListaSigla.class);
		xstream.alias("sigla", TipoProposicao.class);
		xstream.addImplicitCollection(ListaSigla.class, "siglas");
		xstream.aliasAttribute(TipoProposicao.class, "sigla", "tipoSigla");
		xstream.aliasAttribute(TipoProposicao.class, "nome", "descricao");

		ParserFetcher.fetchXStream(wsUrl, xstream, list);

		return list.getSiglas();
	}
}

class ListaSigla {
	protected List<TipoProposicao> siglas;

	protected List<TipoProposicao> getSiglas() {
		return siglas;
	}
}

@XStreamAlias("siglas")
class TiposProposicao {
	@XStreamImplicit(itemFieldName = "sigla")
	List<TipoProposicao> sigla = new ArrayList<TipoProposicao>();

	protected List<TipoProposicao> getSiglas() {
		return sigla;
	}
}

@XStreamAlias("proposicoes")
class Proposicoes {
	@XStreamImplicit(itemFieldName = "proposicao")
	List<Proposicao> proposicoes = new ArrayList<Proposicao>();

	protected List<Proposicao> getProposicoes() {
		return proposicoes;
	}
}

@XStreamAlias("autor1")
class Autor {
	String txtNomeAutor;
	String idecadastro;
	String codPartido;
	String txtSiglaPartido;
	String txtSiglaUF;
}

class TipoConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return String.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// Desnecessario, somente parseia XML->Objetos

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if ("tipoProposicao".equals(reader.getNodeName())) {
			TipoProposicao au = (TipoProposicao) context.convertAnother(reader, TipoProposicao.class);
			if (au != null && au.getNome() != null && au.getNome() != null) {
				return au.getNome();
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}

class AuthorConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return String.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// Desnecessario, somente parseia XML->Objetos

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if ("autor1".equals(reader.getNodeName())) {
			Autor au = (Autor) context.convertAnother(reader, Autor.class);
			if (au != null && au.txtNomeAutor != null && au.txtNomeAutor != null) {
				return au.txtNomeAutor;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}

class OrgaoNumerador {
	String sigla;
	String nome;
}

class ComissaoConverter implements Converter {

	@Override
	public boolean canConvert(Class type) {
		return String.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// Desnecessario, somente parseia XML->Objetos

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		if ("orgaoNumerador".equals(reader.getNodeName())) {
			OrgaoNumerador au = (OrgaoNumerador) context.convertAnother(reader, OrgaoNumerador.class);
			if (au != null && au.sigla != null && au.sigla != null) {
				return au.sigla;
			} else {
				return "";
			}

		}
		return reader.getValue();
	}
}

class ListProposicaoLazy extends CollectionLazyConverter<Proposicao, Proposicao> {

	public ListProposicaoLazy(List<Proposicao> materias) {
		super(materias);
	}

	@Override
	protected Proposicao convertKtoE(Proposicao proposicao) {
		proposicao.setOrigem(Origem.CAMARA);
		proposicao.setLinkProposicao("http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao="
				+ proposicao.getIdProposicao());
		return proposicao;
	}

}