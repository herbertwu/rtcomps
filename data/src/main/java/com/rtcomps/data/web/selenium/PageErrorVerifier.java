package com.rtcomps.data.web.selenium;

import static com.rtcomps.data.web.def.PageResult.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.google.common.annotations.VisibleForTesting;
import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;
/**
 * Simply scrap visible page content for server errors as Selenium api 
 * has no direct access to http response.
 * 
 * If false alarm or false-positive is surfaced, consider other approaches.
 *
 */
public class PageErrorVerifier {
	
	private static final Logger log = Logger.getLogger(PageErrorVerifier.class);
    public static final String SERVER_NOT_FOUND_ERROR_TEXT = "Server not found";
    public static final String SERVER_PAGE_NOT_FOUND_ERROR_TEXT = "HTTP Status 404";
    public static final String SERVER_INTERNAL_ERROR_TEXT = "HTTP Status 500";
    public static final String SERVER_TIMEOUT_ERROR_TEXT = "connection has timed out";
    
	private WebDriver currentWindow;
	

	public PageErrorVerifier(WebDriver webDriver) {
		this.currentWindow = webDriver;
	}
	
	public void verify(int errType) {
		switch (errType) {
		case CHK_SERVER_ERR: verifyHttpResponseErr(); break;
		case CHK_SERVER_TIMEOUT_ERR: verifyConnectionTimeout();break;
		default :
		}
	}
	
	private void verifyHttpResponseErr() {
		(new HttpResponseErrorVerifier(createTextVerifier())).verify();
	}
	
	private void verifyConnectionTimeout() {
		(new ServerConnectionTimeoutVerifier(currentWindow,createTextVerifier())).verify();
	}
	
	@VisibleForTesting
	VisibleTextVerifier createTextVerifier() {
		 return new VisibleTextVerifier(currentWindow,0,0);
	}
	
	private static class HttpResponseErrorVerifier {
		private VisibleTextVerifier textVerifier;
		
		public HttpResponseErrorVerifier(VisibleTextVerifier textVerifier) {
			this.textVerifier = textVerifier;
		}

		public void verify() {
			long start = System.currentTimeMillis();
			if (textVerifier.hasVisibleText(SERVER_NOT_FOUND_ERROR_TEXT)) {
				throw new BrowserException(BrowserErrorCode.SERVER_ERR_NOT_FOUND,"Invalid server url or server is down.");
			} 
			
			if (textVerifier.hasVisibleText(SERVER_PAGE_NOT_FOUND_ERROR_TEXT)) {
				throw new BrowserException(BrowserErrorCode.SERVER_ERR_PAGE_NOT_FOUND,"Invalid page url or system error.");
			} 
				
			if (textVerifier.hasVisibleText(SERVER_INTERNAL_ERROR_TEXT)) {
				throw new BrowserException(BrowserErrorCode.SERVER_ERR_INTERNAL,"System error...check system log or contact tech support.");
			}
				
			long endTime = System.currentTimeMillis();
			log.info(String.format("verifyServerResponseErrorTime: %dms", (endTime - start)));			
		}
	}
	
	private static class ServerConnectionTimeoutVerifier {
		private WebDriver currentWindow;
		private VisibleTextVerifier textVerifier;
		
		public ServerConnectionTimeoutVerifier(WebDriver currentWindow,VisibleTextVerifier textVerifier) {
			this.currentWindow = currentWindow;
			this.textVerifier = textVerifier;
		}
		
		public void verify() {
			long start = System.currentTimeMillis();
			if (!currentWindowOpen()) {
				/**
				 * if current window in transition state, no need to verify(client issue only). 
				 * example: after click a button, popup window is closed and before the control 
				 * is switched back to parent window.
				 */
				return;
			}
				
			if (textVerifier.hasVisibleText(SERVER_TIMEOUT_ERROR_TEXT)) {
				throw new BrowserException(BrowserErrorCode.SERVER_ERR_TIMEOUT,"System slow or hanging...check system log or contact tech support.");
			}
			
			long endTime = System.currentTimeMillis();
			log.info(String.format("verifyConnectionTimeoutTime: %dms", (endTime - start)));			
		}
		
		private boolean currentWindowOpen() {
			try {
				currentWindow.getTitle();
				return true;
			} catch (Exception e) {
				log.warn("current window is stale", e);
				return false;
			}
		}
	}

}
