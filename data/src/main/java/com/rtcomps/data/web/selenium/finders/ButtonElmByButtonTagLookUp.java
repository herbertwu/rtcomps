package com.rtcomps.data.web.selenium.finders;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.base.Optional;
import com.rtcomps.data.web.selenium.ElementInspector;

public class ButtonElmByButtonTagLookUp extends By {

	private final String label;

	public ButtonElmByButtonTagLookUp(String label) {
		this.label = label;
	}

	public Optional<WebElement> find(SearchContext ctx) {
		String trimmedLabel = ElementInspector.trimLabel(label);
		String inputTagButtonQuery = ".//button[contains(" + ElementInspector.toLowerCaseXpathQuery(".") + ",'" + trimmedLabel.toLowerCase() + "') and (@type='submit' or @type='reset' or @type='button')]";
		List<WebElement> allButtons = ctx.findElements(By.xpath(inputTagButtonQuery));
		return ElementInspector.filterElementByTrimmedLabelOnText(allButtons, trimmedLabel);
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
