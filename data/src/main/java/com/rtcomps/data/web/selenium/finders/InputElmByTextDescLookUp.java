package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Optional;
import com.rtcomps.data.web.selenium.ElementFinderUtil;
import com.rtcomps.data.web.selenium.ElementInspector;

public class InputElmByTextDescLookUp extends By {

	private final String label;

	public InputElmByTextDescLookUp(String label) {
		this.label = label;
	}
	
	private Optional<WebElement> find(SearchContext ctx) {
		Optional<WebElement> labelElement = fluentWaitFindWithTrimmingAndCaseFiltering(label, ctx);
		if (labelElement.isPresent()) {
			return getFirstInputElementChild(labelElement.get());
		} else {
			return Optional.absent();
		}
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

	private Optional<WebElement> getFirstInputElementChild(WebElement labelElement) {
		return ElementFinderUtil.find(labelElement, By.xpath(".//input[1][@type='text' or @type='password']"));
	}
	
	private Optional<WebElement> fluentWaitFindWithTrimmingAndCaseFiltering(String label, SearchContext ctx) {
		String trimmedLabel = ElementInspector.trimLabel(label);
		String xpathQuery = "//label[contains("+ElementInspector.toLowerCaseXpathQuery("text()") +",'"+trimmedLabel.toLowerCase()+"')]";
		List<WebElement> eles = ctx.findElements(By.xpath(xpathQuery));
		return ElementInspector.filterElementByTrimmedLabelOnText(eles, trimmedLabel);
	}
}
