package org.daochong.ucm;

import java.util.List;

public interface ConfigurationContainer {

	public List<String> groups();

	public List<String> groupKeys(String groupId);

	public Configuration getConfiguration();

	public Configuration getConfiguration(String beanId, String groupId);

	public Configuration getConfiguration(String beanId);

	public Configuration getConfigurationGroup(String groupId);

	public Configuration getConfiguration(Class<?> clazz);

	public Configuration getConfiguration(String groupId, Class<?> clazz);

	public void referesh();

	public void referesh(String groupId);

	public boolean isExpired(Configuration configuration);
	
}
