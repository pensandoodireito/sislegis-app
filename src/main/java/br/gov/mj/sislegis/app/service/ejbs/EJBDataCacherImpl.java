package br.gov.mj.sislegis.app.service.ejbs;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import br.gov.mj.sislegis.app.service.EJBDataCacher;
import br.gov.mj.sislegis.app.util.SislegisUtil;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class EJBDataCacherImpl implements EJBDataCacher {

	@Schedule(hour = "00", minute = "00", second = "00")
	public void dailyJob() {
		this.evictAll();
	}

	private ConcurrentHashMap<String, Object> refDataCache = null;

	@PostConstruct
	public void initialize() {
		this.refDataCache = new ConcurrentHashMap<>();
	}

	@Override
	public void updateDataCache(String dataKey, Object obj) {
		refDataCache.put(dataKey, obj);
	}

	@Override
	public boolean isEntityCached(String dataKey) {
		boolean isCached = refDataCache.containsKey(dataKey);
		if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Cache " + dataKey + " " + (isCached ? "Hit" : "Miss"));
		}
		return refDataCache.containsKey(dataKey);
	}

	@Override
	public Object getReferenceData(String dataKey) {
		if (refDataCache.containsKey(dataKey)) {
			return refDataCache.get(dataKey);
		}
		return null;
	}

	@Override
	public void evictAll() {
		if (Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).isLoggable(Level.FINE)) {
			Logger.getLogger(SislegisUtil.SISLEGIS_LOGGER).log(Level.FINE, "Limpando o cache dos EJBs");
		}
		refDataCache.clear();
	}
}
