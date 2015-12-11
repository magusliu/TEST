package org.daochong.uai.jdbc;

import javax.sql.DataSource;

public class JdbcTemplateConfiguration {
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
