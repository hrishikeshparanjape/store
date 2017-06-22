package com.c.repositories.food;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.food.Kitchen;

public interface KitchenRepository extends CrudRepository<Kitchen, Long> {
	List<Kitchen> findByOwnerId(Long id);
}
