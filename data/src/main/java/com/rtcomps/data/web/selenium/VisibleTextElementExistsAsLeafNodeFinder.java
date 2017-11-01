package com.rtcomps.data.web.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class VisibleTextElementExistsAsLeafNodeFinder extends By {

	private final String expectedText;

	public VisibleTextElementExistsAsLeafNodeFinder(String text) {
		this.expectedText = text.trim();
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		String elmMatchingQry = "//*[contains(.,'" + expectedText + "') and not(*)]";
		List<WebElement> allMatched = ctx.findElements(By.xpath(elmMatchingQry));
		for (WebElement ele : allMatched) {
			if (ele.isDisplayed()) {
				return ele;
			}
		}
		throw new NoSuchElementException("not found");
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}
}
