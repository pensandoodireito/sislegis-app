package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Proposicao;
import br.gov.mj.sislegis.app.parser.ProposicaoSearcher;
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

	@XStreamAlias("Relatoria")
	Relatoria relatoria;

	@XStreamAlias("SituacaoAtual")
	SituacaoAtual situacaoAtual;
	
	@XStreamAlias("Tramitacoes")
	List<Tramitacao> tramitacoes;

	@Override
	public String toString() {

		return identificacaoMateria.toString();
	}

	public String getRelator() {
		if (relatoria != null && relatoria.relatores != null && !relatoria.relatores.isEmpty()) {
			Relator relator = relatoria.relatores.get(0);
			String desc = "";
			if (relator.tratamento != null && relator.tratamento.length() > 0) {
				desc = relator.tratamento + " ";
			}
			if (relator.nome != null && relator.nome.length() > 0) {
				desc += relator.nome;
			}
			return desc;
		} else if (nomeRelator != null && nomeRelator.length() > 0) {
			return nomeRelator;
		}
		return ProposicaoSearcher.SEM_RELATOR_DEFINIDO;
	}

	// Cada webservice tem uma estrutura, a abaixo é para a pauta. Acima é para
	// proposicao direto, o mais abaixo é para o ws de reuniao do pleanrio
	@XStreamAlias("autoria")
	Autoria autoria;

	@XStreamAlias("Situacoes")
	Situacoes situacoes;

	@XStreamAlias("Ano")
	String ano;
	@XStreamAlias("Codigo")
	Integer codigo;
	@XStreamAlias("Numero")
	String numero;

	@XStreamAlias("Subtipo")
	String subtipo;
	@XStreamAlias("Ementa")
	String ementa;

	@XStreamAlias("AnoMateria")
	String anoMateria;
	@XStreamAlias("NumeroMateria")
	String numeroMateria;
	@XStreamAlias("SiglaMateria")
	String siglaMateria;
	@XStreamAlias("CodigoMateria")
	Integer codigoMateria;
	@XStreamAlias("NomeRelator")
	String nomeRelator;
	@XStreamAlias("SequenciaOrdem")
	Integer sequenciaOrdem;

	public Integer getSequenciaOrdem() {
		return sequenciaOrdem;
	}

	public Proposicao toProposicao() {
		
		Proposicao p = new Proposicao();
		if (identificacaoMateria == null) {
			identificacaoMateria = new IdentificacaoMateria();
			if (ano == null) {
				identificacaoMateria.AnoMateria = anoMateria;
				identificacaoMateria.NumeroMateria = numeroMateria;
				identificacaoMateria.SiglaSubtipoMateria = siglaMateria;
				identificacaoMateria.CodigoMateria = codigoMateria;
			} else {
				identificacaoMateria.AnoMateria = ano;
				identificacaoMateria.CodigoMateria = codigo;
				identificacaoMateria.NumeroMateria = numero;
				identificacaoMateria.SiglaSubtipoMateria = subtipo;
			}

		}
		p.setIdProposicao(identificacaoMateria.CodigoMateria);
		p.setNumero(identificacaoMateria.NumeroMateria);
		p.setAno(identificacaoMateria.AnoMateria);
		p.setTipo(identificacaoMateria.SiglaSubtipoMateria);

		if (autoresPrincipais != null && !autoresPrincipais.autores.isEmpty()) {
			AutorPrincipal autor = autoresPrincipais.autores.get(0);
			p.setAutor(autor.getDescricao());
		} else if (autoria != null && autoria.Autor != null) {
			p.setAutor(autoria.Autor.getDescricao());
		}
		p.setOrigem(Origem.SENADO);
		p.setLinkProposicao("http://www.senado.leg.br/atividade/materia/detalhes.asp?p_cod_mate=" + p.getIdProposicao());
		if (situacaoAtual == null) {
			if (situacoes == null || situacoes.situacao == null) {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINEST,
						"Nao carregou a situacao atual " + p.getLinkProposicao());
			} else {
				Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(
						Level.FINEST,
						"Nao carregou a situacao atual mas situacao foi carregada " + situacoes + " "
								+ situacoes.situacao);
			}
		} else if (situacaoAtual.autuacoes == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER)
					.log(Level.FINEST, "Nao carregou autuacoes da situacao atual");
		} else if (situacaoAtual.autuacoes.autuacoes == null) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER)
					.log(Level.FINEST, "Nao carregou autuacoes da situacao atual");
		} else if (!situacaoAtual.autuacoes.autuacoes.isEmpty()) {
			if (situacaoAtual.autuacoes.autuacoes.get(0).Local != null) {
				p.setComissao(situacaoAtual.autuacoes.autuacoes.get(0).Local.SiglaLocal);
			}
			p.setSituacao(situacaoAtual.autuacoes.autuacoes.get(0).Situacao.SiglaSituacao);
		}
		if (DadosBasicosMateria != null) {
			p.setEmenta(DadosBasicosMateria.EmentaMateria);
		} else {
			p.setEmenta(ementa);
		}

		return p;

	}

	public static void configXstream(XStream xstream) {
		xstream.processAnnotations(Materia.class);
		xstream.processAnnotations(IdentificacaoMateria.class);
		xstream.processAnnotations(DadosBasicosMateria.class);
		xstream.processAnnotations(AutoresPrincipais.class);
		xstream.processAnnotations(AutorPrincipal.class);
		
		Tramitacao.configXstream(xstream);
		Relatoria.configXstream(xstream);

		SituacaoAtual.configXstream(xstream);

	}

	public List<Tramitacao> getTramitacoes() {
		return tramitacoes;
	}
}
