package com.rtcomps.data.web.selenium;

import static com.rtcomps.data.web.selenium.IFrameSwitcher.switchOutIframe;
import static com.rtcomps.data.web.selenium.IFrameSwitcher.switchToFrame;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FindInAnyIFrameFinder extends By {

	private final WebDriver driver;
	private final By finder;

	public FindInAnyIFrameFinder(WebDriver driver, By finder) {
		this.driver = driver;
		this.finder = finder;
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		for (List<Integer> framePath : findAllEncore1FramePaths()) {
			switchToFrame(driver, framePath);
			try {
				return finder.findElement(ctx);
			} catch (StaleElementReferenceException e) {
				// try next iframe
			} catch (NoSuchElementException e) {
				// try next iframe
			}
			switchOutIframe(driver);
		}
		throw new NoSuchElementException("Not found in any frame.");
	}

	private List<List<Integer>> findAllEncore1FramePaths() {
		switchOutIframe(driver);
		return ElementFinderUtil.findAllEncore1FramePaths(driver);
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

}