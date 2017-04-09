package com.c.services.ride;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.c.domain.location.GeoLocation;
import com.c.domain.location.RideProvider;
import com.c.domain.location.ZipCode;
import com.c.domain.order.RideOrder;
import com.c.domain.order.RideStatus;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.OrderRepository;
import com.c.repositories.RideProviderRepository;
import com.c.repositories.ZipCodeRepository;
import com.c.services.location.IAddressLookupService;
import com.c.services.location.RouteService;

@Service
public class RideAssignmentManager {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ZipCodeRepository zipCodeRepository;
	
	@Autowired
	private RideProviderRepository rideProviderRepository;
	
	@Autowired
	private IAddressLookupService addressLookupService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Async
	public Future<Void> assignServiceProviderToRideOrder(RideOrder rideOrder) throws AddressValidationException {
		GeoLocation startLocation = addressLookupService.getGeoLocationByAddress(rideOrder.getStartLocation().getPostCode(), rideOrder.getStartLocation().getLine1());
		RideProvider nearestFreeRideProvider = nearestFreeRideProvider(startLocation);
		List<RideProvider> rideProvidersWithCommonDestination = allRideProvidersWithCommonDestination(rideOrder.getEndLocation().getPostCode());
		List<RideProvider> rideProvidersWithCurrentDestinationInRoute = allRideProvidersWithCurrentDestinationInRoute(rideOrder.getEndLocation().getPostCode());
		List<RideProvider> rideProvidersWithRoutePassingThroughOrigin = allRideProvidersWithRoutePassingThroughOrigin(rideOrder.getStartLocation().getPostCode());
		RideProvider bestRideProvider = chooseBestRideProvider(startLocation, nearestFreeRideProvider, rideProvidersWithCommonDestination, rideProvidersWithCurrentDestinationInRoute, rideProvidersWithRoutePassingThroughOrigin);
		if (bestRideProvider == null) {
			rideOrder.setStatus(RideStatus.CANNOT_BE_ASSIGNED);
			orderRepository.save(rideOrder);
		} else {
			rideOrder.setStatus(RideStatus.ASSIGNED);
			rideOrder.setServiceProvider(bestRideProvider);
			orderRepository.save(rideOrder);
			bestRideProvider.getRidesInProgress().add(rideOrder);
			rideProviderRepository.save(bestRideProvider);
		}
	    return new AsyncResult<Void>(null);
	}
	
	private RideProvider chooseBestRideProvider(GeoLocation startLocation,
			RideProvider nearestFreeRideProvider,
			List<RideProvider> rideProvidersWithCommonDestination,
			List<RideProvider> rideProvidersWithCurrentDestinationInRoute,
			List<RideProvider> rideProvidersWithRoutePassingThroughOrigin) {
		
		RideProvider bestProvider = nearestFreeRideProvider;
		
		GeoLocation bestProviderLocation = new GeoLocation();
		bestProviderLocation.setLatitude(bestProvider.getGeoLocation().split(",")[0]);
		bestProviderLocation.setLongitude(bestProvider.getGeoLocation().split(",")[1]);
		BigDecimal bestProviderDistance = startLocation.distanceFrom(bestProviderLocation);
		
		// filter ride providers with common destination so that it only contains
		// the ones passing through origin of new ride order
		rideProvidersWithCommonDestination.retainAll(rideProvidersWithRoutePassingThroughOrigin);
		
		for ( RideProvider rideProvider : rideProvidersWithCommonDestination) {
			GeoLocation providerLocation = new GeoLocation();
			providerLocation.setLatitude(rideProvider.getGeoLocation().split(",")[0]);
			providerLocation.setLongitude(rideProvider.getGeoLocation().split(",")[1]);
			BigDecimal distance = startLocation.distanceFrom(providerLocation);
			
			if (distance.compareTo(bestProviderDistance) == -1) {
				bestProviderDistance = distance;
				bestProvider = rideProvider;
			}

		}
		
		for ( RideProvider rideProvider : rideProvidersWithCurrentDestinationInRoute) {
			GeoLocation providerLocation = new GeoLocation();
			providerLocation.setLatitude(rideProvider.getGeoLocation().split(",")[0]);
			providerLocation.setLongitude(rideProvider.getGeoLocation().split(",")[1]);
			BigDecimal distance = startLocation.distanceFrom(providerLocation);
			
			if (distance.compareTo(bestProviderDistance) == -1) {
				bestProviderDistance = distance;
				bestProvider = rideProvider;
			}
		}

		return bestProvider;
	}
	
