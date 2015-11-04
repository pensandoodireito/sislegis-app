package br.gov.mj.sislegis.app.parser.senado.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Resultado")
public class Resultado {

    @XStreamAlias("Descricao")
    String descricao;

}
