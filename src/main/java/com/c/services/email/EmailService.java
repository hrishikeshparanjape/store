package com.c.services.email;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private WelcomeEmailSender welcomeEmailSender;

	public void sendWelcomeEmail(String recipientEmail, Locale locale)
			throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		data.put("recipientEmail", recipientEmail);
		
		welcomeEmailSender.send(locale, data);
	}

}
