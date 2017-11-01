package com.rtcomps.data.runtime.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rtcomps.data.base.ClassUtil;

public class ScriptRunSession implements Session, Serializable {
	private static final long serialVersionUID = -319292092713093299L;
	private static final String CLOSE_METHOD_NAME="close";

	private Map<Object, Object> props = new HashMap<Object, Object>();
	private final long expireTime;
	private final String sessionId;
	
	
	
	public ScriptRunSession(String sessionId, long expireTime) {
		this.sessionId = sessionId;
		this.expireTime = expireTime;
	}

	public void setProperty(Object key, Object prop) {
		props.put(key, prop);
	}
	
	public Object getProperty(Object key) {
		return props.get(key);
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public boolean isExpired() {
		if (expireTime <= 0 ) {
			return false; // never expire
		}
		return System.currentTimeMillis() > expireTime;
	}
	
	public void close() {
		for (Object value : props.values()) {
			runCloseMethodIfAny(value);
		}
	}
	
	private void runCloseMethodIfAny(Object obj) {
		ClassUtil.runNoArgMethodIfAny(obj, CLOSE_METHOD_NAME);
	}
}
