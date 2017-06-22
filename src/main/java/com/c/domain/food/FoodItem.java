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

@Entity
public class FoodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@OneToMany
	private Set<FoodCategory> categories;
	
	private Date createdAt;

	private Date updatedAt;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<FoodCategory> getCategories() {
		return categories;
	}
	
	public void setCategories(Set<FoodCategory> categories) {
		this.categories = categories;
	}
}
