package com.rtcomps.data.web.selenium.finders;

import static java.util.Arrays.asList;

import org.openqa.selenium.By;

import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.selenium.CompositeLocator;

public class GenericCssClassFinderRepo {

	public static By createLinkLocator(Attributes attrs) {
		return new CompositeLocator(asList(
				new LinkElmByCssClassNameLookUp(attrs.getCssClass()),
				new LinkElmByCssClassName201403ImplLookUp(attrs.getCssClass())));
	}

}
