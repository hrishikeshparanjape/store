package com.c.controllers.payment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.services.payment.CreditCardService;
import com.c.services.user.FacebookGraphApiClient;
import com.c.services.user.PreAuthenticatedFacebookUserAuthenticationToken;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

//@RestController
public class PaymentController {
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@RequestMapping(path="/paymentmethod", method=RequestMethod.POST)
	public ResponseEntity<Void> createPaymentMethod(@RequestBody CreatePaymentMethodRequest createPaymentMethodRequest) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = getFacebookTokenFromAuthentication(auth);
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if ("card".equals(createPaymentMethodRequest.getType())) {
				creditCardService.addNewCreditCard(customerEmailAddress, createPaymentMethodRequest.getData());
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				throw new UnsupportedOperationException("payment method not supported");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	private String getFacebookTokenFromAuthentication(Authentication auth) {
		String facebookToken = null;
		if(auth instanceof PreAuthenticatedFacebookUserAuthenticationToken) {
			facebookToken = (String) ((PreAuthenticatedFacebookUserAuthenticationToken) auth).getDetails();
		} else {
			facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
		}
		return facebookToken;
	}
	
	private boolean isAuthenticated(Authentication auth) {
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_USER")) {
				return true;
			}
		}
		return false;
	}
}
