package com.c.services.ride;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.c.domain.location.RideProvider;
import com.c.domain.order.RideOrder;
import com.c.exceptions.AddressValidationException;
import com.c.repositories.OrderRepository;
import com.c.repositories.RideProviderRepository;
import com.c.repositories.ZipCodeRepository;
import com.c.test.StoreTestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:fixtures/zip_code_fixtures.sql")
public class RideAssignmentManagerTest {
	
	@Autowired
	private RideAssignmentManager rideAssignmentManager;
	
	@Autowired
	private StoreTestUtils storeTestUtils;
	
	@Autowired
	private RideProviderRepository rideProviderRepository;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ZipCodeRepository zipCodeRepository;

	@Before
	public void beforeEachTest() {
		storeTestUtils.createTestRideProvider("37.792937, -122.404145", "94104");
		storeTestUtils.createTestRideProvider("37.773243, -122.501244", "94121");
		storeTestUtils.createTestRideProvider("37.788555, -122.443239", "94115");		
	}

	@After
	public void AfterEachTest() {
		Iterable<RideProvider> allRideProviders = rideProviderRepository.findAll();
		for (RideProvider r: allRideProviders) {
			r.getRidesInProgress().clear();
			rideProviderRepository.save(r);
		}
		orderRepository.deleteAll();
		rideProviderRepository.deleteAll();
		zipCodeRepository.deleteAll();
	}

	@Test
	public void testAssignServiceProviderToRideOrder_singleOrder() throws AddressValidationException, InterruptedException, ExecutionException {
		RideOrder testRideOrder = storeTestUtils.createTestRideOrder();
		Future<Void> testComplete = rideAssignmentManager.assignServiceProviderToRideOrder(testRideOrder);
		testComplete.get();
		Assert.assertNotEquals(null, testRideOrder.getServiceProvider());
	}
	
	@Test
	public void testAssignServiceProviderToRideOrder_multipleOrders() throws AddressValidationException, InterruptedException, ExecutionException {
		RideOrder testRideOrder1 = storeTestUtils.createTestRideOrder();
		RideOrder testRideOrder2 = storeTestUtils.createTestRideOrder();
		RideOrder testRideOrder3 = storeTestUtils.createTestRideOrder();
		Future<Void> testComplete1 = rideAssignmentManager.assignServiceProviderToRideOrder(testRideOrder1);
		testComplete1.get();		
		Future<Void> testComplete2 = rideAssignmentManager.assignServiceProviderToRideOrder(testRideOrder2);
		Future<Void> testComplete3 = rideAssignmentManager.assignServiceProviderToRideOrder(testRideOrder3);
		testComplete2.get();
		testComplete3.get();
		Assert.assertNotEquals(null, testRideOrder1.getServiceProvider());
		Assert.assertNotEquals(null, testRideOrder2.getServiceProvider());
		Assert.assertNotEquals(null, testRideOrder3.getServiceProvider());
	}
}
