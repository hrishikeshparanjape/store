package com.c.services.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public abstract class EmailSender {
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;
	
	protected void send(String to, Context emailContext) throws MessagingException {
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject(getSubject());
		message.setFrom(getFrom());
		message.setTo(to);
		
		final String htmlContent = templateEngine.process(getTemplate(), emailContext);
		message.setText(htmlContent, true); // true = isHtml
		mailSender.send(mimeMessage);
	}
	
	protected abstract String getSubject();
	
	protected abstract String getFrom();
	
	protected abstract String getTemplate();

}
