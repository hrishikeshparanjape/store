package com.c.domain.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Column(unique=true)
    @Email
    private String email;

    @Column(unique=true)
    private String paymentServiceId;
    
    private String roles;

    private Date createdAt;
    
    private Date updatedAt;
    
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public String getPaymentServiceId() {
		return paymentServiceId;
	}
	
	public void setPaymentServiceId(String paymentServiceId) {
		this.paymentServiceId = paymentServiceId;
	}
	
	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Set<CustomerRole> getCustomerRoles() {
		Set<CustomerRole> ret = new HashSet<CustomerRole>();
		String[] allRoles = roles.split(",");
		for (int i=0; i< allRoles.length; i++) {
			ret.add(CustomerRole.valueOf(allRoles[i]));
		}
		return ret;
	}

	public void addCustomerRole(CustomerRole role) {
		roles = roles + "," + role.toString();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", paymentServiceId=" + paymentServiceId + ", roles=" + roles
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
