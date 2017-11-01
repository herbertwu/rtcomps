package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Optional;
import com.rtcomps.data.web.selenium.ElementInspector;

public class LinkElmByText201403ImplLookUp extends By {

	private final String label;

	public LinkElmByText201403ImplLookUp(String label) {
		this.label = label;
	}
	
	private Optional<WebElement> find(SearchContext ctx) {
		String linkQuery = createLinkLookupQuery(label, ctx);
		List<WebElement> allLinks = ctx.findElements(By.xpath(linkQuery));
		return ElementInspector.filterElementByTrimmedLabelOnText(allLinks,
				ElementInspector.trimLabel(label));
	}
	
	@Override
	public WebElement findElement(SearchContext ctx) throws NoSuchElementException {
		Optional<WebElement> elem = find(ctx);
		if (elem.isPresent()) {
			return elem.get();
		}
		throw new NoSuchElementException("not found");
	}

	@Override
	public List<WebElement> findElements(SearchContext ctx) {
		throw new UnsupportedOperationException();
	}

	private String createLinkLookupQuery(String label, SearchContext ctx) {
		String trimmedLabel = ElementInspector.trimLabel(label);
		return ".//descendant-or-self::div[contains(" + ElementInspector.toLowerCaseXpathQuery("text()") + ",'" + trimmedLabel.toLowerCase() + "')]";
	}

}
