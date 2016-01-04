package com.khinf.mobile.web.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.daochong.lang.DefaultEventHanlder;
import org.daochong.lang.EventListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringEventHanlder extends DefaultEventHanlder implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@Override
	public Map<Class<?>, List<EventListener<Object>>> getCache() {
		Map<Class<?>, List<EventListener<Object>>> map = super.getCache();
		if (map == null) {
			try {
				map = new HashMap<Class<?>, List<EventListener<Object>>>();
				for (EventListener<Object> ls : this.applicationContext.getBeansOfType(EventListener.class).values()) {
					List<EventListener<Object>> list = map.get(ls.getListenerClass());
					if (list == null) {
						list = new ArrayList<EventListener<Object>>();
						map.put(ls.getListenerClass(), list);
					}
					list.add(ls);
				}
				this.setCache(map);
			} catch (Throwable e) {

			}
		}
		return map;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
