package com.rtcomps.data.web.def;

/**
 * 
 * Add/Remove/Update settings here
 *
 */
public class BrowserSettings {
	
	private Integer pageLoadWaitInSecs;
	private String proxy;
	private boolean isMaxSized=true;
	
	public BrowserSettings(Integer pageLoadWaitInSecs, String proxy) {
		this.pageLoadWaitInSecs = pageLoadWaitInSecs;
		this.proxy = proxy;
	}

	public BrowserSettings(Integer pageLoadWaitInSecs, String proxy,
			boolean isMaxSized) {
		this.pageLoadWaitInSecs = pageLoadWaitInSecs;
		this.proxy = proxy;
		this.isMaxSized = isMaxSized;
	}


	public Integer getPageLoadWaitInSecs() {
		return pageLoadWaitInSecs;
	}

	public String getProxy() {
		return proxy;
	}

	public boolean isMaxSized() {
		return isMaxSized;
	}


	public static enum PROXY_TYPE {
		AUTODETECT, MANUAL, SYSTEM, DIRECT
	}
}
