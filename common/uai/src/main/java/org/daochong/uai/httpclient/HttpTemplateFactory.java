package org.daochong.uai.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.daochong.lang.BeanUtil;
import org.daochong.lang.Properties;
import org.daochong.lang.SimpleHttpClient;
import org.daochong.uai.SimpleTemplateFactory;
import org.daochong.ucm.Configuration;

public class HttpTemplateFactory extends SimpleTemplateFactory<SimpleHttpClient> {

	public HttpTemplateFactory() {
		this.setSingleton(true);
	}

	public SimpleHttpClient build(Configuration config) {
		Object o = config.getConfigBean();
		if (o == null || !(config.getConfigBean() instanceof HttpClientConfiguration)) {
			return null;
		}
		SimpleHttpClient client = new SimpleHttpClient();
		Properties prop = config.getProperties();
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : prop.keys()) {
			map.put(key, prop.getProperty(key));
		}
		BeanUtil.pushBean(map, client);
		return client;
	}

	@Override
	public Class<?> getInfoClass() {
		return HttpClientConfiguration.class;
	}

}
