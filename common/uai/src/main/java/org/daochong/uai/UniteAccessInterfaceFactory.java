package org.daochong.uai;

public interface UniteAccessInterfaceFactory {

	public <T> T getTemplate(Class<T> clazz);

	public <T> T getTemplate(String configId, Class<T> clazz);

	public <T> T getTemplate(String configId, String groupId, Class<T> clazz);
	
	public void close();
}
