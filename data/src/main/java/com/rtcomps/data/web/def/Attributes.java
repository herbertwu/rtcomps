package com.rtcomps.data.web.def;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.StringUtils;


public class Attributes {
	
	private static final int GENERIC_BASE=100;
	private static final int ENCORE1_BASE=200;
	private static final int ENCORE2_BASE=300;
	private static final int  EXTJS4_BASE=400;
	
	public static final int UNDEFINED = -1;
	public static final int UNDEFINED_CONTENT_HASH = 0;
	
	public static final int        GENERIC_ID = 101;
	public static final int      GENERIC_NAME = 102;
	public static final int     GENERIC_LABEL = 103;
	public static final int GENERIC_CSS_CLASS = 104;
	public static final int GENERIC_XPATH     = 105;
	public static final int GENERIC_CSS_SELECTOR = 106;
	
	public static final int        ENCORE1_ID = 201;
	public static final int      ENCORE1_NAME = 202;
	public static final int     ENCORE1_LABEL = 203;
	public static final int ENCORE1_CSS_CLASS = 204;
	
	public static final int        ENCORE2_ID = 301;
	public static final int      ENCORE2_NAME = 302;
	public static final int     ENCORE2_LABEL = 303;
	public static final int ENCORE2_CSS_CLASS = 304;
	
	public static final int        EXTJS4_ID = 401;
	public static final int      EXTJS4_NAME = 402;
	public static final int     EXTJS4_LABEL = 403;
	
	private String id;
	private String name;
	private String label;
	private String cssClass;
	private String fieldValue;
	private String contextElementName;
	private Element contextElement;
	
	private final PageType pageType;
	private ElementType elementType=ElementType.any;
	
	public Attributes(String id, String name, String label, PageType pageType) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.pageType = pageType;
	}
	
	public Attributes(String id, String name, String label, String contextElementName, PageType pageType, ElementType elementType) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.contextElementName = contextElementName;
		this.pageType = pageType;
		this.elementType = elementType;
	}
	
	public Attributes(PageType pageType, ElementType elementType) {
		this.pageType = pageType;
		this.elementType = elementType;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}
	
	public String getContextElementName() {
		return contextElementName;
	}
	
	public String getCssClass() {
		return cssClass;
	}	

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Element getContextElement() {
		return contextElement;
	}

	public void setContextElement(Element contextElement) {
		this.contextElement = contextElement;
	}

	public PageType getPageType() {
		return pageType;
	}
	
	public ElementType getElementType() {
		return elementType;
	}
	
	private int getContentHash() {
		if ( onlySingleAttributeValuePresent()) {
			return singleValueHash();
		} else {
			return  UNDEFINED_CONTENT_HASH;
		}
	}
	
	protected int singleValueHash() {
		if ( notEmpty(getId())) {
			return 1;
		}
		if ( notEmpty(getName()) ) {
			return 2;
		}
		if ( notEmpty(getLabel())) {
			return 3;
		}
		
		if ( notEmpty(getCssClass())) {
			return 4;
		}

		return  UNDEFINED_CONTENT_HASH;
	}
	
	protected boolean notEmpty(String fieldValue) {
		return !StringUtils.isEmpty((fieldValue==null||fieldValue.isEmpty())?null:fieldValue.trim());
	}
	
	public int getValueType() {
		if (getPageType() == PageType.GENERIC) {
			return getGenericValueType();
		} else if (getPageType() == PageType.ENCORE1) {
			return  getEncore1ValueType();
		} else if (getPageType() == PageType.ENCORE2) {
			return getEncore2ValueType();
		} else if (getPageType() == PageType.EXTJS4) {
			return getExtjs4ValueType();
		}
		return UNDEFINED;	
	}
	
	private int getGenericValueType() {
		return GENERIC_BASE + getContentHash();
	}
	
    private int getEncore1ValueType() {
    	return ENCORE1_BASE + getContentHash();
	}

    private int getEncore2ValueType() {
    	return ENCORE2_BASE + getContentHash();
    }
    
    private int getExtjs4ValueType() {
    	return EXTJS4_BASE + getContentHash();
    }
    
    protected boolean onlySingleAttributeValuePresent() {
    	int checkSum = BooleanUtils.toInteger(!StringUtils.isEmpty((id==null||id.isEmpty())?null:id.trim())) + 
    			BooleanUtils.toInteger(!StringUtils.isEmpty((name==null||name.isEmpty())?null:name.trim()))      + 
    			BooleanUtils.toInteger(!StringUtils.isEmpty((label==null||label.isEmpty())?null:label.trim()))     +
    	        BooleanUtils.toInteger(!StringUtils.isEmpty((cssClass==null||cssClass.isEmpty())?null:cssClass.trim()));
    	return checkSum == 1;
    }

	@Override
	public String toString() {
		return "Attributes [id=" + id + ", name=" + name + ", label=" + label
				+ ", cssClass=" + cssClass + ", fieldValue=" + fieldValue
				+ ", contextElem=" + contextElement + ", pageType=" + pageType
				+ ", elementType=" + elementType + "]";
	}


	public void verifySingleIdNameLabelValue() {
		
		if ((StringUtils.isEmpty(id) || id.trim().isEmpty()) 
				&& (StringUtils.isEmpty(label) || label.trim().isEmpty())
				&& (StringUtils.isEmpty(name) || name.trim().isEmpty()) 
			   ) {
				   throw new IllegalStateException("Element attribute is missing. Either id or label or name must be provided.");
		}
		
		int checkSum = BooleanUtils.toInteger(id != null) + BooleanUtils.toInteger(name != null) + BooleanUtils.toInteger(label != null );

		if ( checkSum !=1) {
			throw new IllegalStateException("Must provide one and only one value (id or name or label).");
		} 
	}
	
	public String getSingleIdNameLabelValue() {
		if (!StringUtils.isEmpty(id) && !id.trim().isEmpty()) {
			return id;
		} else if (!StringUtils.isEmpty(name) && !name.trim().isEmpty()) {
			return name;
		} else {
			return label;
		}
	}
	
	
	public void verifySingleIdNameCssClassValue() {
		if ((StringUtils.isEmpty(id) || id.trim().isEmpty()) 
				&& (StringUtils.isEmpty(cssClass) || cssClass.trim().isEmpty())
				&& (StringUtils.isEmpty(name) || name.trim().isEmpty()) 
			   ) {
				   throw new IllegalStateException("Element attribute is missing. Either id or CSS class must be provided.");
		}
		
		int checkSum = BooleanUtils.toInteger(id != null) + BooleanUtils.toInteger(name != null) + BooleanUtils.toInteger(cssClass != null);

		if ( checkSum !=1) {
			throw new IllegalStateException("Must provide one and only one value (id or name or CSS class).");
		} 
	}

	
}
