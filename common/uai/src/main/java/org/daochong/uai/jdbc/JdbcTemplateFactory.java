package org.daochong.uai.jdbc;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.daochong.lang.JdbcTemplate;
import org.daochong.uai.SimpleTemplateFactory;
import org.daochong.ucm.Configuration;

public class JdbcTemplateFactory extends SimpleTemplateFactory<JdbcTemplate> {

	public JdbcTemplateFactory() {
		this.setThreadLocal(true);
		this.setSingleton(false);
	}

	public JdbcTemplate build(Configuration config) {
		if (config == null)
			return null;
		Object o = config.getConfigBean();
		if (o == null)
			return null;
		if (o instanceof JdbcTemplateConfiguration) {
			return new JdbcTemplate(((JdbcTemplateConfiguration) o).getDataSource());
		}
		return null;
	}

	@Override
	public Class<?> getInfoClass() {
		return JdbcTemplateConfiguration.class;
	}

	@Override
	public void destory(JdbcTemplate obj) {
		try {
			DataSource ds = obj.getDataSource();
			Method m = ds.getClass().getMethod("close");
			m.invoke(ds);
		} catch (Throwable e) {

		}
	}

}
