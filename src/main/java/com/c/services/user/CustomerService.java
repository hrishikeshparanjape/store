package com.c.services.user;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.user.Customer;
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
			customer.setRoles("ROLE_USER");
			customerRepository.save(customer);
		}
		return customer;
	}
	
	public Customer markCustomerAsDriver(String email) throws EntityNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		if (customer != null) {
			String roles = customer.getRoles();
			roles = roles + "," + "ROLE_DRIVER";
			customer.setRoles(roles);
			customer = customerRepository.save(customer);
			return customer;
		} else {
			throw new EntityNotFoundException("customer record not found: email=" + email);
		}
		
	}
}
