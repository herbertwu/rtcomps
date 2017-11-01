package com.rtcomps.data.web.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class VisibleTextElementExistsAsGeneralNodeFinder extends By {

	private final String expectedText;

	public VisibleTextElementExistsAsGeneralNodeFinder(String text) {
		this.expectedText = text.trim();
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		String elmMatchingQry = "//*[contains(.,'" + expectedText + "')]";
		List<WebElement> allMatched = ctx.findElements(By.xpath(elmMatchingQry));
		if (allMatched.isEmpty()) {
			throw new NoSuchElementException("not found");
		} else {
			WebElement endNode = allMatched.get(allMatched.size() - 1);
			if (endNode.isDisplayed()) {
				return endNode;
			} else {
				throw new NoSuchElementException("not found");
			}
		}
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

}