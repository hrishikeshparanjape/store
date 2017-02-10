package com.c.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.order.RideOrder;

public interface OrderRepository extends CrudRepository<RideOrder, Long> {
}
