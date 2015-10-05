package br.gov.mj.sislegis.app.service;

public interface EJBDataCacher {
	/**
	 * Returna os dados cacheados
	 */
	Object getReferenceData(String key);

	/**
	 * Limpa o cache
	 */
	void evictAll();

	void updateDataCache(String dataKey, Object obj);

	boolean isEntityCached(String dataKey);
}
