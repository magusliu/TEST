package org.daochong.session;

import java.util.Set;

public interface SessionContext {

	public Session getSession(String sessionId);

	public int getMaxInactiveInterval();

	public Set<String> getSessionKeys();

	public void close();

	public Session getDefaultSession();

	public void removeSession(String sessionId);

	public Object getSessionAttribute(String sessionId, String id);

	public void setSessionAttribute(String sessionId, String id, Object o);
	
	public long getSessionIdle(String sessionId);
}
