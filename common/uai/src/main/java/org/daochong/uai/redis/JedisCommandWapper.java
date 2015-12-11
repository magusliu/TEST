package org.daochong.uai.redis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

public class JedisCommandWapper<T> implements InvocationHandler {

	private Pool<T> pool;

	public JedisCommandWapper(Pool<T> pool) {
		this.pool = pool;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final T o = pool.getResource();
		try {
			return method.invoke(o, args);
		} finally {
			pool.returnResource(o);
		}
	}

	public JedisCommands getProxy() {
		return (JedisCommands) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { JedisCommands.class }, this);
	}

}
