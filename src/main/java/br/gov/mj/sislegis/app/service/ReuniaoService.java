package br.gov.mj.sislegis.app.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Reuniao;

@Local
public interface ReuniaoService extends Service<Reuniao> {
	
	Reuniao buscaReuniaoPorData(Date data);

	List<Reuniao> reunioesPorMes(Integer mes, Integer ano);

}
