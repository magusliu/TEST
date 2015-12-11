package org.daochong.ucm;

import java.util.HashMap;
import java.util.Map;

import org.daochong.lang.BeanUtil;
import org.daochong.lang.Properties;

public abstract class BasePropertiesConfigurationFactory extends DefaultConfigurationFactory {
	

	protected Configuration config(Properties prop) {
		try {
			Configuration config = new Configuration();
			config.setProperties(prop);
			if (prop.getProperty("UAI_configId") != null) {
				config.setConfigId(prop.getProperty("UAI_configId"));
			}
			if (prop.getProperty("UAI_groupId") != null) {
				config.setGroupId(prop.getProperty("UAI_groupId"));
			}
			Object o = transfer(prop);
			if (o != null) {
				config.setConfigBean(o);
			}
			return config;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Object transfer(Properties prop) {
		String cls = prop.getProperty("UAI_class");
		if (cls != null) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String key : prop.keys()) {
					if (key.startsWith("UAI_"))
						continue;
					map.put(key, prop.getProperty(key));
				}
				Class<?> clazz = Class.forName(cls);
				Object o = BeanUtil.pushBean(map, clazz);
				return o;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
