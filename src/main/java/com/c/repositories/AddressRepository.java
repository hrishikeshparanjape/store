package com.c.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.location.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
	List<Address> findByLine1AndPostCodeAndCountry(String line1, String postCode, String country);
}
