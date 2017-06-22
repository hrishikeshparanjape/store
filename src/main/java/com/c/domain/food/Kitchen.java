package com.c.domain.food;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.c.domain.location.Address;
import com.c.domain.user.Customer;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Kitchen {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Address address;
	
	@ManyToOne
	private Customer owner;

	private Date createdAt;

	private Date updatedAt;
	
	@OneToMany
	@JsonManagedReference
	private Set<FoodEvent> events;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}
	
	public Set<FoodEvent> getEvents() {
		return events;
	}
	
	public void setEvents(Set<FoodEvent> events) {
		this.events = events;
	}
}
