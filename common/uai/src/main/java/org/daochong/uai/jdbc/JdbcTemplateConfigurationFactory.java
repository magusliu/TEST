package org.daochong.uai.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.daochong.lang.BeanUtil;
import org.daochong.lang.Properties;
import org.daochong.ucm.PropertiesConfigurationFactory;

public class JdbcTemplateConfigurationFactory extends PropertiesConfigurationFactory {

	@Override
	protected Object transfer(Properties prop) {
		String cls = prop.getProperty("UAI_poolClass");
		if (cls == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : prop.keys()) {
			if (key.startsWith("UAI_"))
				continue;
			map.put(key, prop.getProperty(key));
		}
		try {
			JdbcTemplateConfiguration config = new JdbcTemplateConfiguration();
			Object o = Class.forName(cls).newInstance();
			if (o instanceof DataSource) {
				BeanUtil.pushBean(map, o);
				config.setDataSource((DataSource) o);
				return config;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
