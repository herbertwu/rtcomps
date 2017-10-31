package com.rtcomps.core.mail;

import static java.util.Collections.*;
import java.util.Map;

import com.rtcomps.core.template.FreeMarkerTextTemplate;
import com.rtcomps.core.template.TextTemplate;


public class TemplateEmailMessage extends EmailMessage {
	private Map<String, Object> headerModel;
	private Map<String, Object> bodyModel;
	
	public TemplateEmailMessage(String to, String from, String subject, String body) {
		super(to,from,subject,body);
		this.headerModel = emptyMap();
		this.bodyModel = emptyMap();
	}
	
	public TemplateEmailMessage(String to, String from, String subject, String body, Map<String, Object> headerModel, Map<String, Object> bodyModel) {
		super(to,from,subject,body);
		this.headerModel = headerModel;
		this.bodyModel = bodyModel;
	}

	public String getSubject() {
		TextTemplate tmpl = new FreeMarkerTextTemplate(super.getSubject(), this.headerModel);
		return tmpl.resolve();
	}

	public String getBody() {
		TextTemplate tmpl = new FreeMarkerTextTemplate(super.getBody(), this.bodyModel);
		return tmpl.resolve();
	}
}