package com.c.services.ride;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
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
	
	@Test
	public void testAssignServiceProviderToRideOrder() throws AddressValidationException, InterruptedException, ExecutionException {
		storeTestUtils.createTestRideProvider("37.792937, -122.404145", "94104");
		storeTestUtils.createTestRideProvider("37.773243, -122.501244", "94121");
		storeTestUtils.createTestRideProvider("37.788555, -122.443239", "94115");
		RideOrder testRideOrder = storeTestUtils.createTestRideOrder();
		Future<Void> testComplete = rideAssignmentManager.assignServiceProviderToRideOrder(testRideOrder);
		testComplete.get();
		Assert.assertNotEquals(null, testRideOrder.getServiceProvider());
	}
}
