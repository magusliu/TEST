package org.daochong.uai.redis;

import org.daochong.uai.SimpleTemplateFactory;
import org.daochong.ucm.Configuration;

import redis.clients.jedis.JedisCommands;

public class RedisTemplateFactory extends SimpleTemplateFactory<RedisTemplate> {

	public RedisTemplateFactory() {
		this.setSingleton(true);
	}

	public RedisTemplate build(Configuration config) {
		JedisCommands jedisCommands = new JedisCommandsFactory().build(config);
		if (jedisCommands == null)
			return null;
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setJedisCommands(jedisCommands);
		return redisTemplate;
	}

	@Override
	public Class<?> getInfoClass() {
		return RedisConfiguration.class;
	}

}
