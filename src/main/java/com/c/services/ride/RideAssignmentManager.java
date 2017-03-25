package com.c.services.ride;

import java.math.BigDecimal;
import java.util.List;

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
		RideProvider rideProvider = getNearestOnlineAvailableRideProvider(startLocation);
		rideOrder.setServiceProvider(rideProvider.getCustomer());
		orderRepository.save(rideOrder);
	}
	
	private void x(RideOrder rideOrder) {
		Iterable<RideProvider> rideProviders = rideProviderRepository.findAll();
		for (RideProvider provider : rideProviders) {
			List<RideOrder> rideOrders = provider.getRidesInProgress();
			for (int i = 0; i< rideOrders.size(); i++) {
				String currentZipCode = null; //get current provider location
				String providerEndZipCode = rideOrders.get(i).getEndLocation().getPostCode();
				if (getRoute(currentZipCode, providerEndZipCode)
						.contains(rideOrder.getEndLocation().getPostCode())) {
					provider.getRidesInProgress().add(i, rideOrder);
				}
			}
		}
	}
	
	private List<String> getRoute(String currentZipCode, String endZipCode) {
		return null;
	}
	
	private RideProvider getNearestOnlineAvailableRideProvider(GeoLocation start) {
		Iterable<RideProvider> rideProviders = rideProviderRepository.findAll();
		BigDecimal minDistance = BigDecimal.valueOf(Long.MAX_VALUE);
		RideProvider bestRideProvider = null;
		for (RideProvider provider : rideProviders) {
			if (provider.isOnline() && provider.getRidesInProgress().size() < provider.getCapacity().intValue()) {
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
