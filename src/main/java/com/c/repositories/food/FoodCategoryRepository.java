package com.c.repositories.food;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.food.FoodCategory;

public interface FoodCategoryRepository extends CrudRepository<FoodCategory, Long> {
}
