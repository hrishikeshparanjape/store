package com.c.domain.location;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.c.domain.order.RideOrder;
import com.c.domain.user.Customer;

@Entity
public class RideProvider {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date createdAt;

	private Date updatedAt;

	private String geoLocation;
	
	private String postCode;
	
	@OneToMany
	private List<RideOrder> ridesInProgress;

	@OneToOne
	private Customer customer;

	private boolean isOnline;
	
	private String city;
	
	private BigInteger capacity;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigInteger getCapacity() {
		return capacity;
	}

	public void setCapacity(BigInteger capacity) {
		this.capacity = capacity;
	}
	
	public List<RideOrder> getRidesInProgress() {
		return ridesInProgress;
	}
	
	public void setRidesInProgress(List<RideOrder> ridesInProgress) {
		this.ridesInProgress = ridesInProgress;
	}
	
	public String getPostCode() {
		return postCode;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
