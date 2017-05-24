package com.c.controllers;

import javax.validation.constraints.NotNull;

public class FacebookPreAuthRequest {

	@NotNull
	private String token;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
