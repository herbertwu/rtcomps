package com.rtcomps.data.web.def;

import com.rtcomps.data.web.selenium.ElementInspector;

public class XPathHelper {

	public static String containingClass(String className) {
		return "contains(concat(' ',normalize-space(@class),' '),' " + className + " ')";
	}

	public static String containingCaseInsensitiveText(String text) {
		return "contains(" + ElementInspector.toLowerCaseXpathQuery("text()") + ",normalize-space('"
				+ text.toLowerCase() + "'))";
	}

}
