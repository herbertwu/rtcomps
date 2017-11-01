package com.rtcomps.data.web.selenium;

import static java.util.Collections.emptyList;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * A locator that never finds anything.
 * 
 * This was created during refactoring to handle cases where the code was
 * allowing an empty list of ElementQuery's to be returned.
 */
public class NullLocator extends By {

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		return emptyList();
	}

}
