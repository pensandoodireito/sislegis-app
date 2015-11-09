package br.gov.mj.sislegis.deploy;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

public class LiquibaseStarter {
	@Produces
	@LiquibaseType
	public CDILiquibaseConfig createConfig() {
		CDILiquibaseConfig config = new CDILiquibaseConfig();
		config.setChangeLog("liquibase/parser/core/xml/simpleChangeLog.xml");
		return config;
	}

	@Resource(name = "java:jboss/datasources/ExampleDS")
	private DataSource ds;

	@Produces
	@LiquibaseType
	public DataSource createDataSource() {
		return ds;
	}

	@Produces
	@LiquibaseType
	public ResourceAccessor create() {
		return new ClassLoaderResourceAccessor(getClass().getClassLoader());
	}
}
