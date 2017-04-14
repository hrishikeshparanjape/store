package com.c.domain.location;

import org.junit.Assert;
import org.junit.Test;

public class GeoLocationTests {
	
	@Test
	public void testDistanceFrom() {
		GeoLocation testSrc = new GeoLocation("37.788555, -122.443239");
		GeoLocation testDest = new GeoLocation("37.788555, -122.443239");
		Assert.assertNotNull(testSrc.distanceFrom(testDest));
	}
}
