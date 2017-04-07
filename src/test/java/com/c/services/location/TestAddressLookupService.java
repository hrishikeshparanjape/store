package com.c.services.location;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.c.domain.location.GeoLocation;
import com.c.exceptions.AddressValidationException;

@Service
@Profile("test")
public class TestAddressLookupService implements IAddressLookupService {
	
	private Map<String, String> testGeoLocations = new HashMap<String, String>();
	
	@PostConstruct
	public void init() {
		testGeoLocations.put("580 california street", "37.792937, -122.404145");
		testGeoLocations.put("818 41st Ave", "37.773243, -122.501244");
		testGeoLocations.put("3057 Sacramento St", "37.788555, -122.443239");
		testGeoLocations.put("53 Funston Ave", "37.797559, -122.457470");
		testGeoLocations.put("1095 Hyde St", "37.790740, -122.418059");
		testGeoLocations.put("24 Willie Mays Plaza", "37.778857, -122.390234");
		testGeoLocations.put("3950 24th St", "37.752165, -122.431213");
		testGeoLocations.put("4001 Judah St", "37.760110, -122.505022");
		testGeoLocations.put("1128 Taraval St", "37.743374, -122.478118");
		testGeoLocations.put("2814 19th St", "37.760577, -122.410688");
		testGeoLocations.put("2495 3rd St", "37.758394, -122.388634");
		testGeoLocations.put("2815 Diamond St", "37.733950, -122.433292");
		testGeoLocations.put("2036 Lombard St", "37.800156, -122.435685");
	}

	@Override
	public GeoLocation getGeoLocationByAddress(String postCode, String addressLine1) throws AddressValidationException {		
		return new GeoLocation(testGeoLocations.get(addressLine1));
	}

}
