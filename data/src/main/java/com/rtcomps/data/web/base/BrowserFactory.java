package com.rtcomps.data.web.base;

import com.rtcomps.data.web.def.BrowserSettings;
import com.rtcomps.data.web.def.WebBrowser;
import com.rtcomps.data.web.selenium.WebDriverBrowser;


public class BrowserFactory {
	
	public static WebBrowser createBrowser(String browserName, BrowserSettings settings) {
		return new WebDriverBrowser(browserName,settings.getPageLoadWaitInSecs(),settings.getProxy(), settings.isMaxSized());
	}
	
	public static WebBrowser createBrowser(String browserName) {
		return createBrowser(browserName,null);
	}
	
}
