package com.rtcomps.data.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtcomps.data.def.ActionRunResult;
import com.rtcomps.data.def.ActionRunStatus;
import com.rtcomps.data.def.Param;
import com.rtcomps.data.web.base.WebActionsBase;
import com.rtcomps.data.web.def.Element;
import com.rtcomps.data.web.def.ElementType;
import com.rtcomps.data.web.def.PageType;
import com.rtcomps.data.web.def.XPathCSSSelectorAttributes;

public class ClickElementAction extends WebActionsBase {
	protected static final Logger logger =  LoggerFactory.getLogger(ClickElementAction.class);
	private String id;
	private String name;
	private String xpath;
	private String cssSelector;
	private String contextElementName;

	public ClickElementAction(@Param(name = "id")String id, 
			@Param(name = "name")String name, 
			@Param(name = "xpath")String xpath, 
			@Param(name = "cssSelector")String cssSelector, 
			@Param(name = "contextElementName") String contextElementName) {
		this.id = id;
		this.name = name;
		this.xpath = xpath;
		this.cssSelector = cssSelector;
		this.contextElementName = contextElementName;
	}





	@Override
	public ActionRunResult execute() {
		try {
			XPathCSSSelectorAttributes attributes = new XPathCSSSelectorAttributes(id, name, xpath,cssSelector, contextElementName, PageType.GENERIC, ElementType.any);
			attributes.verifySingleIdNameXpathCssSelector();
			addContextElementIfAny(attributes);
			Element element = getBrowser().findElement(attributes);
			clickElementWithVerification(element);
			logger.info("clickElement params=" + attributes +", isElementClicked=true");
			return new ActionRunResult(ActionRunStatus.COMPLETED,0, "", "",false);
		} catch (Exception e) {
			return fail(e);
		}
	}

}
