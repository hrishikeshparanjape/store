package com.c.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.Customer;
import com.c.services.CustomerService;
import com.c.services.FacebookGraphApiClient;

@RestController
public class FacebookRegistrationController {
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(path="/customer", method=RequestMethod.GET)
	public Customer user(Principal principal) {
		String facebookToken = ((OAuth2AuthenticationDetails)((OAuth2Authentication) principal).getDetails()).getTokenValue();
		String emailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
		Customer ret = customerService.signupOrSignInCustomer(emailAddress);
		return ret;
	}
}
