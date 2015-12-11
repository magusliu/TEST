package org.daochong.session;

import java.util.Set;

public interface Session {

	public String getSessionId();

	public long getCreateTime();

	public long getLastAccessedTime();

	public int getMaxInactiveInterval();

	public SessionContext getSessionContext();

	public void invalidate();

	public boolean isNewSession();

	public Object getAtrribute(String id);

	public void setAttribute(String id, Object value);

	public Set<String> getAttributeNames();
}
