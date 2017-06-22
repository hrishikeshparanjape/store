package com.c.repositories.food;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.food.FoodEvent;
import com.c.domain.food.Kitchen;

public interface FoodEventRepository extends CrudRepository<FoodEvent, Long> {
	List<FoodEvent> findByKitchenAndEndsAtAfterAndStartsAtBefore(Kitchen kitchen, Date startTime, Date endTime);
}
