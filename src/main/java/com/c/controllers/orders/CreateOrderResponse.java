package com.c.controllers.orders;

import com.c.domain.order.RideOrder;

public class CreateOrderResponse {
	
	private RideOrder order;

	public RideOrder getOrder() {
		return order;
	}

	public void setOrder(RideOrder order) {
		this.order = order;
	}
}
