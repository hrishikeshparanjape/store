package com.c.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.Customer;
import com.c.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer signupOrSignInCustomer(String email) {
		Customer customer = customerRepository.findByEmail(email);
		if (customer == null) {
			customer = new Customer();
			customer.setEmail(email);
			customerRepository.save(customer);
		}
		return customer;
	}
}
