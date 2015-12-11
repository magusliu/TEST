package org.daochong.uai.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.daochong.uai.SimpleTemplateFactory;
import org.daochong.ucm.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisCommandsFactory extends SimpleTemplateFactory<JedisCommands> {
	
	public JedisCommandsFactory(){
		this.setSingleton(true);
	}

	public JedisCommands build(Configuration config) {
		Object o = config.getConfigBean();
		if (o == null)
			return null;
		if (!(o instanceof RedisConfiguration))
			return null;
		RedisConfiguration configInfo = (RedisConfiguration) o;
		if (configInfo.getServers() == null || configInfo.getServers().size() == 0) {
			return null;
		}
		JedisCommands jedisCommands = null;
		Set<HostAndPort> nodes = configInfo.getServers();
		if (nodes.size() == 1) {
			HostAndPort hp = null;
			for (HostAndPort h : nodes) {
				hp = h;
				break;
			}
			JedisPool pool = new JedisPool(configInfo.getJedisPoolConfig(), hp.getHost(), hp.getPort());
			JedisCommandWapper<Jedis> wapper = new JedisCommandWapper<Jedis>(pool);
			jedisCommands = wapper.getProxy();
		} else if (nodes.size() < 6) {
			List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
			for (HostAndPort hp : nodes) {
				JedisShardInfo info = new JedisShardInfo(hp.getHost(), hp.getPort());
				list.add(info);
			}
			ShardedJedisPool sp = new ShardedJedisPool(configInfo.getJedisPoolConfig(), list);
			JedisCommandWapper<ShardedJedis> wapper = new JedisCommandWapper<ShardedJedis>(sp);
			jedisCommands = wapper.getProxy();
		} else {
			jedisCommands = new JedisCluster(nodes, configInfo.getJedisPoolConfig());
		}
		return jedisCommands;
	}

	@Override
	public Class<?> getInfoClass() {
		return RedisConfiguration.class;
	}
}
