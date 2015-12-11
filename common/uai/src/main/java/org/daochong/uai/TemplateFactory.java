package org.daochong.uai;

import org.daochong.ucm.Configuration;

public interface TemplateFactory<T> {

	public T build(Configuration config);

	public Class<T> getBeanClass();

	public Class<?> getInfoClass();

	public boolean isSingleton();
	
	public boolean isThreadLocal();
	
	public void destory(T obj);
}
