package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.util.SislegisUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Materia")
public class Materia {
	@XStreamAlias("IdentificacaoMateria")
	IdentificacaoMateria identificacaoMateria;
	@XStreamAlias("DadosBasicosMateria")
	DadosBasicosMateria DadosBasicosMateria;

	@XStreamAlias("AutoresPrincipais")
	AutoresPrincipais autoresPrincipais;

	@XStreamAlias("SituacaoAtual")
	SituacaoAtual situacaoAtual;

	@Override
	public String toString() {

		return identificacaoMateria.toString();
	}

	public Proposicao toProposicao() {
		Proposicao p = new Proposicao();
		p.setAno(identificacaoMateria.AnoMateria);
		if (autoresPrincipais != null && !autoresPrincipais.autores.isEmpty()) {
			p.setAutor(autoresPrincipais.autores.get(0).NomeAutor);
		}
		p.setOrigem(Origem.SENADO);
		if (situacaoAtual == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING, "Nao carregou a situacao atual");
		} else if (situacaoAtual.autuacoes == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING,
					"Nao carregou autuacoes da situacao atual");
		} else if (situacaoAtual.autuacoes.autuacoes == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.WARNING,
					"Nao carregou autuacoes da situacao atual");
		} else if (!situacaoAtual.autuacoes.autuacoes.isEmpty()) {
			p.setComissao(situacaoAtual.autuacoes.autuacoes.get(0).Local.SiglaLocal);
			p.setSituacao(situacaoAtual.autuacoes.autuacoes.get(0).Situacao.SiglaSituacao);
		}

		p.setEmenta(DadosBasicosMateria.EmentaMateria);
		p.setIdProposicao(identificacaoMateria.CodigoMateria);
		p.setNumero(identificacaoMateria.NumeroMateria);

		p.setTipo(identificacaoMateria.SiglaSubtipoMateria);
		p.setOrigem(Origem.SENADO);
		p.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=" + p.getIdProposicao());
		return p;

	}

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Materia.class);
		xstream.processAnnotations(IdentificacaoMateria.class);
		xstream.processAnnotations(DadosBasicosMateria.class);
		xstream.processAnnotations(AutoresPrincipais.class);
		xstream.processAnnotations(AutorPrincipal.class);
		SituacaoAtual.configXstream(xstream);

	}
}
