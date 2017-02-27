package com.c.controllers.ride;

import javax.validation.constraints.NotNull;

public class ReportLocationRequest {
	
	@NotNull
	private String geoLocation;
	
	@NotNull
	private Boolean isOnline;
	
	public String getGeoLocation() {
		return geoLocation;
	}
	
	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}	
}
