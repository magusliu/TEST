package org.daochong.session;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.daochong.lang.BeanUtil;

import redis.clients.jedis.JedisCommands;

public class RedisSessionContext implements SessionContext {

	private String prefix = "DAOCHONG_SESSIONS";

	private JedisCommands jedisCommands;

	private int maxInactiveInterval = 86400;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public JedisCommands getJedisCommands() {
		return jedisCommands;
	}

	public void setJedisCommands(JedisCommands jedisCommands) {
		this.jedisCommands = jedisCommands;
	}

	public Session getSession(String sessionId) {
		RedisSession session = new RedisSession();
		session.setSessionId(sessionId);
		session.setMaxInactiveInterval(getMaxInactiveInterval());
		session.setSessionContext(this);
		JedisCommands jedis = this.getJedisCommands();
		String prefix = this.getPrefix();
		if (jedis.exists(prefix)) {
			if (!"set".equals(jedis.type(prefix))) {
				jedis.del(prefix);
			}
			if (!jedis.sismember(prefix, sessionId)) {
				jedis.sadd(prefix, sessionId);
				session.setNewSession(true);
			}
		} else {
			jedis.sadd(prefix, sessionId);
			session.setNewSession(true);
		}
		String nSessionId = prefix + "_" + sessionId;
		if (jedis.exists(nSessionId)) {
			if (!"hash".equals(jedis.type(nSessionId))) {
				jedis.del(nSessionId);
			}
			session.setCreateTime(Long.parseLong(jedis.hget(nSessionId, "createTime")));
			Set<String> sets = jedis.hkeys(nSessionId);
			sets.remove("createTime");
			session.setAttributeNames(sets);
		} else {
			session.setAttributeNames(new LinkedHashSet<String>());
			jedis.hset(nSessionId, "createTime", String.valueOf(session.getCreateTime()));

		}
		jedis.expire(nSessionId, getMaxInactiveInterval());
		return session;
	}

	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public Set<String> getSessionKeys() {
		return this.getJedisCommands().smembers(getPrefix());
	}

	public void close() {
		JedisCommands jedis = this.getJedisCommands();
		for (String key : getSessionKeys()) {
			jedis.del(getPrefix() + "_" + key);
		}
		jedis.del(getPrefix());
	}

	public void removeSession(String sessionId) {
		this.getJedisCommands().srem(getPrefix(), sessionId);
		this.getJedisCommands().del(this.getPrefix() + "_" + sessionId);
	}

	@SuppressWarnings("unchecked")
	public Object getSessionAttribute(String sessionId, String id) {
		if (sessionId == null || id == null)
			return null;
		this.getJedisCommands().expire(this.getPrefix() + "_" + sessionId, getMaxInactiveInterval());
		String str = this.getJedisCommands().hget(getPrefix() + "_" + sessionId, id);
		if (str == null)
			return null;
		if (str.startsWith("object:")) {
			str = str.substring(7);
			return BeanUtil.unSerialize(str);
		} else if (str.startsWith("bean:")) {
			str = str.substring(5);
			int pos = str.indexOf(":");
			if (pos == -1)
				return str;
			String cls = str.substring(0, pos);
			try {
				Class<?> clazz = Class.forName(cls);
				Map<String, Object> map = (Map<String, Object>) BeanUtil
						.unSerialize(str.substring(pos + 1));
				return BeanUtil.pushBean(map, clazz);
			} catch (Throwable e) {
				throw new RuntimeException("make bean file", e);
			}
		}
		return str;
	}

	public void setSessionAttribute(String sessionId, String id, Object o) {
		if (sessionId == null || id == null && o == null)
			return;
		this.getJedisCommands().expire(this.getPrefix() + "_" + sessionId, getMaxInactiveInterval());
		if (o instanceof String) {
			this.getJedisCommands().hset(getPrefix() + "_" + sessionId, id, (String) o);
		} else {
			if (o instanceof java.io.Serializable) {
				String data = BeanUtil.serialize(o);
				if (data == null)
					throw new RuntimeException("no serializable");
				String str = "object:" +data;
				this.getJedisCommands().hset(getPrefix() + "_" + sessionId, id, str);
			} else {
				Map<String, Object> map = BeanUtil.popBean(o);
				String data = BeanUtil.serialize(map);
				if (data == null)
					throw new RuntimeException("no serializable");
				String str = "bean:" + o.getClass().getName() + ":" + data;
				this.getJedisCommands().hset(getPrefix() + "_" + sessionId, id, str);
			}
		}
	}

	public long getSessionIdle(String sessionId) {
		return this.getJedisCommands().ttl(getPrefix() + "_" + sessionId);
	}

	public Session getDefaultSession() {
		return getSession(this.getClass().getName());
	}

}
