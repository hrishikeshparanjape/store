package com.c.controllers.kitchen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.food.FoodCategory;
import com.c.domain.food.FoodEvent;
import com.c.domain.food.FoodItem;
import com.c.domain.food.Kitchen;
import com.c.domain.location.Address;
import com.c.domain.user.Customer;
import com.c.domain.user.CustomerRole;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.CustomerRepository;
import com.c.repositories.food.FoodEventRepository;
import com.c.repositories.food.KitchenRepository;
import com.c.services.food.KitchenService;
import com.c.services.user.FacebookGraphApiClient;
import com.c.services.user.PreAuthenticatedFacebookUserAuthenticationToken;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

@RestController
public class KitchenController {
	
	@Autowired
	private FacebookGraphApiClient facebookGraphApiClient;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private KitchenService kitchenService;

	@Autowired
	private KitchenRepository kitchenRepository;
	
	@Autowired
	private FoodEventRepository foodEventRepository;

	@RequestMapping(path="/kitchens", method=RequestMethod.POST)
	public Kitchen createKitchen(@RequestBody CreateKitchenRequest createKitchenRequest) throws AddressValidationException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = getFacebookTokenFromAuthentication(auth);
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			
			Customer owner = getCustomerByEmail(customerEmailAddress);
			if(owner != null) {
				return kitchenService.createKitchen(owner, getAddressFromRequest(createKitchenRequest));	
			} else {
				throw new ValidationException("user not found");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	@RequestMapping(path="/kitchens/{id}/events", method=RequestMethod.POST)
	public FoodEvent createEvent(@PathVariable String id, @RequestBody CreateEventRequest createEventRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = getFacebookTokenFromAuthentication(auth);
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			
			Customer owner = getCustomerByEmail(customerEmailAddress);
			
			Kitchen kitchen = kitchenRepository.findOne(Long.valueOf(id));
			if(owner != null && kitchen != null) {
				return kitchenService.createFoodEvent(kitchen, createEventRequest.getStartTime(), createEventRequest.getEndTime(), new HashSet<FoodItem>());	
			} else {
				throw new ValidationException("user or kitchen not found");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}
	
	@RequestMapping(path="/kitchens/{id}/events/{eventId}/menuitems", method=RequestMethod.POST)
	public FoodItem addMenuItem(@PathVariable("id") String id, @PathVariable("eventId") String eventId, @RequestBody AddMenuItemRequest addMenuItemRequest) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(auth)) {
			String facebookToken = getFacebookTokenFromAuthentication(auth);
			String customerEmailAddress = facebookGraphApiClient.getEmailAddressByAccessToken(facebookToken);
			
			Customer owner = getCustomerByEmail(customerEmailAddress);
			
			Kitchen kitchen = kitchenRepository.findOne(Long.valueOf(id));
			
			if(owner != null && kitchen != null) {
				
				Set<FoodEvent> events = kitchen.getEvents();
				
				for (FoodEvent event : events) {
					if (event.getId() == Long.valueOf(eventId)) {
						FoodItem item = kitchenService.createFoodItem(addMenuItemRequest.getItemName(), new HashSet<FoodCategory>());
						event.getMenuItems().add(item);
						foodEventRepository.save(event);
						return item;
					}
				}
				throw new ValidationException("event not found");
			} else {
				throw new ValidationException("user or kitchen not found");
			}
		} else {
			throw new AuthenticationCredentialsNotFoundException("User not authenticated");
		}
	}

	private Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	private Address getAddressFromRequest(CreateKitchenRequest createKitchenRequest) {
		Address ret = new Address();
		ret.setLine1(createKitchenRequest.getLocation().getLine1());
		ret.setPostCode(createKitchenRequest.getLocation().getPostCode());
		ret.setCountry(createKitchenRequest.getLocation().getCountry());
		return ret;
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
	
	private String getFacebookTokenFromAuthentication(Authentication auth) {
		String facebookToken = null;
		if(auth instanceof PreAuthenticatedFacebookUserAuthenticationToken) {
			facebookToken = (String) ((PreAuthenticatedFacebookUserAuthenticationToken) auth).getDetails();
		} else {
			facebookToken = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
		}
		return facebookToken;
	}

}
