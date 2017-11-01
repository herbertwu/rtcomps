package com.rtcomps.data.web.def;

import java.io.File;

import com.rtcomps.data.FileStorageService;

public interface WebBrowser {
	public static final String CHROME="chrome";
	public static final String IE="ie";
	public static final String FIREFOX="firefox";
	
	void open(String uri);
	void open(String uri, Integer pageLoadingWaitInSecs);
	String getTitle();
	boolean verifyTitle(String expectedTitle);
	String getSource();
	void verifyPageError(int errType) throws BrowserException;
	void close();
	Element findElement(Attributes attrs);
	void hoverMouse(Attributes attrs);
	void switchToPopupWindow();
	void switchToParentWindow();
	boolean hasVisibleText(String text);
	Screenshot takeScreenshot(ScreenOptions options);
	void saveDownloadFile(String fileName, FileStorageService fileStorage);
	void cleanDownloadFolder();
	boolean elementIsClickable(Element element);
	int getServerResponseTimeoutSecs();
	void doubleClick(Attributes attrs);
	Object executeScript(String jsScript);
}