	private RideProvider nearestFreeRideProvider(GeoLocation start) {
		List<RideProvider> onlineRideProviders = rideProviderRepository.findByIsOnline(true);
		//no more than five miles away
		BigDecimal minDistance = BigDecimal.valueOf(5);
		RideProvider bestRideProvider = null;
		for (RideProvider provider : onlineRideProviders) {
			if (provider.getRidesInProgress().isEmpty()) {
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
	
	private List<RideProvider> allRideProvidersWithCommonDestination(String destinationZipCode) {
		List<RideProvider> eligibleRideProviders = new ArrayList<RideProvider>();
		ZipCode endZipCode = zipCodeRepository.findByCode(destinationZipCode);
		// surrounding zipcodes
		List<String> allowedEndZipCodes = endZipCode.getNeighbors().stream().map(x -> x.getCode()).collect(Collectors.toList());
		allowedEndZipCodes.add(endZipCode.getCode());
		List<RideProvider> onlineRideProviders = rideProviderRepository.findByIsOnline(true);
		for (RideProvider provider : onlineRideProviders) {
			if (provider.getRidesInProgress().size() > 0) {
				String providerPostCode = provider.getRidesInProgress().get(0).getEndLocation().getPostCode();
				if (allowedEndZipCodes.contains(providerPostCode)) {
					eligibleRideProviders.add(provider);
				}
			}
		}
		return eligibleRideProviders;
	}
	
	private List<RideProvider> allRideProvidersWithCurrentDestinationInRoute(String destinationZipCode) {
		List<RideProvider> eligibleRideProviders = new ArrayList<RideProvider>();
		// end zip code of new order
		ZipCode endZipCode = zipCodeRepository.findByCode(destinationZipCode);
		
		List<RideProvider> onlineRideProviders = rideProviderRepository.findByIsOnline(true);
		for (RideProvider provider : onlineRideProviders) {
			if (provider.getRidesInProgress().size() > 0) {
				String providerEndPostCodeString = provider.getRidesInProgress().get(0).getEndLocation().getPostCode();
				//destination zip code of first ride order
				ZipCode providerEndZipCode = zipCodeRepository.findByCode(providerEndPostCodeString);
				ZipCode providerCurrentZipCode = zipCodeRepository.findByCode(provider.getPostCode());
				if (routeService.findShortestRoute(providerCurrentZipCode, endZipCode).contains(providerEndZipCode)) {
					eligibleRideProviders.add(provider);
				}
			}
		}


		return eligibleRideProviders;
	}

	private List<RideProvider> allRideProvidersWithRoutePassingThroughOrigin(String originZipCode){
		List<RideProvider> eligibleRideProviders = new ArrayList<RideProvider>();
		// end zip code of new order
		ZipCode startZipCode = zipCodeRepository.findByCode(originZipCode);
		
		List<RideProvider> onlineRideProviders = rideProviderRepository.findByIsOnline(true);
		for (RideProvider provider : onlineRideProviders) {
			if (provider.getRidesInProgress().size() > 0) {
				String providerEndPostCodeString = provider.getRidesInProgress().get(0).getEndLocation().getPostCode();
				//destination zip code of first ride order
				ZipCode providerEndZipCode = zipCodeRepository.findByCode(providerEndPostCodeString);
				ZipCode providerCurrentZipCode = zipCodeRepository.findByCode(provider.getPostCode());
				if (routeService.findShortestRoute(providerCurrentZipCode, providerEndZipCode).contains(startZipCode)) {
					eligibleRideProviders.add(provider);
				}
			}
		}


		return eligibleRideProviders;

	}
}
