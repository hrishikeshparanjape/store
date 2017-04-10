package com.c.domain.location;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.c.controllers.orders.AddressRequest;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String line1;
	
	private String line2;
	
	private String line3;
	
	private String line4;
	
	private String city;
	
	private String postCode;
	
	private String state;
	
	private String country;
	
	private String locality;

	private Date createdAt;

	private Date updatedAt;
	
	public static final Address createPartialAddressFromAddressRequest(AddressRequest addressRequest) {
		Address ret = new Address();
		ret.setLine1(addressRequest.getLine1());
		ret.setLine2(addressRequest.getLine2());
		ret.setLine3(addressRequest.getLine3());
		ret.setLine4(addressRequest.getLine4());
		ret.setCity(addressRequest.getCity());
		ret.setState(addressRequest.getState());
		ret.setPostCode(addressRequest.getPostCode());
		ret.setCountry(addressRequest.getCountry());
		ret.setLocality(addressRequest.getLocality());
		return ret;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@PrePersist
	public void preCreate() {
		Calendar now = Calendar.getInstance();
		createdAt = now.getTime();
		updatedAt = now.getTime();
	}

	@PreUpdate
	public void preUpdate() {
		Calendar now = Calendar.getInstance();
		updatedAt = now.getTime();
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", line1=" + line1 + ", line2=" + line2 + ", line3=" + line3 + ", line4=" + line4
				+ ", city=" + city + ", postCode=" + postCode + ", state=" + state + ", country=" + country
				+ ", locality=" + locality + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
