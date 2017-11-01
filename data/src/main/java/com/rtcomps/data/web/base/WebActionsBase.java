package com.rtcomps.data.web.base;

import static com.rtcomps.data.web.def.PageResult.CHK_SERVER_ERR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.rtcomps.data.ContextSensitiveAction;
import com.rtcomps.data.def.ActionException;
import com.rtcomps.data.def.ActionRunResult;
import com.rtcomps.data.FileStorageService;
import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;
import com.rtcomps.data.web.def.BrowserSettings;
import com.rtcomps.data.web.def.Dropdown;
import com.rtcomps.data.web.def.Element;
import com.rtcomps.data.web.def.ElementType;
import com.rtcomps.data.web.def.PageResult;
import com.rtcomps.data.web.def.PageType;
import com.rtcomps.data.web.def.ScreenOptions;
import com.rtcomps.data.web.def.Screenshot;
import com.rtcomps.data.web.def.WebBrowser;

public abstract class  WebActionsBase extends ContextSensitiveAction  {
	protected static final Logger logger =  LoggerFactory.getLogger(WebActionsBase.class);
	public static final String BROWSER_SESSION_PROP_KEY = "_firefox";
	
	private static final int MAX_URL_LENGTH = 4000;
	private static final int MAX_CONTENT_LENGTH = 2000;
	private static final Integer DEFAULT_PAGE_LOAD_WAIT = 0;
	private static final String DEFAULT_PROXY_TYPE = "SYSTEM";
	
	private FileStorageService fileStorageService;

	@Override
	public abstract ActionRunResult execute();
	
	protected void openUrlInBrowser(String url,
					String browserName,  
					Integer pageWaitSecs,
					String proxy) throws ActionException {
		//boolean isBrowserMaxSized = Boolean.valueOf(ActionsConfig.getString("browser.maxsized"));
		boolean isBrowserMaxSized = true; //TODO  config test
		
		String urlError = verifyUrl(url);
		if (urlError != null ) {
			throw new BrowserException(BrowserErrorCode.INVALID_URL,urlError);
		}
		WebBrowser webBrowser = createBrowser(browserName,pageWaitSecs,proxy,isBrowserMaxSized);
		if ( webBrowser == null ) {
			throw new BrowserException(BrowserErrorCode.BROWSER_NOT_FOUND, "Browser not found: browserName=" + browserName);
		} else {
			storeBrowser(webBrowser);
		}
		webBrowser.open(url);
		logPage(webBrowser);
	}
	
	protected void closeBrowser() {
		WebBrowser webBrowser = getBrowser();
		if (webBrowser != null ) {
			try {
				webBrowser.close();
			} catch (Exception e) {
				throw new BrowserException(BrowserErrorCode.CLOSE_BROWSER_FAILED,e);
			}
		} else {
			logger.info("Close browser: No browser is active at this time.");
		}
	}
	
	public String createContextElement(String label, Element contextElem) {
		String withinElementName = getContextName(label);
		saveContextElement(withinElementName, contextElem);
		return withinElementName;
	}
	
	public String createContextObject(String label, Object object) {
		String varName = getContextName(label);
		saveContextObject(varName, object);
		return varName;
	}
	
	protected void saveContextElement(String contextName, Element context) {
		put(contextName,context);
	}
	
	protected void saveContextObject(String contextName, Object object) {
		put(contextName,object);
	}
	
	protected Element getContextElement(String contextName) {
		return (Element)get(contextName);
	}
	
	protected Object getContextObject(String contextName) {
		return get(contextName);
	}
	
	private void logPage(WebBrowser webBrowser) {
		logger.info("TITLE: " + webBrowser.getTitle());
		String source = webBrowser.getSource();
		logger.info("SOURCE: " + ((source == null || source.length()<=MAX_CONTENT_LENGTH)? source : source.substring(0,MAX_CONTENT_LENGTH) + "......"));
	}
	
	private String verifyUrl(String url) {
		if (url == null || url.isEmpty() ) {
			return "Error: Url is empty";
		}
		if ( url.length() > MAX_URL_LENGTH ) {
			return "Error: Url is too long... maxLimit=" + MAX_URL_LENGTH + ", actualSize=" + url.length();
		}
		
		return null;
	}
	
	protected void storeBrowser(WebBrowser newWebBrowser) {
		WebBrowser oldOne = (WebBrowser)get(BROWSER_SESSION_PROP_KEY);
		if (oldOne != null) {
			oldOne.close();
		}
		put(BROWSER_SESSION_PROP_KEY, newWebBrowser);
	}
	
	protected WebBrowser getBrowser() {
		return (WebBrowser)get(BROWSER_SESSION_PROP_KEY);
	}
	
	protected void put(String key, Object obj) {
		getSession().setProperty(key, obj);
	}
	
	protected Object get(String key) {
		return getSession().getProperty(key);
	}
	
	@VisibleForTesting
    String getContextName(String componentName) {
		return componentName+"_"+System.currentTimeMillis();
	}
	
	protected void addContextElementIfAny(Attributes attrs) {
		String contextElemName = attrs.getContextElementName();
		if (!StringUtils.isEmpty(contextElemName)) {
			Element contextElem = getContextElement(contextElemName);
			if ( contextElem == null ) {
				throw new BrowserException(BrowserErrorCode.WITHIN_ELEM_NOT_FOUND,"action failed: can not find contextElementName=" +contextElemName + ", for Attributes=" +attrs);
			}
			attrs.setContextElement(contextElem);
		}
	}
	
