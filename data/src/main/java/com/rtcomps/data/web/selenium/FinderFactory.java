package com.rtcomps.data.web.selenium;

import static java.util.Arrays.asList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rtcomps.data.web.def.Attributes;

public class FinderFactory {

	public static By getFinder(WebDriver driver, Attributes attrs) {
		return FinderFactoryForGeneric.getFinder(driver,attrs);
	}
	
	public static By getFinderWithFrames(WebDriver driver, Attributes attrs) {
		return findWithFrames(driver, getFinder(driver, attrs));
	}
	
	public static By findWithFrames(WebDriver driver, By finder) {
		return new CompositeLocator(asList(
				new FindInCurrentWindowFinder(driver, finder),
				new FindInFirstIFrameFinder(driver, finder),
				new FindInAnyIFrameFinder(driver, finder)
		));
	}
}
