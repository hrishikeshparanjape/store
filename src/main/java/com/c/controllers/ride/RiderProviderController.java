package com.c.controllers.ride;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.location.RideProvider;
import com.c.domain.order.RideOrder;
import com.c.domain.user.Customer;
import com.c.domain.user.CustomerRole;
import com.c.repositories.CustomerRepository;
import com.c.repositories.RideProviderRepository;
import com.c.services.user.FacebookGraphApiClient;

//@RestController
public class RiderProviderController {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RideProviderRepository rideProviderRepository;
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@RequestMapping(path="/reportlocation", method=RequestMethod.POST)
	public void reportLocation(@RequestBody ReportLocationRequest reportLocationRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if (isAuthorized(customerEmailAddress, CustomerRole.ROLE_RIDE_PROVIDER)) {
				RideProvider rideProvider = rideProviderRepository.findByCustomerEmail(customerEmailAddress);
				if (rideProvider == null) {
					Customer customer = customerRepository.findByEmail(customerEmailAddress);
					rideProvider = new RideProvider();
					rideProvider.setCustomer(customer);
					rideProvider.setCapacity(BigInteger.valueOf(3));
					rideProvider.setRidesInProgress(new ArrayList<RideOrder>());
				}
				rideProvider.setGeoLocation(reportLocationRequest.getGeoLocation());
				rideProvider.setPostCode(reportLocationRequest.getPostCode());
				rideProvider.setOnline(reportLocationRequest.getOnline());
				rideProviderRepository.save(rideProvider);				
			} else {
				throw new UnauthorizedUserException("User not authorized");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	@RequestMapping(path="/rideProviderProfile", method=RequestMethod.GET)
	public RideProvider getRideProviderInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if (isAuthorized(customerEmailAddress, CustomerRole.ROLE_RIDE_PROVIDER)) {
				return rideProviderRepository.findByCustomerEmail(customerEmailAddress);
			} else {
				throw new UnauthorizedUserException("User not authorized");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}


	}
	
	private boolean isAuthorized(String email, CustomerRole roleRideProvider) {
		Customer c = customerRepository.findByEmail(email);
		return c.getCustomerRoles().contains(roleRideProvider);
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
