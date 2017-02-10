package com.c.services.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Order;

@Service
public class StripePaymentService {
	
	private static final String stripeApiKey = "sk_test_1AoxeYeY8XmwrBNhgZoMY5wq";
	
	public String createNewCustomer(String email) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = stripeApiKey;
		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("email", email);
		Customer c = Customer.create(customerParams);
		return c.getId();
	}

	public String createNewCard(String customerPaymentServiceId,
			String expiryMonth,
			String expiryYear,
			String number,
			String cvc,
			String name) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = stripeApiKey;
		Customer customer = Customer.retrieve(customerPaymentServiceId);
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String> cardParams = new HashMap<String, String>();
		cardParams.put("object", "card");
		cardParams.put("exp_month", expiryMonth);
		cardParams.put("exp_year", expiryYear);
		cardParams.put("number", number);
		cardParams.put("cvc", cvc);
		cardParams.put("name", name);
		
		params.put("source", cardParams);
		ExternalAccount ret = customer.getSources().create(params);
		return ret.getId();
	}
	
	public String createNewOrder(String customerPaymentServiceId, String sku, String currency) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = stripeApiKey;

		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("currency", currency);
		Map<String, String> itemsParams = new HashMap<String, String>();
		itemsParams.put("type", "sku");
		itemsParams.put("parent", sku);
		List<Map<String, String>> itemsList = new ArrayList<Map<String, String>>();
		itemsList.add(itemsParams);
		orderParams.put("items", itemsList);
		orderParams.put("customer", customerPaymentServiceId);
		
		Order ret = Order.create(orderParams);
		return ret.getId();
	}
	
	public String payOrder(String orderPaymentServiceId, String customerPaymentServiceId) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = stripeApiKey;

		Order order = Order.retrieve(orderPaymentServiceId);
		Map<String, Object> orderPayParams = new HashMap<String, Object>();
		orderPayParams.put("customer", customerPaymentServiceId);
		Order ret = order.pay(orderPayParams);
		return ret.getId();
	}

}
