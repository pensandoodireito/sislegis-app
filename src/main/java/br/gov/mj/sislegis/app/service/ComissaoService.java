package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.enumerated.Origem;
import br.gov.mj.sislegis.app.model.Comissao;


@Local
public interface ComissaoService extends Service<Comissao> {
	
	public List<Comissao> listarComissoesCamara()throws Exception;
	public List<Comissao> listarComissoesSenado()throws Exception;
	public  Comissao getBySigla(String sigla) throws Exception;
	public String getComissaoCamara(Long idComissao) throws Exception;
	public Comissao getComissao(Origem origem, String sigla) throws Exception;

}
