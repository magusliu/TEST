package org.daochong.ucm;

import org.daochong.lang.Properties;

@SuppressWarnings("all")
public class Configuration {

	private static final long serialVersionUID = 1L;
	private String configId;
	private String groupId;
	private long configTime;
	private Properties properties;
	private Object configBean;
	private ConfigurationFactory configurationFactory;

	public Configuration() {
		configId = "default";
		groupId = "default";
		configTime = System.currentTimeMillis();
	}

	public ConfigurationFactory getConfigurationFactory() {
		return configurationFactory;
	}

	public void setConfigurationFactory(ConfigurationFactory configurationFactory) {
		this.configurationFactory = configurationFactory;
	}

	public Object getConfigBean() {
		return configBean;
	}

	public void setConfigBean(Object configBean) {
		this.configBean = configBean;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public long getConfigTime() {
		return configTime;
	}

	public void setConfigTime(long configTime) {
		this.configTime = configTime;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Configuration))
			return false;
		Configuration c = (Configuration) obj;
		if (this.getGroupId() != null && this.getConfigId() != null) {
			return this.getGroupId().equals(c.getGroupId()) && this.getConfigId().equals(c.getConfigId());
		} else if (this.getGroupId() == null && this.getConfigId() != null) {
			if (c.getGroupId() != null)
				return false;
			return this.getConfigId().equals(c.getConfigId());
		} else if (this.getGroupId() != null && this.getConfigId() == null) {
			if (c.getGroupId() == null)
				return false;
			return c.getConfigId() == null;
		} else {
			return c.getGroupId() == null && c.getConfigId() == null;
		}
	}

}
