package org.daochong.ucm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class DefaultConfigurationContainer implements ConfigurationContainer {

	private Map<String, Map<String, Configuration>> groups;
	private Map<Class<?>, List<Configuration>> clsMappings;
	private Map<String, List<ConfigurationFactory>> factorys;
	private List<Configuration> configs;

	public DefaultConfigurationContainer() {
		groups = new LinkedHashMap<String, Map<String, Configuration>>();
		clsMappings = new HashMap<Class<?>, List<Configuration>>();
		factorys = new LinkedHashMap<String, List<ConfigurationFactory>>();
		configs = new ArrayList<Configuration>();
	}

	public Configuration getConfiguration() {
		Configuration config = null;
		if (this.groups.get("default") != null) {
			config = this.groups.get("default").get("default");
		}
		if (config == null && configs.size() > 0) {
			config = configs.get(0);
		}
		return config;
	}

	public Configuration getConfiguration(Class<?> clazz) {
		List<Configuration> list = this.clsMappings.get(clazz);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Configuration getConfiguration(String beanId) {
		Map<String, Configuration> map = this.groups.get("default");
		if (map == null) {
			for (String key : this.groups.keySet()) {
				map = this.groups.get(key);
				break;
			}
		}
		if (map != null) {
			if (map.size() > 0) {
				return map.get(beanId);
			}
		}
		return null;
	}

	public Configuration getConfigurationGroup(String groupId) {
		Map<String, Configuration> map = this.groups.get(groupId);
		if (map != null) {
			if (map.size() > 0) {
				Configuration config = map.get("default");
				if (config != null) {
					return config;
				}
				for (String key : map.keySet()) {
					return map.get(key);
				}
			}
		}
		return null;
	}

	public Configuration getConfiguration(String groupId, Class<?> clazz) {
		List<Configuration> list = this.clsMappings.get(clazz);
		if (list != null && list.size() > 0) {
			for (Configuration config : list) {
				if (config.getGroupId().equals(groupId) && config.getConfigId().equals("default")) {
					return config;
				}
			}
			for (Configuration config : list) {
				if (config.getGroupId().equals(groupId)) {
					return config;
				}
			}
		}
		return null;
	}

	public Configuration getConfiguration(String beanId, String groupId) {
		Map<String, Configuration> map = this.groups.get(groupId);
		if (map != null) {
			return map.get(beanId);
		}
		return null;
	}

	public List<String> groupKeys(String groupId) {
		Map<String, Configuration> map = this.groups.get(groupId);
		if (map == null)
			return new ArrayList<String>();
		List<String> list = new ArrayList<String>(map.size());
		list.addAll(map.keySet());
		return list;
	}

	public List<String> groups() {
		List<String> list = new ArrayList<String>(groups.size());
		list.addAll(groups.keySet());
		return list;
	}

	public boolean isExpired(Configuration configuration) {
		for (Configuration config : this.configs) {
			if (config.equals(configuration)) {
				return config.getConfigTime() != configuration.getConfigTime();
			}
		}
		return false;
	}

	public void referesh() {
		for (String key : this.factorys.keySet()) {
			for (ConfigurationFactory factory : this.factorys.get(key)) {
				try {
					factory.load();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void referesh(String groupId) {
		if (this.factorys.containsKey(groupId)) {
			for (ConfigurationFactory factory : this.factorys.get(groupId)) {
				try {
					factory.load();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		for (Configuration cfg : this.configs) {
			if (cfg.getConfigBean() != null) {
				destory(cfg.getConfigBean());
			}
		}
	}

	private void destory(Object obj) {
		if (obj == null)
			return;
		try {
			Method m = obj.getClass().getDeclaredMethod("close");
			m.setAccessible(true);
			m.invoke(obj);
			return;
		} catch (Throwable e) {

		}
		try {
			Method m = obj.getClass().getDeclaredMethod("destory");
			m.setAccessible(true);
			m.invoke(obj);
			return;
		} catch (Throwable e) {

		}
	}

	protected synchronized void updateConfiguration(ConfigurationFactory factory, Configuration config) {
		if (config.getGroupId() == null)
			throw new RuntimeException("config groupId is null");
		if (config.getConfigId() == null)
			throw new RuntimeException("config configId is null");
		if (this.configs.contains(config)) {
			this.configs.remove(config);
		}
		this.configs.add(config);

		if (config.getConfigBean() != null) {
			List<Configuration> list = this.clsMappings.get(config.getConfigBean().getClass());
			if (list == null) {
				list = new ArrayList<Configuration>();
				this.clsMappings.put(config.getConfigBean().getClass(), list);
			}
			if (list.contains(config)) {
				list.remove(config);
			}
			list.add(config);
		}
		Map<String, Configuration> map = this.groups.get(config.getGroupId());
		List<ConfigurationFactory> list = this.factorys.get(config.getGroupId());
		if (map == null) {
			map = new HashMap<String, Configuration>();
			this.groups.put(config.getGroupId(), map);
			list = new ArrayList<ConfigurationFactory>();
			this.factorys.put(config.getGroupId(), list);
		}

		map.put(config.getConfigId(), config);
		if (!list.contains(factory)) {
			list.add(factory);
		}
	}

}
