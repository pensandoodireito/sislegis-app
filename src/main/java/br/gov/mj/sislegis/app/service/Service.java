package br.gov.mj.sislegis.app.service;

import java.util.List;

import javax.ejb.Local;

import br.gov.mj.sislegis.app.model.Equipe;


@Local
public interface Service<T> {

	public List<T> listAll();
	public T save(T entity);
	public T findById(Long id);
	public List<T> findByProperty(String property, Object value, String orderBy);
	public T findByProperty(String property, Object value);
	public void deleteById(Long id);
	
	
}
