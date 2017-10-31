package com.rtcomps.core.mail;

import static java.util.Collections.*;
import java.util.List;

public class EmailMessage {
	private String to;
	private String cc;
	private String bcc;
	private String from;
	private String subject;
	private String body;
	private List<Attachement> attachements;
	
	
	public EmailMessage(String to, String from, String subject, String body) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.body = body;
		this.attachements = emptyList();
	}

	public EmailMessage(String to, String from, String subject, String body, List<Attachement> attachements) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.body = body;
		this.attachements = attachements;
	}

	public String getTo() {
		return to;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setAttachements(List<Attachement> attachements) {
		this.attachements = attachements;
	}

	public List<Attachement> getAttachements() {
		return attachements;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	
}