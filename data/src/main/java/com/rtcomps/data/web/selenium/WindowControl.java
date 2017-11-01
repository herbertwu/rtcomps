package com.rtcomps.data.web.selenium;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


// Assuming at most one popup window at any given time
public class WindowControl {
	public static final int ROOT_WINDOW=1;
	public static final int POPUP_WINDOW=2;
	public static final int FAIL=-1;
	
	private static final Logger log = Logger.getLogger(WindowControl.class);
	
	final private WebDriver driver;
	final private int maxWaitInSecs;
	
	private String parentWindowHandle;
	
	public WindowControl(WebDriver driver, int maxWaitInSecs) {
		this.driver = driver;
		this.maxWaitInSecs = maxWaitInSecs;
		//parentWindowHandle = driver.getWindowHandle();
	}
	
	public int switchToPopupWindow() {
		if (driver.getWindowHandles().size() == 1) {
			if (!waitForNumberOfWindowsToEqual(2)) {
				return FAIL;
			}
		} 
		return switchToSingleChildWindow();
	}
	
	public int switchToParentWindow() {
		if (driver.getWindowHandles().size() == 2) {
			if (!waitForNumberOfWindowsToEqual(1)) {
				return FAIL;
			}
		} 
		return switchToRootWindow();
	}
	
	public WebDriver getCurrentWindow() {
		return driver;
	}
	
	private int switchToSingleChildWindow() {
		 Set<String> handles = driver.getWindowHandles();
		 handles.remove(parentWindowHandle);
		 String popupHandle=handles.iterator().next();
		 driver.switchTo().window(popupHandle);
		 return POPUP_WINDOW;
	}
	
	private int switchToRootWindow() {
		 driver.switchTo().window(parentWindowHandle);
		 return ROOT_WINDOW;
	}
		
	private boolean waitForNumberOfWindowsToEqual(final int numberOfWindows) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxWaitInSecs);
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return (driver.getWindowHandles().size() == numberOfWindows);
				}
			});
			return true;
		} catch (TimeoutException e) {
			log.info("_seleniumSwitchWindowWithWait> No windows changed in timeout period: timeout=" + maxWaitInSecs + ", expectedNoOfWindows="  + numberOfWindows);
			return false;
		}
	 
    }
}
