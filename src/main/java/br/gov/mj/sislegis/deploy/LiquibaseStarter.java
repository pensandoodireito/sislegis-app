package br.gov.mj.sislegis.deploy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.naming.spi.DirStateFactory.Result;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

@Dependent
public class LiquibaseStarter {
	@Produces
	@LiquibaseType
	public CDILiquibaseConfig createConfig() {
		CDILiquibaseConfig config = new CDILiquibaseConfig();
		config.setChangeLog("sislegisdb.postinstall.changelog.xml");

		return config;
	}

	@Resource(name = "java:jboss/datasources/SISLEGIS")
	private DataSource ds;

	@Produces
	@LiquibaseType
	public DataSource createDataSource() {
		return ds;
	}

	@Produces
	@LiquibaseType
	public ResourceAccessor create() {
		System.out.println("criou aqui! ds " + ds);
		try {
			PreparedStatement ps = ds.getConnection().prepareCall("select id from PUBLIC.DATABASECHANGELOG");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ClassLoaderResourceAccessor(getClass().getClassLoader());
	}
}
