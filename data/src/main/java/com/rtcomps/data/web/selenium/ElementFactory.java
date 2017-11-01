package com.rtcomps.data.web.selenium;

import org.openqa.selenium.WebElement;

import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.def.Element;


public class ElementFactory {
	
	public static Element createDecorElement(Element sourceElement, Attributes attrs) {
		WebElement webElement = ((WebDriverElement)sourceElement).getWebElement();
//		if (ElementType.select == attrs.getElementType()) {
//			if (PageType.ENCORE1 == attrs.getPageType() ) {
//				return new Encore1DropdownElement(webElement);
//			} else if (PageType.ENCORE2 == attrs.getPageType()) {
//				return new Encore2DropdownElement(webElement);
//			} else if (PageType.EXTJS4 == attrs.getPageType()) {
//				return new Extjs4DropdownElement(webElement);
//			} 
//		} else 	if (ElementType.checkbox == attrs.getElementType()) {
//			if (PageType.EXTJS4 == attrs.getPageType()) {
//				return new Extjs4CheckboxElement(webElement);
//			} 
//		} 
		
		return sourceElement;
	}

}
