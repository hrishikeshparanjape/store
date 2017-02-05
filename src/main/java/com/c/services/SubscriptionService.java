package com.c.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.Address;
import com.c.domain.Customer;
import com.c.domain.GeoLocation;
import com.c.domain.Plan;
import com.c.domain.RideSubscription;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.CustomerRepository;

@Service
public class SubscriptionService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressLookupService addressLookupService;

	public RideSubscription startNewRideSubscription(Address rideStartPoint, Address rideFinishPoint, String customerEmailAddress) throws AddressValidationException {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		GeoLocation start = addressLookupService.getGeoLocationByAddress(rideStartPoint);
		GeoLocation end = addressLookupService.getGeoLocationByAddress(rideFinishPoint);
		Plan plan = getPlanByRideDistance(start.distanceFrom(end));
		RideSubscription subscription = new RideSubscription();
		subscription.setPlan(plan);
		subscription.setCustomer(customer);
		subscription.setRideStartAddress(start);
		subscription.setRideEndAddress(end);
		return subscription;
	}
	
	private Plan getPlanByRideDistance(BigDecimal rideDistance) {
		Plan ret = new Plan();
		return ret;
	}
}
