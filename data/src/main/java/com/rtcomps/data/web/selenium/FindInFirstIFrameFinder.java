package com.rtcomps.data.web.selenium;

import static com.rtcomps.data.web.selenium.IFrameSwitcher.switchOutIframe;
import static com.rtcomps.data.web.selenium.IFrameSwitcher.switchToFirstIframeIfAny;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rtcomps.data.web.def.BrowserException;

public class FindInFirstIFrameFinder extends By {

	private final WebDriver driver;
	private final By finder;

	public FindInFirstIFrameFinder(WebDriver driver, By finder) {
		this.driver = driver;
		this.finder = finder;
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		try {
			switchToFirstIframeIfAny(driver);
			return finder.findElement(ctx);
		} catch (StaleElementReferenceException e) {
			switchOutIframe(driver);
			throw new NoSuchElementException("wrong iframe", e);
		} catch (NoSuchElementException e) {
			switchOutIframe(driver);
			throw e;
		} catch (BrowserException e) {
			switchOutIframe(driver);
			throw new NoSuchElementException("No iframe found", e);
		}
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

}