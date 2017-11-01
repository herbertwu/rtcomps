package com.rtcomps.data.web.selenium;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.rtcomps.data.web.def.Element;
import com.rtcomps.data.web.def.ElementNotFoundException;


public class ElementFinderUtil {
	private static final Logger log = Logger.getLogger(ElementFinderUtil.class);
	private static final int IGNORE_TAIL_IFRAMES=1;
	
	
	public static Optional<Element> findElementWithWait(By locator, final SearchContext ctx, long timeoutSecs,
			int intervalSecs) {
		try {
			Wait<By> wait = new FluentWait<By>(locator)
					.withTimeout(timeoutSecs, TimeUnit.SECONDS)
					.pollingEvery(intervalSecs, TimeUnit.SECONDS)
					.ignoring(ElementNotFoundException.class);

			Element ele = wait.until(new Function<By, Element>() {
				
				@Override
				public Element apply(By locator) {
					log.debug("_seleniumFindElementWithWait()... locator=" + locatorToString(locator));
					try {
						return new WebDriverElement(ctx.findElement(locator));
					} catch (NoSuchElementException e) {
						throw new ElementNotFoundException("Can not find the element. " + locatorToString(locator));
					}
				}
				
			});

			return Optional.of(ele);
		} catch (TimeoutException e) {
			log.error("_seleniumFindElementWithWait> webElement can not find the element in timeout period: timeout="
					+ timeoutSecs + ", locator=" + locatorToString(locator));
			return Optional.absent();
		}
	}
	
	public static Optional<WebElement> find(final WebElement webElement, final By locator) {
		try {
			return Optional.of(webElement.findElement(locator));
		} catch (NoSuchElementException e) {
			return Optional.absent();
		}
	}
    
    public static List<List<Integer>> findAllEncore1FramePaths(final WebDriver driver) {
    	List<List<Integer>> frameTrees = new ArrayList<List<Integer>>();
    	List<WebElement> iframes = getTopLevelIFrames(driver);
    	if ( iframes.isEmpty() ) {
    		return frameTrees;
    	}
    	for (int iframeIndex =0; iframeIndex<iframes.size()-IGNORE_TAIL_IFRAMES; iframeIndex++) {
			IFrameSwitcher.switchToIframeByIndex(driver, iframeIndex);
    		List<WebElement> frames = driver.findElements(By.tagName("frame"));
    		for (int frameIndex =0; frameIndex < frames.size(); frameIndex++ ) {
        		frameTrees.add(Arrays.asList(iframeIndex,frameIndex));
        	}
			IFrameSwitcher.switchOutIframe(driver);
    		frameTrees.add(Arrays.asList(iframeIndex));
    	}
    	return frameTrees;
    	
    }
    
    private static List<WebElement> getTopLevelIFrames(final WebDriver driver) {
    	return driver.findElements(By.tagName("iframe"));
    }
    
	public static String locatorToString(By locator) {
		String value = locator.toString();
		if ("[unknown locator]".equals(value)) {
			value = reflectionToString(locator, SHORT_PREFIX_STYLE);
		}
		return value;
	}

}
