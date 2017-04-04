package com.c.test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.c.domain.location.Address;
import com.c.domain.order.RideOrder;
import com.c.domain.user.Customer;

@Component
public class StoreTestUtils {
	
	@Autowired
	private ResourceLoader resourceLoader;

	private SecureRandom random = new SecureRandom();
	
	Set<Address> sampleSanFranciscoAddresses = new HashSet<Address>();
	
	@PostConstruct
	public void init() throws IOException {
		resourceLoader.getResource("file:fixtures/sample_san_francisco_addresses.json").getFile();
	}

	public Customer createTestCustomer() {
		Customer ret = new Customer();
		ret.setEmail(randomAlphaNumericString() + "@test.com");
		ret.setPaymentServiceId(randomAlphaNumericString());
		ret.setRoles("ROLE_USER");
		return ret;
	}

	public RideOrder createTestRideOrder() {
		RideOrder ret = new RideOrder();
		ret.setCustomer(createTestCustomer());
		ret.setPaymentServiceId(randomAlphaNumericString());
		return ret;
	}

	public String randomAlphaNumericString() {
		return new BigInteger(130, random).toString(32);
	}
}
