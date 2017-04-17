package com.c.services.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.user.Customer;
import com.c.repositories.CustomerRepository;
import com.c.services.order.StripePaymentService;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

@Service
public class CreditCardService {
	
	@Autowired
	private StripePaymentService stripePaymentService;
	
	@Autowired
	private CustomerRepository customerRepository;

	public void addNewCreditCard(String customerEmailAddress, Map<String, String> cardData) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Customer customer = customerRepository.findByEmail(customerEmailAddress);
		String customerPaymentServiceId = getCustomerPaymentServiceId(customer);	
		stripePaymentService.createNewCard(customerPaymentServiceId,
				cardData.get("expiryMonth"),
				cardData.get("expiryYear"),
				cardData.get("number"),
				cardData.get("cvc"),
				cardData.get("name"));
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
