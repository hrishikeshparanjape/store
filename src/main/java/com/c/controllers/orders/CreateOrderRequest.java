package com.c.controllers.orders;

import javax.validation.constraints.NotNull;

public class CreateOrderRequest {
	
	@NotNull
	private AddressRequest start;
	
	@NotNull
	private AddressRequest end;

	public AddressRequest getStart() {
		return start;
	}

	public void setStart(AddressRequest start) {
		this.start = start;
	}

	public AddressRequest getEnd() {
		return end;
	}

	public void setEnd(AddressRequest end) {
		this.end = end;
	}
	
}
