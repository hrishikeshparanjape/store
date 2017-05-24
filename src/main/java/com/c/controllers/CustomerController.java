package com.c.controllers;

import java.security.Principal;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.user.Customer;
import com.c.domain.user.CustomerRole;
import com.c.repositories.CustomerRepository;
import com.c.services.user.CustomerService;
import com.c.services.user.FacebookGraphApiClient;
import com.c.services.user.PreAuthenticatedFacebookUserAuthenticationToken;

@RestController
public class CustomerController {
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(path="", method=RequestMethod.GET)
	public Customer user(Principal principal) {
		String facebookToken = ((OAuth2AuthenticationDetails)((OAuth2Authentication) principal).getDetails()).getTokenValue();
		String emailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
		Customer ret = customerService.signupOrSignInCustomer(emailAddress);
		return ret;
	}
	
	@RequestMapping(path="/login/preauth/facebook", method=RequestMethod.POST)
	public Customer preAuth(@Valid @RequestBody FacebookPreAuthRequest facebookPreAuthRequest) {
		String facebookToken = facebookPreAuthRequest.getToken();
		String emailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
		Customer ret = customerService.signupOrSignInCustomer(emailAddress);
		PreAuthenticatedFacebookUserAuthenticationToken preAuthenticatedFacebookUserAuthenticationToken = new PreAuthenticatedFacebookUserAuthenticationToken(ret, facebookPreAuthRequest.getToken());
		SecurityContextHolder.getContext().setAuthentication(preAuthenticatedFacebookUserAuthenticationToken);
		return ret;
	}
	
	@RequestMapping(path="/enrollRideProvider", method=RequestMethod.POST)
	public Customer markCustomerAsDriver(@RequestBody EnrollRideProviderRequest enrollRideProviderRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if (isAuthorized(customerEmailAddress, CustomerRole.ROLE_ADMIN)) {
				return customerService.markCustomerAsDriver(enrollRideProviderRequest.getEmail());
			} else {
				throw new UnauthorizedUserException("User not authorized");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	private boolean isAuthorized(String email, CustomerRole role) {
		Customer c = customerRepository.findByEmail(email);
		return c.getCustomerRoles().contains(role);
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
