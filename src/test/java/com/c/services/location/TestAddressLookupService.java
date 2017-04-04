package com.c.services.location;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.c.domain.location.GeoLocation;
import com.c.exceptions.AddressValidationException;

@Service
@Profile("test")
public class TestAddressLookupService implements IAddressLookupService {

	@PostConstruct
	public void init() throws IOException {
		System.out.println("Initializing TestAddressLookupService");
	}
	
	@Override
	public GeoLocation getGeoLocationByAddress(String postCode, String addressLine1) throws AddressValidationException {
		// TODO Auto-generated method stub
		return null;
	}

}
