package com.c.repositories.food;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.food.FoodItem;

public interface FoodItemRepository extends CrudRepository<FoodItem, Long> {
	
	public FoodItem findByName(String name);
}
