package br.gov.mj.sislegis.app.parser.senado.xstream;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Situacoes {
	@XStreamAlias("SituacaoAtual")
	List<Situacao> situacao = new ArrayList<Situacao>();
}
