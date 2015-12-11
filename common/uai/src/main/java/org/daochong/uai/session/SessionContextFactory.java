package org.daochong.uai.session;

import org.daochong.lang.Properties;
import org.daochong.session.RedisSessionContext;
import org.daochong.session.SessionContext;
import org.daochong.uai.SimpleTemplateFactory;
import org.daochong.uai.redis.JedisCommandsFactory;
import org.daochong.uai.redis.RedisConfiguration;
import org.daochong.ucm.Configuration;

import redis.clients.jedis.JedisCommands;

public class SessionContextFactory extends SimpleTemplateFactory<SessionContext> {
	@Override
	public Class<?> getInfoClass() {
		return RedisConfiguration.class;
	}

	public SessionContext build(Configuration config) {
		JedisCommands commands = new JedisCommandsFactory().build(config);
		if (commands != null) {
			RedisSessionContext context = new RedisSessionContext();
			context.setJedisCommands(commands);
			Properties p = config.getProperties();
			if (p != null) {
				if (p.getProperty("UAI_SessionPrefix") != null) {
					context.setPrefix(p.getProperty("UAI_SessionPrefix"));
				}
				if (p.getProperty("UAI_SessionTimeOut") != null) {
					context.setMaxInactiveInterval(p.getInt("UAI_SessionTimeOut", 86400));
				}
			}
			return context;
		}
		return null;
	}
}
