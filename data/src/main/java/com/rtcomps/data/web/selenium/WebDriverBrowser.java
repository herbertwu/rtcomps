package com.rtcomps.data.web.selenium;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.rtcomps.data.ActionsConfig;
import com.rtcomps.data.FileStorageService;
import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;
import com.rtcomps.data.web.def.BrowserTools;
import com.rtcomps.data.web.def.Element;
import com.rtcomps.data.web.def.ScreenOptions;
import com.rtcomps.data.web.def.Screenshot;
import com.rtcomps.data.web.def.WebBrowser;
import com.rtcomps.data.web.selenium.WebDriverTools.ToolsOptions;


public class WebDriverBrowser implements WebBrowser {

	private static final Logger log = Logger.getLogger(WebDriverBrowser.class);
	private static final String lookUpTimeHeader = "_browserElementLookupTime>";
	
	/**
	 * In linux env, webdriver.quit() call may interference another webdriver open call in progress.
	 * This lock prevent open() and quit() call being made at same time.
	 */
	private static final ReentrantReadWriteLock openCloseLock = new ReentrantReadWriteLock();
	
	private WebDriverPreferences preferences;
	private WebDriver driver;
	
	private WindowControl windowControl;
	private BrowserTools browserTools;
	
	public WebDriverBrowser(String browserName) {
		this.preferences = new WebDriverPreferences(browserName);
	}
	
	public WebDriverBrowser(String browserName, Integer pageLoadWaitInSecs, String proxy) {
		this.preferences = new WebDriverPreferences(browserName, pageLoadWaitInSecs, proxy);
		
	}
	
	public WebDriverBrowser(String browserName, Integer pageLoadWaitInSecs, String proxy, boolean isMaxSized) {
		this.preferences = createDriverPreferences(browserName, pageLoadWaitInSecs, proxy, isMaxSized);
	}
	
	@VisibleForTesting
	WebDriverPreferences createDriverPreferences(String browserName, Integer pageLoadWaitInSecs, String proxy, boolean isMaxSized) {
		return new WebDriverPreferences(browserName, pageLoadWaitInSecs, proxy, isMaxSized);
	}
	
	private void initDriver() {
		driver = preferences.buildWebDriver();
		createAddOns();
	}
	
	
	private void cleanUpTmpFilesIfAny() {
		try {
			if ( browserTools != null ) {
				browserTools.removeDownloadFolder();
			}
		} catch (IOException ioe) {
			log.error("Failed to cleanup driver tmp download folder="+preferences.getDownloadFolder(), ioe);
			throw new RuntimeException(ioe);
		}
	}

	public WebDriverPreferences getWebDriverPreferences() {
		return preferences;
	}

	@Override
	public void open(String url) {
		open(url,null);
	}
	
	@Override
	public void open(String url, Integer newPageWaitSecs) {
		close();
		overridePageWaitSecondsIfAny(newPageWaitSecs);
		createNewWebDriverWithOpenCloseLock(url);
	}
	
	private void createAddOns() {
		windowControl = getWindowControl();
		browserTools = initTooling();
	}
	
