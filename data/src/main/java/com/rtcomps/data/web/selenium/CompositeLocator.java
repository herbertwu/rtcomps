package com.rtcomps.data.web.selenium;

import static com.google.common.collect.Lists.newArrayList;
import static com.rtcomps.data.web.selenium.ElementFinderUtil.locatorToString;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class CompositeLocator extends By {

	private final Iterable<? extends By> locators;

	public CompositeLocator(Iterable<? extends By> locators) {
		this.locators = ImmutableList.copyOf(locators);
	}

	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		for (By locator : locators) {
			try {
				return locator.findElement(ctx);
			} catch (NoSuchElementException e) {
				// try next locator
			}
		}
		throw new NoSuchElementException("not found");
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		List<WebElement> found = newArrayList();
		for (By locator : locators) {
			found.addAll(locator.findElements(ctx));
		}
		return found;
	}

	@Override
	public String toString() {
		List<String> locatorStrings = newArrayList();
		for (By locator : locators) {
			locatorStrings.add(locatorToString(locator));
		}
		return "Composite Locator [" + Joiner.on(",  ").join(locatorStrings) + "]";
	}

}