	protected void switchToPopupWindow() {
		getBrowser().switchToPopupWindow();
	}
	
	protected void switchToParentWindow() {
		getBrowser().switchToParentWindow();
	}
	
	protected void takeScreenshot() {
		takeScreenshot(null);
	}
	
	protected void takeScreenshot(Exception actionException) {
		try {
			ScreenOptions options = new ScreenOptions(getSession().getSessionId(), actionException, fileStorageService);
			Screenshot shot = getBrowser().takeScreenshot(options);
			logger.info("_seleniumScreenshotTaken: " + shot);
		} catch (BrowserException e) {
			logger.error("_seleniumScreenshotFailed: ", e);
		}
	}
	
	protected void clickElementWithVerification(Element element) {
		if (getBrowser().elementIsClickable(element)) {
			element.click();
		} else {
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_CLICKABLE, "Element is not clickable: element=" + element);
		}
	}
	
	protected ActionRunResult verifyAndComplete(PageResult pageResult) {
		verifyPageError(pageResult);
		logger.info(pageResult.getSuccessMsgs());
		return ActionRunResult.completed();
	}
	
	private void verifyPageError(PageResult pageResult) {
		switch (pageResult.getErrType()) {
			case CHK_SERVER_ERR : checkUrlOrServerErr();break;
			// future error type goes here
			default:
		}
	}
	
	private void checkUrlOrServerErr() {
		getBrowser().verifyPageError(CHK_SERVER_ERR);
	}
	
	protected ActionRunResult fail(Exception error) {
		takeScreenshot(error);
		logger.error("_sessionId="+getSession().getSessionId(),error);
		String userFriendlyErrMsgs = (error instanceof BrowserException)? ((BrowserException)error).getUserFriendlyErrMsgs() : BrowserException.SYSTEM_ERR_MSGS + "... Details:\r\n" + error;
		return ActionRunResult.failed(userFriendlyErrMsgs);

	}
	
	protected ActionRunResult fail(String userFriendlyErrorMsgs) {
		takeScreenshot();
		logger.error("_sessionId="+getSession().getSessionId() + " failed: " + userFriendlyErrorMsgs);
		return ActionRunResult.failed(userFriendlyErrorMsgs);

	}
	
	protected ActionRunResult clickAndWaitForDownload(Attributes attributes, String storageFileName) {
		try {
			//1. Remove any previous files if any.
			getBrowser().cleanDownloadFolder();
			//2. Start download to tmp folder. This step may hang and can be forced to stop manually.
			Element element = getBrowser().findElement(attributes) ;
			clickElementWithVerification(element);
			//3. Wait for download to complete and save the downloaded file from tmp to Minion storage within a given timeout period.
			getBrowser().saveDownloadFile(storageFileName,getFileStorageService());
			logger.info("Download success. Attributes=" + attributes);
			return ActionRunResult.completed();
		} catch (Exception e) {
			return fail(e);
		}		
	}
	
	protected String readDropdownValue(String id, String name, String label, PageType pageType, String contextElementName) {
		Attributes attributes = new Attributes(id, name, label, contextElementName, pageType, ElementType.select);
		attributes.verifySingleIdNameLabelValue();  
		addContextElementIfAny(attributes);
		Element dropdown = getBrowser().findElement(attributes) ;
		if (getBrowser().elementIsClickable(dropdown)) {
			String dropdownSelectedValue = ((Dropdown)dropdown).getSelectedLabel();
			logger.info("Read dropdown value success: Value=[" + dropdownSelectedValue + "], Attributes=" + attributes);
			return dropdownSelectedValue;
		} else {
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_CLICKABLE, "Dropdown is not clickable: element=" + dropdown);
		}
	}
	
	protected boolean isElementSelected(String id, String name, String label, PageType pageType, ElementType elementType, String contextElementName) {
		Attributes attributes = new Attributes(id, name, label, contextElementName, pageType, elementType);
		attributes.verifySingleIdNameLabelValue();  
		addContextElementIfAny(attributes);
		Element radioOrCheckbox = getBrowser().findElement(attributes) ;
		boolean isSelected = radioOrCheckbox.isSelected();
		logger.info("Radio or Checkbox isSelected=" + isSelected + ", Attributes=" + attributes);
		return isSelected;
	}
	
	protected String readInputTextFieldValue(String id, String name, String label, PageType pageType, String contextElementName) {
		Attributes attributes = new Attributes(id, name, label, contextElementName, pageType, ElementType.input);
		attributes.verifySingleIdNameLabelValue();  
		addContextElementIfAny(attributes);
		
		Element inputTextField = getBrowser().findElement(attributes) ;
		String elementValue = inputTextField.getAttributeValue("value");
		logger.info("Input Text Field value success: Value=[" + elementValue + "], Attributes=" + attributes);
		return elementValue;
	}
	
		
	
	@VisibleForTesting
	WebBrowser createBrowser(String browserName,Integer pageLoadWaitSecs,String proxy, boolean isMaxSized) {
		BrowserSettings settings = new BrowserSettings(pageLoadWaitSecs, proxy, isMaxSized);
		try {
			return BrowserFactory.createBrowser(browserName,settings);
		} catch (Exception e ) {
			throw new BrowserException(BrowserErrorCode.CREATE_BROWSER_FAILED,"Failed to create browser ", e);
		}
	}
	
	public void setFileStorageService(FileStorageService service) {
		this.fileStorageService = service;
	}

	public FileStorageService getFileStorageService() {
		return fileStorageService;
	}
	
}
