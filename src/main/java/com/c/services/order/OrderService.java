package com.c.services.order;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.location.Address;
import com.c.domain.location.GeoLocation;
import com.c.domain.order.RideOrder;
import com.c.domain.user.Customer;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.CustomerRepository;
import com.c.repositories.OrderRepository;
import com.c.services.location.AddressLookupService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

@Service
public class OrderService {
	
	@Autowired
	private StripePaymentService stripePaymentService;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AddressLookupService addressLookupService;

	public RideOrder createNewOrder(Address rideStartPoint, Address rideFinishPoint, String customerEmailAddress) throws AddressValidationException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		GeoLocation start = addressLookupService.getGeoLocationByAddress(rideStartPoint);
		GeoLocation end = addressLookupService.getGeoLocationByAddress(rideFinishPoint);
		RideOrder order = new RideOrder();
		
		String customerPaymentServiceId = getCustomerPaymentServiceId(customer);
		
		String productSku = getProductSku(start.distanceFrom(end));
		
		String orderPaymentServiceId = stripePaymentService.createNewOrder(customerPaymentServiceId, productSku, "usd");
		order.setPaymentServiceId(orderPaymentServiceId);
		order.setCustomer(customer);
		order = orderRepository.save(order);
		return order;
	}	
	
	private String getProductSku(BigDecimal rideDistance) {
		rideDistance = rideDistance.setScale(0, RoundingMode.UP);
		int distance = rideDistance.intValueExact();
		String ret;
		switch (distance) {
		case 1:
			ret = "one-mile-ride-sku";
			break;
		case 2:
			ret = "two-mile-ride-sku";
			break;
		case 3:
			ret = "three-mile-ride-sku";
			break;
		default:
			ret = "three-mile-ride-sku";
		}
		return ret;
	}

	private String getCustomerPaymentServiceId(Customer customer) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		if (customer.getPaymentServiceId() == null) {
			String customerPaymentServiceId = stripePaymentService.createNewCustomer(customer.getEmail());
			customer.setPaymentServiceId(customerPaymentServiceId);
			customerRepository.save(customer);
		}
		return customer.getPaymentServiceId();
	}
}
