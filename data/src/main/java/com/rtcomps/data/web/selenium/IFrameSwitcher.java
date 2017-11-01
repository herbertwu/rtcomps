package com.rtcomps.data.web.selenium;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;

public class IFrameSwitcher {

	private static final Logger log = Logger.getLogger(IFrameSwitcher.class);
	private static final Integer DEFAULT_PAGE_WAIT_SECONDS = 1;

	public static void switchToIframeById(WebDriver driver, String frameId) {
		System.out.println("switchToIframeById:" + frameId);
		try {
			switchOutIframe(driver);
			if (StringUtils.isBlank(frameId)) {
				// default to first iframe if any
				List<WebElement> frames = ElementInspector.waitUntilElementsPresent(driver, By.tagName("iframe"),
						DEFAULT_PAGE_WAIT_SECONDS);
				if (frames.size() == 0) {
					return;
				}
				driver.switchTo().frame(frames.get(0));
			} else {
				driver.switchTo().frame(frameId);
			}
		} catch (Exception e) {
			String err = "Failed to find an iframe by frameId=" + frameId;
			log.error(err, e);
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_FOUND,err, e);
		}
	}

	public static void switchToIframeByIndex(WebDriver driver, int index) {
		driver.switchTo().frame(index);
	}

	public static void switchToFirstIframeIfAny(WebDriver driver) {
		switchToIframeById(driver, null);
	}

	public static void switchOutIframe(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	public static void switchToFrame(WebDriver driver, List<Integer> framePath) {
		switchOutIframe(driver);
		for (Integer frameNode : framePath) {
			driver.switchTo().frame(frameNode);
		}
	}

}
