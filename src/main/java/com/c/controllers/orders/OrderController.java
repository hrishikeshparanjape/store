package com.c.controllers.orders;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.user.Customer;
import com.c.domain.user.CustomerRole;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.CustomerRepository;
import com.c.services.order.OrderService;
import com.c.services.user.FacebookGraphApiClient;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(path="/order", method=RequestMethod.POST)
	public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createSubscriptionRequest) throws AddressValidationException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			CreateOrderResponse ret = new CreateOrderResponse();
			ret.setOrder(orderService.createNewOrder(createSubscriptionRequest.getStart(), createSubscriptionRequest.getEnd(), customerEmailAddress));
			return ret;
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	@RequestMapping(path="/order/{id}/cancel", method=RequestMethod.POST)
	public void cancelOrder(@PathVariable String id) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			orderService.cancelOrder(id, customerEmailAddress);
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	@RequestMapping(path="/order/{id}/start", method=RequestMethod.POST)
	public void startRide(@PathVariable String id) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if (isAuthorized(customerEmailAddress, CustomerRole.ROLE_RIDE_PROVIDER)) {
				orderService.startOrder(id, customerEmailAddress);
			} else {
				throw new UnauthorizedUserException("User not authorized");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}

	@RequestMapping(path="/order/{id}/complete", method=RequestMethod.POST)
	public void completeRide(@PathVariable String id) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			if (isAuthorized(customerEmailAddress, CustomerRole.ROLE_RIDE_PROVIDER)) {
				orderService.completeOrder(id, customerEmailAddress);
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
