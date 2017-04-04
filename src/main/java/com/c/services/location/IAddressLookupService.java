package com.c.services.location;

import com.c.domain.location.GeoLocation;
import com.c.exceptions.AddressValidationException;

public interface IAddressLookupService {
	public GeoLocation getGeoLocationByAddress(String postCode, String addressLine1) throws AddressValidationException;
}
