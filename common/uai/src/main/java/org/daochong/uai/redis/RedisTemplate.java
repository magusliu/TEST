package org.daochong.uai.redis;

import org.daochong.lang.BeanUtil;

import redis.clients.jedis.JedisCommands;

public class RedisTemplate {
	private JedisCommands jedisCommands;

	public JedisCommands getJedisCommands() {
		return jedisCommands;
	}

	public void setJedisCommands(JedisCommands jedisCommands) {
		this.jedisCommands = jedisCommands;
	}

	public Object getObject(String key) {
		String str = jedisCommands.get(key);
		Object o = BeanUtil.unSerialize(str);
		return o;
	}

	public void setObject(String key, Object obj) {
		String str = BeanUtil.serialize(obj);
		jedisCommands.set(key, str);
	}

	public void setObject(String key, Object obj, int times) {
		String str =BeanUtil.serialize(obj);
		jedisCommands.setex(key, times, str);
	}

	public String get(String key) {
		return jedisCommands.get(key);
	}

	public void set(String key, String value) {
		jedisCommands.set(key, value);
	}
	public void set(String key, String value,int times) {
		jedisCommands.setex(key,times, value);
	}
}
