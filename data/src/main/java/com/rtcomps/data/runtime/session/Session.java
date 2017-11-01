package com.rtcomps.data.runtime.session;

public interface Session {
	
    public void setProperty(Object key, Object prop);
	
	public Object getProperty(Object key) ;
	
	public String getSessionId() ;
	
	public boolean isExpired() ;
	
	public void close() ;

}
