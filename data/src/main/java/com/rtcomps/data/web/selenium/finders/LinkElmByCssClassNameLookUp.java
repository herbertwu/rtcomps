package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class LinkElmByCssClassNameLookUp extends By {

	private final String cssClass;

	public LinkElmByCssClassNameLookUp(String cssClass) {
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
		return By.xpath("//div[@class='" + cssClass + "']/parent::a[1]");
	}

}