	private void createNewWebDriverWithOpenCloseLock(String url) {
		openCloseLock.readLock().lock();
		try {
			/**
			 * Time on this call increases dramatically when number of active firefox increases on the same machine. 
			 * Hence it may block close() call of other threads - you may notice that first open browser may not 
			 * close until last browser is opened. See Selenium APIs for further details.
			 */
			initDriver();
		} catch (Exception e ) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			openCloseLock.readLock().unlock();
		}
		try {
			driver.get(url);
			log.info("new webdriver opened on url="+url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void overridePageWaitSecondsIfAny(Integer newPageWaitSeconds) {
		if ((newPageWaitSeconds != null)) {
			preferences.setPageLoadWaitInSecs(newPageWaitSeconds);
		}
	}
	
	@VisibleForTesting
	WindowControl getWindowControl() {
		return new WindowControl(this.driver,preferences.getPageLoadWaitinSecs());
	}
	
	@VisibleForTesting
	BrowserTools initTooling() {
		int downloadTimeoutSecs = ActionsConfig.getIntOrDefault("download.timeout.secs",
				WebDriverPreferences.DEFAULT_DOWNLOAD_TIMEOUT_SECS);

		ToolsOptions options = new ToolsOptions(new File(preferences.getDownloadFolder()), preferences.getPageLoadWaitinSecs(),
				downloadTimeoutSecs);
		return new WebDriverTools(driver, options);
	}
	
	@Override
	public void close() {
		cleanUpTmpFilesIfAny();
		openCloseLock.writeLock().lock();
		try {
			if (driver != null) {
				driver.quit();
				log.info("webdriver browser closed.");
			}
		} catch (Exception e ) {
			log.error("Failed to close browser: ",e);
		} finally {
			driver = null;
			openCloseLock.writeLock().unlock();
		}
	}
	
	@Override
	public boolean verifyTitle(String expectedTitle) {
		return driver.getTitle().indexOf(expectedTitle) >=0;
	}
	
	@Override
	public String getTitle() {
		return driver.getTitle();
	}
	
	@Override
	public String getSource() {
		return driver.getPageSource();
	}
	
	@Override
	public void verifyPageError(int errType) throws BrowserException {
		PageErrorVerifier pageErrorVerifier = createPageErrorVerifier();
		pageErrorVerifier.verify(errType);
	}
	
	@VisibleForTesting
	PageErrorVerifier createPageErrorVerifier() {
		return new PageErrorVerifier(windowControl.getCurrentWindow());
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	private SearchContext getSearchContext(WebDriver driver, Attributes attrs) {
		if (attrs.getContextElement() != null) {
			return ((WebDriverElement) attrs.getContextElement()).getWebElement();
		} else {
			return driver;
		}
	}

	@Override
	public Element findElement(Attributes attrs) {
		long t1 = System.currentTimeMillis();
		IFrameSwitcher.switchOutIframe(driver);
		By locator = FinderFactory.getFinderWithFrames(driver, attrs);
		SearchContext ctx = getSearchContext(driver, attrs);
		Optional<Element> elem = ElementFinderUtil.findElementWithWait(locator, ctx, getPageLoadWaitinSecs(),
				WebDriverPreferences.DEFAULT_PAGE_POLL_SECONDS);
		log.info(lookUpTimeHeader + " ms=" +(System.currentTimeMillis()-t1) +", attrs="+attrs);
		
		if (!elem.isPresent()) {
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_FOUND,"Can not find element by atttributes: " + attrs);
		}
		return ElementFactory.createDecorElement(elem.get(),attrs);
	}
	
	@Override
	public void hoverMouse(Attributes attrs) {
		Element mouseOver = findElement(attrs);
		
		WebElement hoverOverMenuElem = ((WebDriverElement)mouseOver).getWebElement();
		Actions mouseActions = getMouseActions();
		mouseActions.moveToElement(hoverOverMenuElem).build().perform();

	}
	
	@Override
	public void doubleClick(Attributes attrs) {
		Element elementToDoubleClick = findElement(attrs);
		
		if (elementIsClickable(elementToDoubleClick)) {
			WebElement element = ((WebDriverElement)elementToDoubleClick).getWebElement();
			Actions mouseActions = getMouseActions();
			mouseActions.moveToElement(element).doubleClick().build().perform();
		} 
	}
	
	@VisibleForTesting
	Actions getMouseActions() {
		return new Actions(driver);
	}
	
	public int getPageLoadWaitinSecs() {
		return preferences.getPageLoadWaitinSecs();
	}
	
	@Override
	public void switchToPopupWindow() {
		if ( windowControl.switchToPopupWindow() == WebDriverPreferences.FAIL) {
			throw new BrowserException(BrowserErrorCode.POPUP_NOT_FOUND,"No popup window found.");
		}
	}
	
	@Override
	public void switchToParentWindow() {
		if (windowControl.switchToParentWindow() == WebDriverPreferences.FAIL) {
			throw new BrowserException(BrowserErrorCode.POPUP_NOT_CLOESED, "Popup window can not be closed.");
		}
	}
	
	@Override
	public boolean hasVisibleText(String text) {
		VisibleTextVerifier textVerifier = new VisibleTextVerifier(driver, getPageLoadWaitinSecs(), WebDriverPreferences.DEFAULT_PAGE_POLL_SECONDS);
		return textVerifier.hasVisibleText(text);
	}
	
	@Override
	public Screenshot takeScreenshot(ScreenOptions options) {
		try {
			return browserTools.takeScreenshot(options);
		} catch (Exception e) {
			log.error("_takeScreenshot failed: ", e);
			throw new BrowserException(BrowserErrorCode.SCREENSHOT_FAILED,"Failed to take screenshot: screenOptions="+options, e);
		}
	}
	
	@Override
	public void saveDownloadFile(String fileName,FileStorageService fileStorageService) {
		try {
			browserTools.saveDownloadFile(fileName, fileStorageService);
		} catch (Exception e) {
			log.error("_saveDownloadFile failed: ", e);
			throw new BrowserException(BrowserErrorCode.DOWNLOAD_FILE_FAILED,"Download file failed: fileName="+fileName, e);
		}
		
	}
	
	@Override
	public void cleanDownloadFolder() {
		try {
			browserTools.cleanDownloadFolder();
		} catch (Exception e) {
			log.error("_cleanDownloadFolder failed: ", e);
			throw new BrowserException(BrowserErrorCode.DOWNLOAD_FILE_FAILED,"Failed to clean download folder: folder="+preferences.getDownloadFolder(), e);
		}
	}

	@Override
	public boolean elementIsClickable(Element element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, preferences.getPageLoadWaitinSecs());
			WebElement webElem = ((WebDriverElement) element).getWebElement();
			wait.until(ExpectedConditions.elementToBeClickable(webElem));
			return true;
		} catch (TimeoutException e) {
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_CLICKABLE, "Element is not clickable: element=" + element, e);
		}
	}
	
	@Override
	public int getServerResponseTimeoutSecs() {
		return preferences.getServerTimeoutInSecs();
	}

	@Override
	public Object executeScript(String jsScript) {
		JavascriptExecutor jsEx =(JavascriptExecutor)driver;
        return jsEx.executeScript(jsScript);
	}
}
