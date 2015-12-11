package org.daochong.ucm;

import java.util.List;

public class DefaultConfigurationFactory implements ConfigurationFactory {

	private ConfigurationContainer configurationContainer;
	private List<Configuration> list;
	private Configuration configuration;
	private long refereshTime;

	public long getRefereshTime() {
		return refereshTime;
	}

	public void setRefereshTime(long refereshTime) {
		this.refereshTime = refereshTime;
	}

	public List<Configuration> getList() {
		return list;
	}

	public void setList(List<Configuration> list) {
		this.list = list;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public ConfigurationContainer getConfigurationContainer() {
		return configurationContainer;
	}

	public void setConfigurationContainer(ConfigurationContainer configurationContainer) {
		this.configurationContainer = configurationContainer;
	}

	protected void updateConfiguration(Configuration config) {
		if (this.getConfigurationContainer() instanceof DefaultConfigurationContainer) {
			DefaultConfigurationContainer container = (DefaultConfigurationContainer) this.getConfigurationContainer();
			container.updateConfiguration(this, config);
		}
	}

	public void load() {
		this.refereshTime = System.currentTimeMillis();
		if (this.configuration != null) {
			updateConfiguration(this.configuration);
		}
		if (this.list != null) {
			for (Configuration config : this.list) {
				updateConfiguration(config);
			}
		}
	}

}
