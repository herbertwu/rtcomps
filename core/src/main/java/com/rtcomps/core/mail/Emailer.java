package com.rtcomps.core.mail;

public interface Emailer {
	void send(EmailMessage msgs) throws Exception;
}
