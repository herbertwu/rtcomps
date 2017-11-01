package com.rtcomps.data.web.selenium;

import static com.rtcomps.data.web.selenium.ElementFinderUtil.locatorToString;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class QueryFinder extends By {

	private final SearchContextFinder searchContextFinder;
	private final By locator;

	public QueryFinder(SearchContextFinder searchContextFinder, By locator) {
		this.searchContextFinder = searchContextFinder;
		this.locator = locator;
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		for (SearchContext domain : searchContextFinder.findSearchContexts()) {
			try {
				return locator.findElement(domain);
			} catch (NoSuchElementException e) {
				// try next query domain
			}
		}
		throw new NoSuchElementException("Can not find the elment - " + locatorToString(locator));
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

	public static interface SearchContextFinder {

		// provide a list of SearchContexts within which to locate the element.
		// this is called each time a find is attempted to get a fresh list
		// (since the elements on the page eligible to be searched may
		// change with time).
		List<? extends SearchContext> findSearchContexts();
	}

	@Override
	public String toString() {
		return "QueryFinder:" + locatorToString(locator);
	}
}
