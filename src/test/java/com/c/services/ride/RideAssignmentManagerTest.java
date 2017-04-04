package com.c.services.ride;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.c.exceptions.AddressValidationException;
import com.c.test.StoreTestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RideAssignmentManagerTest {
	
	@Autowired
	private RideAssignmentManager rideAssignmentManager;
	
	@Autowired
	private StoreTestUtils storeTestUtils;
	
	@Test
	public void testAssignServiceProviderToRideOrder() throws AddressValidationException {
	}
}
