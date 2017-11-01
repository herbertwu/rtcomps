package com.rtcomps.data.web.selenium.finders;

import static java.util.Arrays.asList;

import org.openqa.selenium.By;

import com.rtcomps.data.web.def.Attributes;
import com.rtcomps.data.web.selenium.CompositeLocator;

public class GenericLabelFinderRepo {

	public static By createInputLocator(Attributes attrs) {
		return new CompositeLocator(asList(
				new InputElmByForAttributeLookUp(attrs.getLabel()),
				new InputElmByTextDescLookUp(attrs.getLabel())));
	}
	
	public static By createButtonLocator(Attributes attrs) {
		return new CompositeLocator(asList(
				new ButtonElmByInputTagLookUp(attrs.getLabel()),
				new ButtonElmByButtonTagLookUp(attrs.getLabel())));
	}
	
	public static By createLinkLocator(Attributes attrs) {
		return new CompositeLocator(asList(
				new LinkElmByTextLookUp(attrs.getLabel()), 
				new LinkElmByText201403ImplLookUp(attrs.getLabel())));
	}

	public static By createSectionLocator(Attributes attrs) {
		return new MenuHeaderElmByContentLookUp(attrs.getLabel());
	}

}
