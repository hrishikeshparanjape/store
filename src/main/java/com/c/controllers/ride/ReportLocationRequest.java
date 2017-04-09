package com.c.controllers.ride;

import javax.validation.constraints.NotNull;

public class ReportLocationRequest {
	
	@NotNull
	private String geoLocation;

	private String postCode;

	@NotNull
	private Boolean online;
	
	public String getGeoLocation() {
		return geoLocation;
	}
	
	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public boolean getOnline() {
		return online;
	}

	public void setOnline(boolean isOnline) {
		this.online = isOnline;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
