package org.daochong.uai.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.daochong.lang.BeanUtil;
import org.daochong.lang.Properties;
import org.daochong.ucm.PropertiesConfigurationFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfigurationFactory extends PropertiesConfigurationFactory {

	@Override
	protected Object transfer(Properties prop) {
		Set<HostAndPort> list = new HashSet<HostAndPort>();
		Map<String, Object> poolConfig = new HashMap<String, Object>();
		for (String name : prop.keys()) {
			if (name.equals("UAI_configId") || name.equals("UAI_groupId")) {
				continue;
			} else if (name.startsWith("UAI_server")) {
				String value = prop.getProperty(name);
				int pos = value.lastIndexOf(":");
				if (pos > 0) {
					try {
						HostAndPort host = new HostAndPort(value.substring(0, pos),
								Integer.parseInt(value.substring(pos + 1)));
						list.add(host);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			} else {
				poolConfig.put(name, prop.getProperty(name));
			}
		}
		if (list.size() == 0) {
			return null;
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		if (poolConfig.size() > 0) {
			try {
				new BeanUtil().push(poolConfig, jedisPoolConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RedisConfiguration config = new RedisConfiguration();
		config.setServers(list);
		config.setJedisPoolConfig(jedisPoolConfig);
		return config;
	}

}
