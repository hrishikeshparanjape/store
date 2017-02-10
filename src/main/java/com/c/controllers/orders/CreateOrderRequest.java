package com.c.controllers.orders;

import javax.validation.constraints.NotNull;

import com.c.domain.location.Address;

public class CreateOrderRequest {
	
	@NotNull
	private Address start;
	
	@NotNull
	private Address end;

	public Address getStart() {
		return start;
	}

	public void setStart(Address start) {
		this.start = start;
	}

	public Address getEnd() {
		return end;
	}

	public void setEnd(Address end) {
		this.end = end;
	}
	
}
