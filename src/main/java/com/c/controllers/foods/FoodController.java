package com.c.controllers.foods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.c.domain.food.FoodEvent;
import com.c.repositories.food.FoodEventRepository;

@RestController
public class FoodController {
    
    @Autowired
    private FoodEventRepository foodEventRepository;
    
	@RequestMapping(path="/foods", method=RequestMethod.GET)
	public Page<FoodEvent> getFood(Pageable pageable) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return foodEventRepository.findByKitchenAddressCity("San Francisco", pageable);
	}
}
