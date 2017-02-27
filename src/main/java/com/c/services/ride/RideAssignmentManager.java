package com.c.services.ride;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.c.controllers.orders.AddressRequest;
import com.c.domain.location.GeoLocation;
import com.c.domain.location.RideProvider;
import com.c.domain.order.RideOrder;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.OrderRepository;
import com.c.repositories.RideProviderRepository;
import com.c.services.location.AddressLookupService;

@Service
public class RideAssignmentManager {
	
	@Autowired
	private RideProviderRepository rideProviderRepository;
	
	@Autowired
	private AddressLookupService addressLookupService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Async
	public void assignServiceProviderToRideOrder(RideOrder rideOrder) throws AddressValidationException {
		AddressRequest req = new AddressRequest();
		req.setPostCode(rideOrder.getStartLocation().getPostCode());
		req.setLine1(rideOrder.getStartLocation().getLine1());
		GeoLocation startLocation = addressLookupService.getGeoLocationByAddress(req);
		RideProvider rideProvider = getNearestOnlineRideProvider(startLocation);
		rideOrder.setServiceProvider(rideProvider.getCustomer());
		orderRepository.save(rideOrder);
	}
	
	private RideProvider getNearestOnlineRideProvider(GeoLocation start) {
		Iterable<RideProvider> rideProviders = rideProviderRepository.findAll();
		BigDecimal minDistance = BigDecimal.valueOf(Long.MAX_VALUE);
		RideProvider bestRideProvider = null;
		for (RideProvider provider : rideProviders) {
			if (provider.isOnline() && provider.getRidesInProgress().compareTo(provider.getCapacity()) == -1) {
				GeoLocation providerLocation = new GeoLocation();
				providerLocation.setLatitude(provider.getGeoLocation().split(",")[0]);
				providerLocation.setLongitude(provider.getGeoLocation().split(",")[1]);
				BigDecimal distance = start.distanceFrom(providerLocation);
				if (distance.compareTo(minDistance) == -1) {
					minDistance = distance;
					bestRideProvider = provider;
				}
			}
		}
		return bestRideProvider;
	}
}
