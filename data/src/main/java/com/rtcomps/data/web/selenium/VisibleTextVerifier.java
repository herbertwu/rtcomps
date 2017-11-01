package com.rtcomps.data.web.selenium;

import static java.util.Arrays.asList;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Optional;
import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;
import com.rtcomps.data.web.def.Element;

public class VisibleTextVerifier {
	private WebDriver driver;
	private int pageWaitSecs;
	private int pollSecs;
	
	public VisibleTextVerifier(WebDriver driver,
			int pageWaitSecs, int pollSecs) {
		this.driver = driver;
		this.pageWaitSecs = pageWaitSecs;
		this.pollSecs = pollSecs;
	}

	public boolean hasVisibleText(String text) {
		verifyVisibleTextInput(text);
		By locator = getElementExistsLocator(text);
		Optional<Element> elem = ElementFinderUtil.findElementWithWait(locator, driver, pageWaitSecs,pollSecs);
		return elem.isPresent();
	}
	
	private void verifyVisibleTextInput(String text) {
		if (StringUtils.isBlank(text)) {
			throw new BrowserException(BrowserErrorCode.VERIFY_BLANK_TEXT,"Must provide non-blank text for visible text verification.");
		}
	}
	
	private By getElementExistsLocator(String text) {
		By elementExistsFinder = new CompositeLocator(asList(
				new VisibleTextElementExistsAsLeafNodeFinder(text),
				new VisibleTextElementExistsAsGeneralNodeFinder(text)
		));

		return FinderFactory.findWithFrames(driver, elementExistsFinder);
	}
}
