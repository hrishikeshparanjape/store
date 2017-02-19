package com.c.domain.order;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.c.domain.location.Address;
import com.c.domain.user.Customer;

@Entity
public class RideOrder {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(unique=true)
    private String paymentServiceId;

    private Date createdAt;
    
    private Date updatedAt;
    
    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Customer serviceProvider;
    
    @ManyToOne
    private Address startLocation;
    
    @ManyToOne
    private Address endLocation;

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

	public String getPaymentServiceId() {
		return paymentServiceId;
	}

	public void setPaymentServiceId(String paymentServiceId) {
		this.paymentServiceId = paymentServiceId;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Customer getServiceProvider() {
		return serviceProvider;
	}
	
	public void setServiceProvider(Customer serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Address getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Address startLocation) {
		this.startLocation = startLocation;
	}

	public Address getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Address endLocation) {
		this.endLocation = endLocation;
	}
}
