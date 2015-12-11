package org.daochong.uai.redis;

import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfiguration {
	private Set<HostAndPort> servers;
	private JedisPoolConfig jedisPoolConfig;

	public Set<HostAndPort> getServers() {
		return servers;
	}

	public void setServers(Set<HostAndPort> servers) {
		this.servers = servers;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

}
