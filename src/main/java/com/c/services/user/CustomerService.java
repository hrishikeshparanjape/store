package com.c.services.user;

import java.util.Locale;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.user.Customer;
import com.c.repositories.CustomerRepository;
import com.c.services.email.EmailService;

@Service
public class CustomerService {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Customer signupOrSignInCustomer(String email) {
		Customer customer = customerRepository.findByEmail(email);
		if (customer == null) {
			customer = new Customer();
			customer.setEmail(email);
			customer.setRoles("ROLE_USER");
			customerRepository.save(customer);
			try {
				emailService.sendWelcomeEmail(email, email, Locale.ENGLISH);
			} catch (Exception e) {
				log.error("Error sending email: email=" + email + ", exception=", e);
			}
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
