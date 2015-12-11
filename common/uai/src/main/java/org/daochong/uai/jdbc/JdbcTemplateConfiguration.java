package org.daochong.uai.jdbc;

import java.lang.reflect.Method;

import javax.sql.DataSource;

public class JdbcTemplateConfiguration {
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void close() {
		if (this.dataSource != null) {
			try {
				Method m = this.dataSource.getClass().getDeclaredMethod("close");
				m.setAccessible(true);
				m.invoke(this.dataSource);
			} catch (Throwable e) {

			}
		}
	}
}
