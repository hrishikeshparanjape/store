package com.c.domain;

public class RideSubscription {

	private Customer customer;
	
	private Plan plan;
	
	private GeoLocation rideStartAddress;
	
	private GeoLocation rideEndAddress;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public GeoLocation getRideStartAddress() {
		return rideStartAddress;
	}

	public void setRideStartAddress(GeoLocation rideStartAddress) {
		this.rideStartAddress = rideStartAddress;
	}

	public GeoLocation getRideEndAddress() {
		return rideEndAddress;
	}

	public void setRideEndAddress(GeoLocation rideEndAddress) {
		this.rideEndAddress = rideEndAddress;
	}
}
