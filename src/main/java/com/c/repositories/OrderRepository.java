package com.c.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.order.RideOrder;

public interface OrderRepository extends CrudRepository<RideOrder, Long> {
	List<RideOrder> findByCustomerEmail(String email);
}
