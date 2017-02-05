package com.c.controllers.subscriptions;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.exceptions.AddressValidationException;
import com.c.services.FacebookGraphApiClient;
import com.c.services.SubscriptionService;

@RestController
public class SubscriptionController {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@RequestMapping(path="/subscription", method=RequestMethod.POST)
	public CreateSubscriptionResponse createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest) throws AddressValidationException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			CreateSubscriptionResponse ret = new CreateSubscriptionResponse();
			ret.setSubscription(subscriptionService.startNewRideSubscription(createSubscriptionRequest.getStart(), createSubscriptionRequest.getEnd(), customerEmailAddress));
			return ret;
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
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
