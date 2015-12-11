package org.daochong.uai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.daochong.lang.BeanUtil;
import org.daochong.ucm.Configuration;
import org.daochong.ucm.ConfigurationContainer;

@SuppressWarnings("all")
public class DefaultUniteAccessInterfaceFactory implements UniteAccessInterfaceFactory {
	private List<TemplateFactory<?>> templateFactorys;
	private ConfigurationContainer configurationContainer;
	private Map<Class<?>, TemplateFactory<?>> clsMapping;
	private Map<String, ObjectCache> objects;
	private ThreadLocal<Map<String, ObjectCache>> locales;

	public DefaultUniteAccessInterfaceFactory() {
		this.clsMapping = new HashMap<Class<?>, TemplateFactory<?>>();
		this.objects = new HashMap<String, ObjectCache>();
		this.locales = new ThreadLocal<Map<String, ObjectCache>>();
	}

	public ObjectCache getLocale(String key) {
		Map<String, ObjectCache> map = locales.get();
		if (map == null) {
			map = new HashMap<String, ObjectCache>();
			locales.set(map);
		}
		return map.get(key);
	}

	public void setLocale(String key, ObjectCache obj) {
		Map<String, ObjectCache> map = locales.get();
		if (map == null) {
			map = new HashMap<String, ObjectCache>();
			locales.set(map);
		}
		map.put(key, obj);
	}

	public void addTemplateFactory(TemplateFactory<?> templateFactory) {
		if (templateFactory == null)
			return;
		this.clsMapping.put(templateFactory.getBeanClass(), templateFactory);
	}

	public ConfigurationContainer getConfigurationContainer() {
		if (this.configurationContainer == null)
			throw new RuntimeException("container is null");
		return configurationContainer;
	}

	public List<TemplateFactory<?>> getTemplateFactorys() {
		return templateFactorys;
	}

	public void setTemplateFactorys(List<TemplateFactory<?>> templateFactorys) {
		this.templateFactorys = templateFactorys;
		if (this.templateFactorys != null) {
			for (TemplateFactory<?> templateFactory : this.templateFactorys) {
				addTemplateFactory(templateFactory);
			}
		}
	}

	public void setConfigurationContainer(ConfigurationContainer configurationContainer) {
		this.configurationContainer = configurationContainer;
	}

	protected void loadTemplateFactory() {

	}

	public <T> TemplateFactory<T> getTemplateFactory(Class<T> clazz) {
		if (this.clsMapping.size() == 0) {
			loadTemplateFactory();
		}
		return (TemplateFactory<T>) this.clsMapping.get(clazz);
	}

	public <T> T getTemplate(Class<T> clazz) {
		TemplateFactory<?> factory = getTemplateFactory(clazz);
		if (factory == null) {
			throw new RuntimeException("class[" + clazz.getName() + "] is no factory");
		}
		Configuration config = null;
		if (factory.getInfoClass() != null) {
			config = this.getConfigurationContainer().getConfiguration(factory.getInfoClass());
		} else {
			config = this.getConfigurationContainer().getConfiguration();
		}
		if (config == null)
			return null;
		return create(clazz, config);
	}

	protected <T> T create(Class<T> clazz, Configuration config) {

		TemplateFactory<T> templateFactory = getTemplateFactory(clazz);
		if (templateFactory == null) {
			throw new RuntimeException("class[" + clazz.getName() + "] is no factory");
		}
		try {
			ObjectCache cache = null;
			String key = config.getGroupId() + "_" + config.getConfigId() + "_" + clazz.getName();
			if (templateFactory.isSingleton()) {
				cache = objects.get(key);
			} else if (templateFactory.isThreadLocal()) {
				cache = this.getLocale(key);
			}
			if (cache != null) {
				if (this.getConfigurationContainer().isExpired(cache.getConfig())) {
					templateFactory.destory((T) cache.getObject());
				} else if (BeanUtil.isMatch(cache.getObject(), clazz)) {
					return (T) cache.getObject();
				}
			}
			T o = (T) templateFactory.build(config);
			if (templateFactory.isSingleton()) {
				cache = new ObjectCache();
				cache.config = config;
				cache.object = o;
				this.objects.put(key, cache);
			} else if (templateFactory.isThreadLocal()) {
				cache = new ObjectCache();
				cache.config = config;
				cache.object = o;
				this.setLocale(key, cache);
			}
			return o;
		} catch (Throwable e) {
			throw new RuntimeException("build template fail", e);
		}
	}

	public <T> T getTemplate(String beanId, Class<T> clazz) {
		Configuration config = this.getConfigurationContainer().getConfiguration(beanId);
		if (config == null)
			return null;
		return create(clazz, config);
	}

	public <T> T getTemplate(String beanId, String groupId, Class<T> clazz) {
		Configuration config = this.getConfigurationContainer().getConfiguration(beanId, groupId);
		if (config == null)
			return null;
		return create(clazz, config);
	}

	class ObjectCache {
		private Configuration config;
		private Object object;

		public Configuration getConfig() {
			return config;
		}

		public void setConfig(Configuration config) {
			this.config = config;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}
	}

	public void close() {
		try {
			if (objects.size() > 0) {
				for (ObjectCache cache : objects.values()) {
					TemplateFactory<Object> f = (TemplateFactory<Object>) this.clsMapping
							.get(cache.getObject().getClass());
					if (f != null) {
						f.destory(cache.getObject());
					}
				}
			}
			this.clsMapping.clear();
			this.configurationContainer = null;
			this.objects.clear();
			if (this.templateFactorys != null) {
				this.templateFactorys.clear();
			}
		} catch (Throwable e) {

		}
	}

}
