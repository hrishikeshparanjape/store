package com.c.test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.c.controllers.orders.AddressRequest;
import com.c.domain.location.Address;
import com.c.domain.location.RideProvider;
import com.c.domain.order.RideOrder;
import com.c.domain.order.RideStatus;
import com.c.domain.user.Customer;
import com.c.repositories.AddressRepository;
import com.c.repositories.CustomerRepository;
import com.c.repositories.OrderRepository;
import com.c.repositories.RideProviderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StoreTestUtils {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RideProviderRepository rideProviderRepository;

	@Autowired
	private AddressRepository addressRepository;

	private SecureRandom random = new SecureRandom();
	
	List<AddressRequest> sampleSanFranciscoAddresses;
	
	@PostConstruct
	public void init() throws IOException {
		File sampleSanFranciscoAddressesJsonFile = resourceLoader.getResource("classpath:fixtures/sample_san_francisco_addresses.json").getFile();
		byte[] encoded = Files.readAllBytes(sampleSanFranciscoAddressesJsonFile.toPath());
		String sampleSanFranciscoAddressesJson = new String(encoded, Charset.defaultCharset());
		AddressRequest[] sampleAddresses = (new ObjectMapper()).readValue(sampleSanFranciscoAddressesJson, AddressRequest[].class);
		sampleSanFranciscoAddresses = Arrays.asList(sampleAddresses);
	}

	public AddressRequest createRandomSanFranciscoAddressRequest() {
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(sampleSanFranciscoAddresses.size());
		return sampleSanFranciscoAddresses.get(index);
	}

	public Customer createTestCustomer() {
		Customer ret = new Customer();
		ret.setEmail(randomAlphaNumericString() + "@test.com");
		ret.setPaymentServiceId(randomAlphaNumericString());
		ret.setRoles("ROLE_USER");
		ret = customerRepository.save(ret);
		return ret;
	}
	
	public Address createTestAddress() {
		Address ret = Address.createPartialAddressFromAddressRequest(createRandomSanFranciscoAddressRequest());
		ret = addressRepository.save(ret);
		return ret;
	}

	public RideOrder createTestRideOrder() {
		RideOrder ret = new RideOrder();
		ret.setCustomer(createTestCustomer());
		ret.setPaymentServiceId(randomAlphaNumericString());
		ret.setStartLocation(createTestAddress());
		ret.setEndLocation(createTestAddress());
		ret.setStatus(RideStatus.NEW);
		ret = orderRepository.save(ret);
		return ret;
	}
	
	public RideProvider createTestRideProvider(String geoLocation, String zipCode) {
		RideProvider ret = new RideProvider();
		ret.setCapacity(new BigInteger("3"));
		ret.setOnline(true);
		ret.setCustomer(createTestCustomer());
		ret.setRidesInProgress(new ArrayList<RideOrder>());
		ret.setPostCode(zipCode);
		ret.setGeoLocation(geoLocation);
		ret = rideProviderRepository.save(ret);
		return ret;
	}

	public String randomAlphaNumericString() {
		return new BigInteger(130, random).toString(32);
	}
}
