package br.gov.mj.sislegis.app.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;

import br.gov.mj.sislegis.app.enumerated.Origem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
//@formatter:off
@NamedNativeQueries({
  @NamedNativeQuery(
          name    =   "getAllProposicoesSeguidas",
          query   =   "SELECT * " +
                      "FROM Proposicao a where a.id in (select distinct proposicoesSeguidas_id from Usuario_ProposicaoSeguida)",
                      resultClass=Proposicao.class
  )
})
//@formatter:on
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Proposicao extends AbstractEntity {

	private static final long serialVersionUID = 7949894944142814382L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false, unique = true)
	private Integer idProposicao;

	@Column
	private String tipo;

	@Column
	private String ano;

	@Column
	private String numero;

	@Column
	private String situacao;

	@Column
	private String autor;

	@Enumerated(EnumType.STRING)
	@Column
	private Origem origem;

	@Column(length = 2000)
	private String resultadoASPAR;

	@Transient
	private String comissao;

	@Transient
	private Integer seqOrdemPauta;

	@Transient
	private String sigla;

	@Column(length = 2000)
	private String ementa;

	@Column
	private String linkProposicao;

	@Transient
	private String linkPauta;

	@Column
	private Posicionamento posicionamento;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario responsavel;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "proposicao")
	private Set<TagProposicao> tags;

	@Transient
	private Set<Comentario> listaComentario = new HashSet<>();

	@Transient
	private Set<EncaminhamentoProposicao> listaEncaminhamentoProposicao = new HashSet<>();

	@Transient
	private Reuniao reuniao;

	@Column(nullable = false)
	private boolean isFavorita;


	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "proposicao")
	@OrderBy("data ASC")
	private SortedSet<AlteracaoProposicao> alteracoesProposicao = new TreeSet<AlteracaoProposicao>();

	@Transient
	private Integer totalComentarios = 0;

	@Transient
	private Integer totalEncaminhamentos = 0;


	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "proposicoesFilha")
	private Set<Proposicao> proposicoesPai;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "proposicao_proposicao", joinColumns = { @JoinColumn(name = "proposicao_id") }, inverseJoinColumns = { @JoinColumn(name = "proposicao_id_filha") })
	private Set<Proposicao> proposicoesFilha;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "proposicao_elaboracao_normativa", joinColumns = { @JoinColumn(name = "proposicao_id") }, inverseJoinColumns = { @JoinColumn(name = "elaboracao_normativa_id") })
	private Set<ElaboracaoNormativa> elaboracoesNormativas;

	public String getSigla() {
		if (Objects.isNull(sigla))
			sigla = getTipo() + " " + getNumero() + "/" + getAno();
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Integer getIdProposicao() {
		return idProposicao;
	}

	public void setIdProposicao(Integer idProposicao) {
		this.idProposicao = idProposicao;
	}

	public String getTipo() {
		if (tipo != null && !tipo.isEmpty()) {
			tipo = tipo.trim();
		}
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEmenta() {
		return ementa;
	}

	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getComissao() {
		return comissao;
	}

	public void setComissao(String comissao) {
		this.comissao = comissao;
	}

	public Integer getSeqOrdemPauta() {
		return seqOrdemPauta;
	}

	public void setSeqOrdemPauta(Integer seqOrdemPauta) {
		this.seqOrdemPauta = seqOrdemPauta;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public String getLinkProposicao() {
		return linkProposicao;
	}

	public void setLinkProposicao(String linkProposicao) {
		this.linkProposicao = linkProposicao;
	}

	public String getLinkPauta() {
		return linkPauta;
	}

	public void setLinkPauta(String linkPauta) {
		this.linkPauta = linkPauta;
	}

	public Posicionamento getPosicionamento() {
		return posicionamento;
	}

	public void setPosicionamento(Posicionamento posicionamento) {
		this.posicionamento = posicionamento;
	}

	public Set<Comentario> getListaComentario() {
		return this.listaComentario;
	}

	public void setListaComentario(final Set<Comentario> listaComentario) {
		this.listaComentario = listaComentario;
	}

	public Set<EncaminhamentoProposicao> getListaEncaminhamentoProposicao() {
		return listaEncaminhamentoProposicao;
	}

	public void setListaEncaminhamentoProposicao(Set<EncaminhamentoProposicao> listaEncaminhamentoProposicao) {
		this.listaEncaminhamentoProposicao = listaEncaminhamentoProposicao;
	}

	public Set<TagProposicao> getTags() {
		return tags;
	}

	public void setTags(Set<TagProposicao> tags) {
		this.tags = tags;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Reuniao getReuniao() {
		return reuniao;
	}

	public void setReuniao(Reuniao reuniao) {
		this.reuniao = reuniao;
	}

	public String getResultadoASPAR() {
		return resultadoASPAR;
	}

	public void setResultadoASPAR(String resultadoASPAR) {
		this.resultadoASPAR = resultadoASPAR;
	}

	public boolean isFavorita() {
		return isFavorita;
	}

	public void setFavorita(boolean isFavorita) {
		this.isFavorita = isFavorita;
	}

	public Integer getTotalComentarios() {
		if (CollectionUtils.isNotEmpty(listaComentario)){
			totalComentarios = listaComentario.size();
		}
		return totalComentarios;
	}

	public void setTotalComentarios(Integer totalComentarios) {
		this.totalComentarios = totalComentarios;
	}

	public Integer getTotalEncaminhamentos() {
		if (CollectionUtils.isNotEmpty(listaEncaminhamentoProposicao)){
			totalEncaminhamentos = listaEncaminhamentoProposicao.size();
		}
		return totalEncaminhamentos;
	}

	public void setTotalEncaminhamentos(Integer totalEncaminhamentos) {
		this.totalEncaminhamentos = totalEncaminhamentos;
	}

	public Set<Proposicao> getProposicoesPai() {
		return proposicoesPai;
	}

	public Set<Proposicao> getProposicoesFilha() {
		return proposicoesFilha;
	}

	public void setProposicoesPai(Set<Proposicao> proposicoesPai) {
		this.proposicoesPai = proposicoesPai;
	}

	public void setProposicoesFilha(Set<Proposicao> proposicoesFilha) {
		this.proposicoesFilha = proposicoesFilha;
	}

	public Set<ElaboracaoNormativa> getElaboracoesNormativas() {
		return elaboracoesNormativas;
	}

	public void setElaboracoesNormativas(Set<ElaboracaoNormativa> elaboracoesNormativas) {
		this.elaboracoesNormativas = elaboracoesNormativas;
	}

	@JsonIgnore
	public Set<AlteracaoProposicao> getAlteracoesProposicao() {
		return alteracoesProposicao;
	}

	@JsonIgnore
	public AlteracaoProposicao getLastAlteracoesProposicao() {
		return alteracoesProposicao.last();
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";

		result += "idProposicao: " + idProposicao;

		result += ", tipo: " + tipo;

		result += ", ano: " + ano;

		result += ", numero: " + numero;
		if (autor != null && !autor.trim().isEmpty())
			result += ", autor: " + autor;
		if (comissao != null && !comissao.trim().isEmpty())
			result += ", comissao: " + comissao;
		if (seqOrdemPauta != null)
			result += ", seqOrdemPauta: " + seqOrdemPauta;
		if (situacao != null)
			result += ", situacao: " + situacao;
		return result;
	}

	public void addAlteracao(AlteracaoProposicao altera) {
		altera.setProposicao(this);
		alteracoesProposicao.add(altera);

	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String siglaSituacao) {
		this.situacao = siglaSituacao;
	}

}
