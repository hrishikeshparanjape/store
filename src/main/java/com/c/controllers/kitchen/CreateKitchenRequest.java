package com.c.controllers.kitchen;

import javax.validation.constraints.NotNull;

public class CreateKitchenRequest {
	
	@NotNull
	private AddressRequest location;
	
	public AddressRequest getLocation() {
		return location;
	}
	
	public void setLocation(AddressRequest location) {
		this.location = location;
	}
}
