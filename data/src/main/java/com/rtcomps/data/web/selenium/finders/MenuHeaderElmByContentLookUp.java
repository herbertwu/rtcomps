package com.rtcomps.data.web.selenium.finders;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.rtcomps.data.web.selenium.ElementInspector;

public class MenuHeaderElmByContentLookUp extends By {

	private final String label;

	public MenuHeaderElmByContentLookUp(String label) {
		this.label = label;
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		return ctx.findElement(createLocator());
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		return ctx.findElements(createLocator());
	}

	private By createLocator() {
		String trimmedLabel = ElementInspector.trimLabel(label);
		By locator = By.xpath(".//span[contains(" + ElementInspector.toLowerCaseXpathQuery(".") + ",'" + trimmedLabel.toLowerCase() + "')]/ancestor-or-self::li[1]");
		return locator;
	}
	
}
