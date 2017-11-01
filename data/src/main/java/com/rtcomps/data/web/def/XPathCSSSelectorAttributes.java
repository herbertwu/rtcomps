package com.rtcomps.data.web.def;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.StringUtils;

public class XPathCSSSelectorAttributes extends Attributes {
	
	public static final int ENCORE1_XPATH     = 205;
	
	private final String xpath;
	private final String cssSelector;
	
	public XPathCSSSelectorAttributes(String id, String name, String xpath, String cssSelector,String contextElementName,
			PageType pageType, ElementType elementType) {
		super(id, name, null, contextElementName,pageType, elementType);
		this.xpath = xpath;
		this.cssSelector = cssSelector;
	}
	
	
	
	public XPathCSSSelectorAttributes(PageType pageType, ElementType elementType, String xpath) {
		super(pageType,elementType);
		this.xpath = xpath;
		this.cssSelector = null;
	}



	public String getXpath() {
		return xpath;
	}

	public String getCssSelector() {
		return cssSelector;
	}
	
	@Override
	protected int singleValueHash() {
		int idNameValue = super.singleValueHash();
		if ( idNameValue != UNDEFINED_CONTENT_HASH) {
			return idNameValue;
		}
		if ( notEmpty(getXpath())) {
			return 5;
		}
		if ( notEmpty(getCssSelector())) {
			return 6;
		}
		return  UNDEFINED_CONTENT_HASH;
	}
	
    @Override
	protected boolean onlySingleAttributeValuePresent() {
    	if (super.onlySingleAttributeValuePresent()) {
    		return true;
    	}
    	int checkSum =  BooleanUtils.toInteger(!StringUtils.isEmpty((xpath==null)?null:xpath.trim())) +
    	                BooleanUtils.toInteger(!StringUtils.isEmpty((cssSelector==null)?null:cssSelector.trim()));
    	return checkSum == 1;
    }
	
	
	public void verifySingleIdNameXpathCssSelector() {
		if ((StringUtils.isEmpty(getId()) || getId().trim().isEmpty()) 
				&& (StringUtils.isEmpty(xpath) || xpath.trim().isEmpty())
				&& (StringUtils.isEmpty(cssSelector) || cssSelector.trim().isEmpty())
				&& (StringUtils.isEmpty(getName()) || getName().trim().isEmpty()) 
			   ) {
				   throw new IllegalStateException("Element attribute is missing. Either id or name or xpath or cssSelector must be provided.");
		}
		
		int checkSum = BooleanUtils.toInteger(getId() != null && !getId().isEmpty()) + BooleanUtils.toInteger(getName() != null&& !getName().isEmpty()) + BooleanUtils.toInteger(xpath != null &&!xpath.isEmpty()) + BooleanUtils.toInteger(cssSelector != null&&!cssSelector.isEmpty());

		if ( checkSum !=1) {
			throw new IllegalStateException("Must provide one and only one value (id or name or xpath or cssSelector).");
		} 
	}

	@Override
	public String toString() {
		return "XPathCSSSelectorAttributes [xpath=" + xpath + ", cssSelector="
				+ cssSelector + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getLabel()=" + getLabel() + ", getCssClass()="
				+ getCssClass() + ", getFieldValue()=" + getFieldValue()
				+ ", getContextElement()=" + getContextElement()
				+ ", getPageType()=" + getPageType() + ", getElementType()="
				+ getElementType() + ", getValueType()=" + getValueType() + "]";
	}
	
	

}
