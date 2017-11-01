package com.rtcomps.data.web.selenium;

import static com.rtcomps.data.web.selenium.IFrameSwitcher.switchOutIframe;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FindInCurrentWindowFinder extends By {

	private final WebDriver driver;
	private final By finder;

	public FindInCurrentWindowFinder(WebDriver driver, By finder) {
		this.driver = driver;
		this.finder = finder;
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		try {
			switchOutIframe(driver);
			return finder.findElement(ctx);
		} catch (StaleElementReferenceException e) {
			throw new NoSuchElementException("wrong iframe", e);
		}
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

}