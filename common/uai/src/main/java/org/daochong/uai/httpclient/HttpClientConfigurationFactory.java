package org.daochong.uai.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.daochong.lang.BeanUtil;
import org.daochong.lang.Properties;
import org.daochong.ucm.PropertiesConfigurationFactory;

public class HttpClientConfigurationFactory extends PropertiesConfigurationFactory {

	@Override
	protected Object transfer(Properties prop) {
		HttpClientConfiguration config = new HttpClientConfiguration();
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : prop.keys()) {
			map.put(key, prop.getProperty(key));
		}
		BeanUtil.pushBean(map, config);
		return config;
	}

}
