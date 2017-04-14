package com.c.services.email;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class WelcomeEmailSender extends EmailSender {
	
	private static final String from = "team@c.com";
	
	private static final String subject = "Welcome to C";
	
	private static final String template = "welcome";
	
	public void send(Locale locale, Map<String, String> data) throws MessagingException {
		
		final Context ctx = new Context(locale);
		ctx.setVariable("subscriptionDate", new Date());
		
		super.send(data.get("recipientEmail"), ctx);
	}
	
	protected String getSubject() {
		return subject;
	}
	
	protected String getFrom() {
		return from;
	}
	
	protected String getTemplate() {
		return template;
	}
}
