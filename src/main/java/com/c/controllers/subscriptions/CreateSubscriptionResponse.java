package com.c.controllers.subscriptions;

import com.c.domain.RideSubscription;

public class CreateSubscriptionResponse {
	
	private RideSubscription subscription;

	public RideSubscription getSubscription() {
		return subscription;
	}

	public void setSubscription(RideSubscription subscription) {
		this.subscription = subscription;
	}
}
