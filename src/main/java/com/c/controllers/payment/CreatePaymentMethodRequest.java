package com.c.controllers.payment;

import java.util.Map;

public class CreatePaymentMethodRequest {
	
	private String type;
	
	private Map<String, String> data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
