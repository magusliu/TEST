package org.daochong.session;

import java.util.Set;

public class RedisSession implements Session {

	private String sessionId;
	private long createTime;
	private long lastAccessedTime;
	private int maxInactiveInterval;
	private SessionContext sessionContext;
	private boolean newSession;
	private Set<String> attributeNames;

	public RedisSession() {
		this.createTime = System.currentTimeMillis();
		this.lastAccessedTime = this.createTime;
	}

	public Object getAtrribute(String id) {
		return this.getSessionContext().getSessionAttribute(this.getSessionId(), id);
	}

	public long getCreateTime() {
		return createTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void invalidate() {
		this.getSessionContext().removeSession(this.getSessionId());
	}

	public boolean isNewSession() {
		return newSession;
	}

	public void setAttribute(String id, Object value) {
		this.getSessionContext().setSessionAttribute(this.getSessionId(), id, value);
	}

	protected void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	protected void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	protected void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	protected void setNewSession(boolean newSession) {
		this.newSession = newSession;
	}

	protected void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	protected void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Set<String> getAttributeNames() {
		return this.attributeNames;
	}

	protected void setAttributeNames(Set<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

}
