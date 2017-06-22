package com.c.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.c.domain.order.RideOrder;

@RepositoryRestResource(exported = false)
public interface OrderRepository extends CrudRepository<RideOrder, Long> {
	List<RideOrder> findByCustomerEmail(String email);
}
