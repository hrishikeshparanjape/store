package com.c.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.location.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
