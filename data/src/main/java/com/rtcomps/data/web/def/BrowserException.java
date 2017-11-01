package com.rtcomps.data.web.def;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class BrowserException extends RuntimeException {
	private static final long serialVersionUID = -8596262266898703553L;
	
	public static final String SYSTEM_ERR_MSGS = "Sorry, Web UI action failed.";
	
	private BrowserErrorCode errCode;
	
	//TODO move message mapping to properties file later
	private static Map<BrowserErrorCode, String> userFriendlyErrMsgs = ImmutableMap.<BrowserErrorCode, String>builder()
			.put(BrowserErrorCode.ELEM_NOT_FOUND, "Page element not found.") 
			.put(BrowserErrorCode.INVALID_URL, "Page not found - invalid URL or system down.")
			.put(BrowserErrorCode.POPUP_NOT_FOUND, "Can not find any popup window.")
			.put(BrowserErrorCode.POPUP_NOT_CLOESED, "Can not close the popup window.")
			.put(BrowserErrorCode.SCREENSHOT_FAILED, "Failed to take a screenshot.")
			.put(BrowserErrorCode.BROWSER_NOT_FOUND, "Browser not found.")
			.put(BrowserErrorCode.VISIBLE_TEXT_NOT_FOUND, "Visible text not found.")
			.put(BrowserErrorCode.CLOSE_BROWSER_FAILED, "Failed to close browser.")
			.put(BrowserErrorCode.DOWNLOAD_FILE_FAILED, "Failed to download a file.")
			.put(BrowserErrorCode.VERIFY_BLANK_TEXT, "Can not verify blank text.")
			.put(BrowserErrorCode.PAGE_REFRESH_RETRY_TIMEOUT, "Error - page refresh wait is timed out.")
			.put(BrowserErrorCode.ELEM_NOT_CLICKABLE, "Page element not clickable.")
			.put(BrowserErrorCode.WITHIN_ELEM_NOT_FOUND, "Within element not found.")
			.put(BrowserErrorCode.CREATE_BROWSER_FAILED, "Failed to start browser.")
			.put(BrowserErrorCode.EXPECTED_VALUE_DID_NOT_MATCH_ACTUAL, "Expected value did not match with actual value.")
			.put(BrowserErrorCode.SERVER_ERR_NOT_FOUND, "Error: Server not found.")
			.put(BrowserErrorCode.SERVER_ERR_PAGE_NOT_FOUND, "Error: Page not found.")
			.put(BrowserErrorCode.SERVER_ERR_INTERNAL, "Sorry, server internal error. Try again later... ")
			.put(BrowserErrorCode.SERVER_ERR_TIMEOUT, "Error: No server response - connection timed out.")
			.put(BrowserErrorCode.INVALID_PAGETYPE, "Page Type Error. ")
			.put(BrowserErrorCode.ELEM_NOT_EDITABLE, "Page element not editable.")
			.build();
	
	public BrowserException(BrowserErrorCode errCode, Throwable cause) {
		super(cause);
		this.errCode = errCode;
	}
	
	public BrowserException(BrowserErrorCode errCode, String message) {
		super(message);
		this.errCode = errCode;
	}
	
	public BrowserException(BrowserErrorCode errCode,String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}

	public BrowserErrorCode getErrCode() {
		return errCode;
	}
	
	public String getUserFriendlyErrMsgs() {
		return userFriendlyErrMsgs.get(errCode) + " Details: " + getMessage();
	}
}
