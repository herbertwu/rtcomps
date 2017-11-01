package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.rtcomps.data.web.selenium.ElementInspector;

public class InputElmByForAttributeLookUp extends By {

	private final String label;

	public InputElmByForAttributeLookUp(String label) {
		this.label = label;
	}

	public Optional<WebElement> find(SearchContext ctx) {
		Optional<WebElement> labelElement = fluentWaitFindWithTrimmingAndCaseFiltering(label, ctx);
		if (labelElement.isPresent()) {
			return getInputElementById(labelElement.get(), ctx);
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

	private boolean isTextFieldElement(WebElement element) {
		if ("text".equalsIgnoreCase(element.getAttribute("type"))
				|| "password".equalsIgnoreCase(element.getAttribute("type"))) {
			return true;
		} else {
			return false;
		}
	}
	
	private Optional<WebElement> fluentWaitFindWithTrimmingAndCaseFiltering(String label, SearchContext ctx) {
		String trimmedLabel = ElementInspector.trimLabel(label);
		String xpathQuery = "//label[contains("+ElementInspector.toLowerCaseXpathQuery("text()") +",'"+trimmedLabel.toLowerCase()+"')]";
		List<WebElement> candidateLabels = ctx.findElements(By.xpath(xpathQuery));
		return ElementInspector.filterElementByTrimmedLabelOnText(candidateLabels, trimmedLabel);
	}
	
	private Optional<WebElement> getInputElementById(WebElement labelElement, SearchContext ctx) {
		String id = labelElement.getAttribute("for");
		if ( StringUtils.isEmpty(id) || id.trim().isEmpty()) {
			return Optional.absent();
		}

		try {
			WebElement elem = ctx.findElement(By.id(id));
			return isTextFieldElement(elem) ? Optional.of(elem) : Optional.<WebElement> absent();
		} catch (NoSuchElementException e) {
			return Optional.absent();
		}
	}
}
