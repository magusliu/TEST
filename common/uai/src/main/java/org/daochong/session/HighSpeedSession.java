package org.daochong.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HighSpeedSession implements Session {
	private Map<String,Object> attributes;
	private String sessionId;
	private long createTime;
	private long lastAccessedTime;
	private int maxInactiveInterval;
	private HighSpeedSessionContext sessionContext;
	
	public HighSpeedSession(){
		attributes = new HashMap<String, Object>();
	}
	
	public String getSessionId() {
		this.lastAccessedTime = System.currentTimeMillis();
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	public void setSessionContext(HighSpeedSessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}

	public void invalidate() {
		this.attributes.clear();
		this.getSessionContext().removeSession(getSessionId());
	}

	public boolean isNewSession() {
		return true;
	}

	public Object getAtrribute(String id) {
		this.lastAccessedTime = System.currentTimeMillis();
		return this.attributes.get(id);
	}

	public void setAttribute(String id, Object value) {
		this.lastAccessedTime = System.currentTimeMillis();
		this.attributes.put(id, value);
	}

	public Set<String> getAttributeNames() {
		this.lastAccessedTime = System.currentTimeMillis();
		return this.attributes.keySet();
	}



}
