package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.model.pautacomissao.PautaReuniaoComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.ProposicaoPautaComissao;
import br.gov.mj.sislegis.app.model.pautacomissao.Sessao;
import br.gov.mj.sislegis.app.model.pautacomissao.SituacaoSessao;

public class ReuniaoBeanSenado extends br.gov.mj.sislegis.app.parser.ReuniaoBean {
	private static final String MATERIA = "MATE";

	public static String getMateria() {
		return MATERIA;
	}

	protected List<ComissaoBean> comissoes = new ArrayList<ComissaoBean>();

	protected List<ParteBean> partes = new ArrayList<ParteBean>();

	protected List<ParteBean> getPartes() {
		return partes;
	}

	protected List<ComissaoBean> getComissoes() {
		return comissoes;
	}

	public Set<ProposicaoPautaComissao> getProposicoesPauta(PautaReuniaoComissao reuniao) {

		for (ParteBean parteBean : this.getPartes()) {
			List<ItemBean> itens = parteBean.getItens();
			for (Iterator iterator = itens.iterator(); iterator.hasNext();) {
				ItemBean itemBean = (ItemBean) iterator.next();

				// Não adicionamos por exemplo, os requerimentos, pois não são
				// tratados como proposições
				if (itemBean.tipo.equalsIgnoreCase(MATERIA)) {

					Materia mat = itemBean.getMateria();
					Proposicao prop = mat.toProposicao();
					prop.setComissao(comissoes.get(0).getSigla() + " - " + comissoes.get(0).getNome());
					prop.setOrigem(Origem.SENADO);
					prop.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
							+ prop.getIdProposicao());
					ProposicaoPautaComissao propPauta = new ProposicaoPautaComissao(reuniao, prop);
					propPauta.setOrdemPauta(itemBean.getSeqOrdemPauta());
					propPauta.setRelator(itemBean.getMateria().getRelator());

					reuniao.addProposicaoPauta(propPauta);
				}
			}

			List<EventoBean> eventos = parteBean.getEventos(); // tipicamente
			// aparece em
			// audiencias
			// publicas
			for (Iterator iterator = eventos.iterator(); iterator.hasNext();) {
				EventoBean eventoBean = (EventoBean) iterator.next();

				Materias materias = eventoBean.getMateriasRelacionadas();
				if (materias != null && materias.materias != null) {
					for (Iterator iterator2 = materias.materias.iterator(); iterator2.hasNext();) {
						Materia materia = (Materia) iterator2.next();

						Proposicao prop = materia.toProposicao();
						ProposicaoPautaComissao propPauta = new ProposicaoPautaComissao(reuniao, prop);
						propPauta.setOrdemPauta(-1);
						propPauta.setRelator(materia.getRelator());
						reuniao.addProposicaoPauta(propPauta);

					}
				}
			}

		}

		return reuniao.getProposicoesDaPauta();
	}

	// protected List<Proposicao> getProposicoes() {
	// List<Proposicao> materias = new ArrayList<Proposicao>();
	//
	// for (ParteBean parteBean : this.getPartes()) {
	// List<ItemBean> itens = parteBean.getItens();
	// List<EventoBean> eventos = parteBean.getEventos(); // tipicamente
	// // aparece em
	// // audiencias
	// // publicas
	//
	// for (ItemBean itemBean : itens) {
	// // Não adicionamos por exemplo, os requerimentos, pois não são
	// // tratados como proposições
	// if (itemBean.tipo.equalsIgnoreCase(MATERIA)) {
	// Proposicao prop = itemBean.getProposicao();
	// prop.setComissao(comissoes.get(0).getSigla() + " - " +
	// comissoes.get(0).getNome());
	// prop.setOrigem(Origem.SENADO);
	// prop.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
	// + prop.getIdProposicao());
	// prop.setLinkPauta("http://legis.senado.leg.br/comissoes/reuniao?reuniao="
	// + getCodigo());
	// materias.add(prop);
	// }
	// }
	// if (eventos != null) {
	//
	// for (EventoBean eventoBean : eventos) {
	// List<Proposicao> proposicoes = eventoBean.getProposicoes();
	//
	// for (Proposicao prop : proposicoes) {
	// prop.setComissao(comissoes.get(0).getSigla() + " - " +
	// comissoes.get(0).getNome());
	// prop.setOrigem(Origem.SENADO);
	// prop.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
	// + prop.getIdProposicao());
	// prop.setLinkPauta("http://legis.senado.leg.br/comissoes/reuniao?reuniao="
	// + getCodigo());
	// materias.add(prop);
	// }
	// }
	// }
	// }
	//
	// return materias;
	// }

	@Override
	public String toString() {

		return titulo + ":" + tipo + " " + situacao + "@" + data + " " + hora;
	}

	protected enum Situacao {
		Realizada, Agendada, Cancelada
	};

	public Sessao getSessao() {
		Sessao sessao = new Sessao();
		try {
			sessao.setData(getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sessao.setIdentificadorExterno(getCodigo().toString());
		sessao.setTitulo(titulo);
		switch (Situacao.valueOf(situacao)) {
		case Realizada:
			sessao.setSituacao(SituacaoSessao.Realizada);
			break;
		case Agendada:
			sessao.setSituacao(SituacaoSessao.Agendada);
			break;
		case Cancelada:
			sessao.setSituacao(SituacaoSessao.Cancelada);
			break;

		default:
			break;
		}
		return sessao;
	}

	public Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		return sdf.parse(data + " " + hora);
	}

}