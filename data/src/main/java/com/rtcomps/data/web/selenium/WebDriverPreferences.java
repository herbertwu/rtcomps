package com.rtcomps.data.web.selenium;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.google.common.annotations.VisibleForTesting;
import com.rtcomps.data.ActionsConfig;
import com.rtcomps.data.web.def.WebBrowser;
import com.rtcomps.data.web.selenium.chrome.ChromeWebDriverBuilder;


public class WebDriverPreferences {
	protected static final Logger consoleLog = Logger.getLogger(WebDriverPreferences.class);
	
	// See http://www.webmaster-toolkit.com/mime-types.shtml for more details
    public static final String DOWNLOAD_MIME_TYPES = "text/comma-separated-values, text/plain, text/csv, " +
    		"application/octet-stream, application/pdf, application/zip, application/csv, application/excel, " +
    		"image/png, image/gif, image/jpg";
    public static final int DEFAULT_DOWNLOAD_TIMEOUT_SECS = 900; // 15m
    public static final int DEFAULT_SERVER_TIMEOUT_SECS = 900; // 15m
    public static final int SERVER_TIMEOUT_ADDON_SECS = 60; // 15m
    public static final String SERVER_TIMEOUT_KEY = "http.server.response.timeout.secs";
    public static final Integer FAIL = -1; 
    public static final Integer DEFAULT_PAGE_WAIT_SECONDS = 30; 
    public static final Integer MAX_PAGE_WAIT_SECONDS = 3600; 
    public static final Integer DEFAULT_PAGE_POLL_SECONDS = 1; 
    public static final long IMPLICIT_PAGE_WAIT_SECONDS = 0; 
	
    public static final int MAX_SCREEN_WIDTH = 1080;
    public static final int MAX_SCREEN_HEIGHT = 786;
    
	//settings 
	private final String browserName;
	private Integer pageLoadWaitInSecs = DEFAULT_PAGE_WAIT_SECONDS;
	private Integer serverTimeoutInSecs;
	private boolean isMaxSized = true;
	private String proxy;
	private String downloadFolderBase;
	private String downloadFolder;
	
	
	
	private String initTmpDownloadBaseFolder() {
		String tmpDir="webdownload";
		String workDir=System.getProperty("java.io.tmpdir");
		return (workDir.endsWith("/") || workDir.endsWith("\\"))? workDir + tmpDir : workDir +"/" + tmpDir;
	}
	
	private int takeValidPageWaitSecs(Integer pageWaitInSecs) {
		return (pageWaitInSecs != null && pageWaitInSecs > 0 && pageWaitInSecs <= MAX_PAGE_WAIT_SECONDS)?  pageWaitInSecs : DEFAULT_PAGE_WAIT_SECONDS;
	}
	
	public WebDriverPreferences(String browserName) {
		this.browserName = browserName;
		initOtherParams();
	}
	
	public WebDriverPreferences(String browserName, Integer pageLoadWaitInSecs, String proxy) {
		this.browserName = browserName;
		this.pageLoadWaitInSecs = takeValidPageWaitSecs(pageLoadWaitInSecs);
		this.proxy = proxy;
		initOtherParams();
	}
	
	public WebDriverPreferences(String browserName, Integer pageLoadWaitInSecs, String proxy, boolean isMaxSized) {
		this.browserName = browserName;
		this.pageLoadWaitInSecs = takeValidPageWaitSecs(pageLoadWaitInSecs);
		this.proxy = proxy;
		this.isMaxSized = isMaxSized;
		initOtherParams();
	}
	
	private void initOtherParams() {
		int cfgTimeoutVal = ActionsConfig.getIntOrDefault(SERVER_TIMEOUT_KEY,DEFAULT_SERVER_TIMEOUT_SECS);
		int minTimeout = this.pageLoadWaitInSecs + SERVER_TIMEOUT_ADDON_SECS;
		serverTimeoutInSecs = (cfgTimeoutVal > minTimeout)? cfgTimeoutVal : minTimeout;
		downloadFolderBase = initTmpDownloadBaseFolder();
		downloadFolder = downloadFolderBase + "/" + System.currentTimeMillis();
		consoleLog.info(String.format("serverTimeoutInSecs=%d(minTimeout=%ds,cfgTimeoutVal=%ds)",serverTimeoutInSecs,minTimeout,cfgTimeoutVal));
	}

	public Integer getPageLoadWaitinSecs() {
		return pageLoadWaitInSecs;
	}
	
	public Integer getServerTimeoutInSecs() {
		return serverTimeoutInSecs;
	}

	public void setPageLoadWaitInSecs(Integer pageLoadWaitInSecs) {
		this.pageLoadWaitInSecs = takeValidPageWaitSecs(pageLoadWaitInSecs);
	}

	public String getDownloadFolder() {
		return downloadFolder;
	}
	
	public WebDriver buildWebDriver() {
		WebDriverBuilder builder = createWebDriverBuilder();
		return builder.build();
	}
	
	@VisibleForTesting
	WebDriverBuilder createWebDriverBuilder() {
		if (StringUtils.isEmpty(browserName)||browserName.trim().equalsIgnoreCase(WebBrowser.CHROME)) {
			return new ChromeWebDriverBuilder(proxy, isMaxSized, downloadFolder);
		} else if (browserName.trim().equalsIgnoreCase(WebBrowser.FIREFOX)) {
			throw new RuntimeException("Firefox browser not supproted!");
			//TODO return new FirefoxWebDriverBuilder(proxyType, isMaxSized, downloadFolder);
		} else if (browserName.trim().equalsIgnoreCase(WebBrowser.IE)) {
			throw new RuntimeException("IE browser not supproted!");
		    //TODO return new IEWebDriverBuilder(bproxyType, isMaxSized, downloadFolder);
		}
		throw new RuntimeException("Browser " + browserName + " not supproted!");
	}
}
