package com.c.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.user.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	public Customer findByEmail(String email);
}
