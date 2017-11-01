package com.rtcomps.data.web.selenium.finders;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class LinkElmByCssClassName201403ImplLookUp extends By {

	private final String cssClass;

	public LinkElmByCssClassName201403ImplLookUp(String cssClass) {
		this.cssClass = cssClass;
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
		return By.xpath("//a[contains(@class,'" + cssClass + "')]");
	}

}
