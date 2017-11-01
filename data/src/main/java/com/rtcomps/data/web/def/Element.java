package com.rtcomps.data.web.def;


public interface Element {

	void setValue(String value);
	String getValue();
	public void click();
	boolean isSelected();
	void selectByVisibleText(String text);
	String getAttributeValue(String attributeName);
	String getTagName();

}
