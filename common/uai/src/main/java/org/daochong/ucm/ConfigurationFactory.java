package org.daochong.ucm;

public interface ConfigurationFactory {

	public ConfigurationContainer getConfigurationContainer();

	public void setConfigurationContainer(ConfigurationContainer container);

	public void load();
	
	public long getRefereshTime();
}
