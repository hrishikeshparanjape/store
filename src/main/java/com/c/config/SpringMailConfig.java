package com.c.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class SpringMailConfig {

	@Value("${javamailsender.host:smtp.gmail.com}")
	private String host;

	@Value("${javamailsender.port:587}")
	private String port;

	@Value("${javamailsender.protocol:smtp}")
	private String protocol;

	@Value("${javamailsender.username:[GMAIL_USERNAME]}")
	private String username;

	@Value("${javamailsender.password:[GMAIL_PASSWORD]}")
	private String password;

	@Bean
	public JavaMailSender mailSender() throws IOException {

		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// Basic mail sender configuration, based on emailconfig.properties
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		final Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		javaMailProperties.setProperty("mail.smtp.quitwait", "false");

		mailSender.setJavaMailProperties(javaMailProperties);

		return mailSender;

	}
}