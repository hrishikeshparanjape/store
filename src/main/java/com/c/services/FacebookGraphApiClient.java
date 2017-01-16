package com.c.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class FacebookGraphApiClient {

	private static final String facebookBaseUrl = "https://graph.facebook.com/v2.8/me?fields=email&access_token=";

	@Autowired
	private RestOperations restTemplate;

	public String getEmailAddressByAccessToken(String accessToken) {
		ResponseEntity<String> fbResponse = restTemplate.getForEntity(facebookBaseUrl + accessToken, String.class);
		JSONObject response = new JSONObject(fbResponse.getBody());
		return response.getString("email");
	}
}
