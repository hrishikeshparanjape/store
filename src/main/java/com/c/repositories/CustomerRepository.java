package com.c.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

}
