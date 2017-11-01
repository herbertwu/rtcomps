package com.rtcomps.data.web.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.annotations.VisibleForTesting;
import com.rtcomps.data.web.def.BrowserErrorCode;
import com.rtcomps.data.web.def.BrowserException;
import com.rtcomps.data.web.def.Element;


public class WebDriverElement implements Element {

	private static final Logger log = Logger.getLogger(WebDriverElement.class);
	private WebElement element;
	
	
	public WebDriverElement(WebElement element) {
		this.element = element;
	}

	@Override
	public void setValue(String value) {
		try {
			element.clear();
			element.sendKeys(value);
		} catch (StaleElementReferenceException se) {
			//let it go if Stale Exception to allow re-try at high level
			throw se;
		} catch (Exception e) {
			log.error("Failed to setValue in input text field, web element is not editable.", e);
			throw new BrowserException(BrowserErrorCode.ELEM_NOT_EDITABLE, "I was given a web element that is not editable, Sorry: couldn't do it");
		}
	}

	@Override
	public void click() {
		element.click();
	}

	public WebElement getWebElement() {
		return element;
	}

	@Override
	public boolean isSelected() {
		return element.isSelected();
	}
	
	@Override
	public void selectByVisibleText(String userInput) {
		Select select = getSelect();
		if (ElementInspector.isEmptySelectOnEmptyOptions(select, userInput)) {
			return;
		}
		String visibleText = ElementInspector.getVisibleText(select,userInput);
		select.selectByVisibleText((visibleText == null)? "" : visibleText);
	}
	
	@VisibleForTesting
	Select getSelect() {
		return new Select(element);
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return element.getAttribute(attributeName);
	}
	
	@Override
	public String getValue() {
		return element.getText();
	}
	
	@Override
	public String getTagName() {
		return element.getTagName();
	}
	
}
