package com.rtcomps.core.mail;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

public class CommonsTextEmailer implements Emailer {
	private static final String ADDRESS_DELIM=",";
	private static final String ADDRESS_DELIM2=";";
	private final Logger log =  LoggerFactory.getLogger(CommonsTextEmailer.class);
	private String smtp;
	private int port;
	private String acctName;
	private String acctPass;
	private boolean isSSL=false;
	
	public CommonsTextEmailer() {
		//TODO
		//this.smtp = Properties.getString("smtp.host");
		//this.port = Properties.getInt("smtp.port");
	}
	
	public CommonsTextEmailer(String smtp, int port) {
		this.smtp = smtp;
		this.port = port;
	}

	public CommonsTextEmailer(String smtp, int port, String acctName, String acctPass) {
		this.smtp = smtp;
		this.port = port;
		this.acctName = acctName;
		this.acctPass = acctPass;
	}
	
	public boolean isSSL() {
		return isSSL;
	}
	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	public void send(EmailMessage msgs)	 throws Exception {
		Email email = createTextEmail();
		email.setHostName(smtp);
		email.setSmtpPort(port);
		if (authRequired()) {
			setAuth(email);
		}
		email.setSSLOnConnect(isSSL);
		email.setFrom(msgs.getFrom());
		email.setSubject(msgs.getSubject());
		email.setMsg(msgs.getBody());
		email.addTo(parseEmailAddresses(msgs.getTo()));
		if (!StringUtils.isEmpty(msgs.getCc())) {
			email.addCc(parseEmailAddresses(msgs.getCc()));
		}
		
		if (!StringUtils.isEmpty(msgs.getBcc())) {
			email.addBcc(parseEmailAddresses(msgs.getBcc()));
		}
		
		for (Attachement att : msgs.getAttachements()) {
			((MultiPartEmail)email).attach(createCommonsAttachement(att));
		}
		String msgsId = email.send();
		log.info("Email sent successfully: messageId="+msgsId);
	}

	private EmailAttachment createCommonsAttachement(Attachement att) {
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath(att.getPath());
		  attachment.setDisposition(att.getDisposition());
		  attachment.setDescription(att.getDescription());
		  attachment.setName(att.getName());
		  return attachment;
	}

	private void setAuth(Email email) {
		 email.setAuthenticator(new DefaultAuthenticator(acctName, acctPass));
	}

	private boolean authRequired() {
		return acctName!=null && acctPass!=null;
	}

	@VisibleForTesting
	Email createTextEmail() {
		return new MultiPartEmail();
	}

	private String[] parseEmailAddresses(String emails) {
		if (emails.indexOf(ADDRESS_DELIM)>0) {
			return emails.split(ADDRESS_DELIM);
		} else {
			return emails.split(ADDRESS_DELIM2);
		}
	}
}
