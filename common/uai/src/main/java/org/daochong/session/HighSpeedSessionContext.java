package org.daochong.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class HighSpeedSessionContext implements SessionContext{
	private Timer timer;
	
	private Map<String,Session> sessions = new HashMap<String, Session>();
	
	private int maxInactiveInterval = 86400;
	
	public HighSpeedSessionContext(){
		timer = new Timer();
		CleanSession cs = new CleanSession();
		cs.context = this;
		timer.schedule(cs, 10000);
	}
	
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public Session getSession(String sessionId) {
		if(sessions.containsKey(sessionId)){
			return sessions.get(sessionId);
		}
		HighSpeedSession session = new HighSpeedSession();
		session.setCreateTime(System.currentTimeMillis());
		session.setMaxInactiveInterval(getMaxInactiveInterval());
		session.setLastAccessedTime(session.getCreateTime());
		session.setSessionContext(this);
		session.setSessionId(sessionId);
		sessions.put(sessionId, session);
		return session;
	}

	public Set<String> getSessionKeys() {
		return sessions.keySet();
	}

	public void close() {
		List<Session> list = new ArrayList<Session>();
		for(Session session:sessions.values()){
			list.add(session);
		}
		for(Session session:list){
			session.invalidate();
		}
	}

	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	public Object getSessionAttribute(String sessionId, String id) {
		return getSession(sessionId).getAtrribute(id);
	}

	public void setSessionAttribute(String sessionId, String id, Object o) {
		getSession(sessionId).setAttribute(id, o);
	}

	public long getSessionIdle(String sessionId) {
		Session session = getSession(sessionId);
		if(session.getMaxInactiveInterval()>0){
			long re = session.getMaxInactiveInterval()-(System.currentTimeMillis()-session.getLastAccessedTime());
			if(re<=0){
				return 0;
			}else{
				return re;
			}
		}else{
			return -1;
		}
	}
	@Override
	protected void finalize() throws Throwable {
		if(this.timer!=null){
			this.timer.cancel();
		}
		super.finalize();
	}
	class CleanSession extends TimerTask{
		private HighSpeedSessionContext context;
		@Override
		public void run() {
			if(this.context!=null){
				List<String> timeout = new ArrayList<String>();
				for(String id:this.context.sessions.keySet()){
					if(this.context.getSessionIdle(id)==0){
						timeout.add(id);
					}
				}
				for(String id:timeout){
					this.context.removeSession(id);
				}
			}
		}
		
	}
	public Session getDefaultSession() {
		return getSession(this.getClass().getName());
	}
}
