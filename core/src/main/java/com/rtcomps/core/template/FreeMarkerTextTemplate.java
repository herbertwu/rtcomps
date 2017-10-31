package com.rtcomps.core.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTextTemplate implements TextTemplate {
	private final String textTemplate;
	private Map<String, Object> model;
	
	public FreeMarkerTextTemplate(String codeTemplate, Map<String, Object> model) {
		this.textTemplate = codeTemplate;
		this.model= model;
	}

	@Override
	public String resolve() {
		try {
			Template template = createTemplate();
			Writer out = new StringWriter();
			template.process(model, out);
		    return out.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		} catch (TemplateException te) {
			te.printStackTrace();
			throw new RuntimeException(te);
		}
	}

	private Template createTemplate() throws IOException  {
		Configuration cfg = new Configuration();
		Template template = new Template("jclJob", new StringReader(textTemplate), cfg);
		return template;
	}

}
