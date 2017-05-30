package com.c.services.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.controllers.orders.AddressRequest;
import com.c.domain.location.Address;
import com.c.domain.location.GeoLocation;
import com.c.domain.location.RideProvider;
import com.c.domain.order.RideOrder;
import com.c.domain.order.RideStatus;
import com.c.domain.user.Customer;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.AddressRepository;
import com.c.repositories.CustomerRepository;
import com.c.repositories.OrderRepository;
import com.c.repositories.RideProviderRepository;
import com.c.services.location.IAddressLookupService;
import com.c.services.ride.RideAssignmentManager;
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
	private AddressRepository addressRepository;
	
	@Autowired
	private RideProviderRepository rideProviderRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private IAddressLookupService addressLookupService;
	
	@Autowired
	private RideAssignmentManager rideAssignmentManager;
	
	public List<RideOrder> getOrdersByCustomer(String customerEmailAddress) {
		return orderRepository.findByCustomerEmail(customerEmailAddress);
	}

	public RideOrder createNewOrder(AddressRequest rideStartPoint, AddressRequest rideFinishPoint, String customerEmailAddress) throws AddressValidationException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		GeoLocation start = addressLookupService.getGeoLocationByAddress(rideStartPoint.getPostCode(), rideStartPoint.getLine1());
		GeoLocation end = addressLookupService.getGeoLocationByAddress(rideFinishPoint.getPostCode(), rideFinishPoint.getLine1());
		RideOrder order = new RideOrder();
		order.setGeoLocationStart(start.getLatitude() + "," + start.getLongitude());
		order.setGeoLocationEnd(end.getLatitude() + "," + end.getLongitude());
		
		String customerPaymentServiceId = getCustomerPaymentServiceId(customer);
		
		String productSku = getProductSku(start.distanceFrom(end));
		
		String orderPaymentServiceId = stripePaymentService.createNewOrder(customerPaymentServiceId, productSku, "usd");
		order.setPaymentServiceId(orderPaymentServiceId);
		order.setCustomer(customer);
		order.setStartLocation(getAddressEntityFromAddressRequest(rideStartPoint));
		order.setEndLocation(getAddressEntityFromAddressRequest(rideFinishPoint));
		order.setStatus(RideStatus.NEW);
		order = orderRepository.save(order);
		rideAssignmentManager.assignServiceProviderToRideOrder(order);
		return order;
	}
	
	public RideOrder getOrder(Long id) {
		return orderRepository.findOne(id);
	}
	
	public RideOrder getOrderPreview(AddressRequest rideStartPoint, AddressRequest rideFinishPoint, String customerEmailAddress) throws AddressValidationException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		GeoLocation start = addressLookupService.getGeoLocationByAddress(rideStartPoint.getPostCode(), rideStartPoint.getLine1());
		GeoLocation end = addressLookupService.getGeoLocationByAddress(rideFinishPoint.getPostCode(), rideFinishPoint.getLine1());
		RideOrder order = new RideOrder();
		order.setGeoLocationStart(start.getLatitude() + "," + start.getLongitude());
		order.setGeoLocationEnd(end.getLatitude() + "," + end.getLongitude());
		
		String productSku = getProductSku(start.distanceFrom(end));
		
		order.setPaymentServiceId(null);
		order.setCustomer(customer);
		order.setStartLocation(getAddressEntityFromAddressRequest(rideStartPoint));
		order.setEndLocation(getAddressEntityFromAddressRequest(rideFinishPoint));
		order.setStatus(RideStatus.NEW);
		order.setPrice(stripePaymentService.getProductPriceBySkuString(productSku));
		
		return order;
	}
	
	private Address getAddressEntityFromAddressRequest(AddressRequest addressRequest) {
		Address ret = Address.createPartialAddressFromAddressRequest(addressRequest);
		ret = addressRepository.save(ret);
		return ret;
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

	public void cancelOrder(String id, String customerEmailAddress) throws Exception {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		RideOrder orderToCancel = orderRepository.findOne(Long.valueOf(id));
		if (orderToCancel.getCustomer().getId() == customer.getId()) {
			if (orderToCancel.getStatus() == RideStatus.NEW || orderToCancel.getStatus() == RideStatus.ASSIGNED) {
				orderToCancel.setStatus(RideStatus.CANCELLED);
				orderRepository.save(orderToCancel);
				stripePaymentService.cancelOrder(orderToCancel.getPaymentServiceId());
			} else {
				throw new Exception("Cannot be cancelled");
			}
		} else {
			throw new Exception("Cannot cancel someone else's order");
		}
	}
	
	public void startOrder(String orderId, String providerEmail) throws Exception {
		RideProvider rideProvider = rideProviderRepository.findByCustomerEmail(providerEmail);
		RideOrder orderToStart = orderRepository.findOne(Long.valueOf(orderId));
		if (orderToStart.getServiceProvider().getId() == rideProvider.getId()) {
			if (orderToStart.getStatus() == RideStatus.ASSIGNED) {
				orderToStart.setStatus(RideStatus.STARTED);
				orderRepository.save(orderToStart);
				stripePaymentService.payOrder(orderToStart.getPaymentServiceId(), orderToStart.getCustomer().getPaymentServiceId());
			} else {
				throw new Exception("Cannot be started, wrong status");
			}
		} else {
			throw new Exception("Cannot be started, wrong ride provider");
		}
	}

	public void completeOrder(String orderId, String providerEmail) throws Exception {
		RideProvider rideProvider = rideProviderRepository.findByCustomerEmail(providerEmail);
		RideOrder orderToComplete = orderRepository.findOne(Long.valueOf(orderId));
		if (orderToComplete.getServiceProvider().getId() == rideProvider.getId()) {
			if (orderToComplete.getStatus() == RideStatus.STARTED) {
				orderToComplete.setStatus(RideStatus.COMPLETED);
				orderRepository.save(orderToComplete);
				stripePaymentService.fulfilOrder(orderToComplete.getPaymentServiceId());
			} else {
				throw new Exception("Cannot be started, wrong status");
			}
		} else {
			throw new Exception("Cannot be started, wrong ride provider");
		}
	}
}
