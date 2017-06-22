package com.c.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.c.domain.location.RideProvider;

@RepositoryRestResource(exported = false)
public interface RideProviderRepository extends CrudRepository<RideProvider, Long> {
	public List<RideProvider> findByCity(String city);
	public RideProvider findByCustomerEmail(String email);
	public List<RideProvider> findByIsOnline(boolean isOnline);
}
