package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Optional;
import com.rtcomps.data.web.selenium.ElementInspector;

public class ButtonElmByInputTagLookUp extends By {

	private static final String INPUT_TAG_VALUE_ATTRIBUTE="value";
	private final String label;

	public ButtonElmByInputTagLookUp(String label) {
		this.label = label;
	}

	public Optional<WebElement> find(SearchContext ctx) {
		String trimmedLabel = ElementInspector.trimLabel(label);
		String inputTagButtonQuery = ".//input[contains(" + ElementInspector.toLowerCaseXpathQuery("@value") + ",'" + trimmedLabel.toLowerCase() + "') and (@type='submit' or @type='reset' or @type='button')]";
		List<WebElement> allButtons = ctx.findElements(By.xpath(inputTagButtonQuery));
		return ElementInspector.filterElementByTrimmedLabelOnAttribute(allButtons, trimmedLabel, INPUT_TAG_VALUE_ATTRIBUTE);
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
	public List<WebElement> findElements(SearchContext arg0) {
		throw new UnsupportedOperationException();
	}

}
