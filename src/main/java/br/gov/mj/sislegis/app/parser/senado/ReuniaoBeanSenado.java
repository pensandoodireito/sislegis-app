package br.gov.mj.sislegis.app.parser.senado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
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

	protected List<Proposicao> getProposicoes() {
		List<Proposicao> materias = new ArrayList<Proposicao>();

		for (ParteBean parteBean : this.getPartes()) {
			List<ItemBean> itens = parteBean.getItens();
			List<EventoBean> eventos = parteBean.getEventos(); // tipicamente
																// aparece em
																// audiencias
																// publicas

			for (ItemBean itemBean : itens) {
				// Não adicionamos por exemplo, os requerimentos, pois não são
				// tratados como proposições
				if (itemBean.tipo.equalsIgnoreCase(MATERIA)) {
					Proposicao prop = itemBean.getProposicao();
					prop.setComissao(comissoes.get(0).getSigla() + " - " + comissoes.get(0).getNome());
					prop.setOrigem(Origem.SENADO);
					prop.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
							+ prop.getIdProposicao());
					prop.setLinkPauta("http://legis.senado.leg.br/comissoes/reuniao?reuniao=" + getCodigo());
					materias.add(prop);
				}
			}

			for (EventoBean eventoBean : eventos) {
				List<Proposicao> proposicoes = eventoBean.getProposicoes();

				for (Proposicao prop : proposicoes) {
					prop.setComissao(comissoes.get(0).getSigla() + " - " + comissoes.get(0).getNome());
					prop.setOrigem(Origem.SENADO);
					prop.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate="
							+ prop.getIdProposicao());
					prop.setLinkPauta("http://legis.senado.leg.br/comissoes/reuniao?reuniao=" + getCodigo());
					materias.add(prop);
				}
			}
		}

		return materias;
	}

	@Override
	public String toString() {

		return titulo + ":" + tipo + " " + situacao + "@" + data + " " + hora;
	}

	protected enum Situacao {
		Realizada, Agendada, Cancelada
	};

	public Sessao getSessao() {
		Sessao sessao = new Sessao();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		try {
			sessao.setData(sdf.parse(data + " " + hora));
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

}