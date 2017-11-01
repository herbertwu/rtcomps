package com.rtcomps.data.web.selenium;


import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.base.MoreObjects.firstNonNull;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.def.ElementType;
import com.rtcomps.data.web.def.XPathCSSSelectorAttributes;
import com.rtcomps.data.web.selenium.QueryFinder.SearchContextFinder;
import com.rtcomps.data.web.selenium.finders.GenericCssClassFinderRepo;
import com.rtcomps.data.web.selenium.finders.GenericLabelFinderRepo;

public class FinderFactoryForGeneric {
	
	public static By getFinder(WebDriver driver, Attributes attrs) {
		switch (attrs.getValueType()) {
			case Attributes.GENERIC_ID :
				return By.id(attrs.getId());
			case Attributes.GENERIC_NAME :
				return By.name(attrs.getName());
			case Attributes.GENERIC_CSS_SELECTOR :
				return By.cssSelector(((XPathCSSSelectorAttributes) attrs).getCssSelector());
			case Attributes.GENERIC_LABEL :
				return createGenericLabelFinder(driver, attrs);
			case Attributes.GENERIC_CSS_CLASS :
				return createGenericCssClassFinder(driver, attrs);
			case XPathCSSSelectorAttributes.GENERIC_XPATH: 
				return By.xpath(((XPathCSSSelectorAttributes)attrs).getXpath());
			default :
				throw new IllegalStateException("Can not create locator - Invalid element attributes: " + attrs);
		}
				
	}

	private static By createGenericLabelFinder(final WebDriver driver, final Attributes attrs) {
		SearchContextFinder searchContextFinder;
		if (attrs.getContextElement() == null) {
			searchContextFinder = new StaticSearchContextFinder(Arrays.asList(driver));
		} else {
			final WebElement headerElement = ((WebDriverElement) attrs.getContextElement()).getWebElement();
			searchContextFinder = new SearchContextFinder() {

				@Override
				public List<? extends SearchContext> findSearchContexts() {
					List<SearchContext> contexts = newArrayList();
					contexts.add(headerElement);

					List<WebElement> siblings = ElementInspector.getMenuSectionElements(headerElement);
					for (WebElement siblingElem : siblings) {
						contexts.add(siblingElem);
					}
					return contexts;
				}
			};
		}

		By locator = createGenericLabelLocator(attrs);

		return new QueryFinder(searchContextFinder, locator);
	}

	private static By createGenericLabelLocator(Attributes attrs) {
		switch (firstNonNull(attrs.getElementType(), ElementType.any)) {
			case input :
				return GenericLabelFinderRepo.createInputLocator(attrs);
			case button :
				return GenericLabelFinderRepo.createButtonLocator(attrs);
			case link :
				return GenericLabelFinderRepo.createLinkLocator(attrs);
			case section :
				return GenericLabelFinderRepo.createSectionLocator(attrs);
			default :
				return new NullLocator();
		}
	}

	private static By createGenericCssClassFinder(final WebDriver driver, final Attributes attrs) {
		SearchContextFinder searchContextFinder =
			new StaticSearchContextFinder(Arrays.asList(driver));

		By locator = createGenericCssLocator(attrs);

		return new QueryFinder(searchContextFinder, locator);
	}

	private static By createGenericCssLocator(final Attributes attrs) {
		switch (firstNonNull(attrs.getElementType(), ElementType.any)) {
			case link :
				return GenericCssClassFinderRepo.createLinkLocator(attrs);
			default :
				return new NullLocator();
		}
	}

}
