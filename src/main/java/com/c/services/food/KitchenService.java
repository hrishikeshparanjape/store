package com.c.services.food;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.domain.food.FoodCategory;
import com.c.domain.food.FoodEvent;
import com.c.domain.food.FoodItem;
import com.c.domain.food.Kitchen;
import com.c.domain.location.Address;
import com.c.domain.user.Customer;
import com.c.repositories.AddressRepository;
import com.c.repositories.food.FoodEventRepository;
import com.c.repositories.food.FoodItemRepository;
import com.c.repositories.food.KitchenRepository;

@Service
public class KitchenService {
	
	@Autowired
	private KitchenRepository kitchenRepository;

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private FoodEventRepository foodEventRepository;
	
	@Autowired
	private FoodItemRepository foodItemRepository;

	public Kitchen createKitchen(Customer owner, Address address) {
		List<Kitchen> kitchens = kitchenRepository.findByOwnerId(owner.getId());
		for (Kitchen k : kitchens) {
			if (address.equals(k.getAddress())) {
				throw new ValidationException("Cannot create many kitchens at same address");
			}
		}
		Kitchen ret = new Kitchen();
		ret.setAddress(findOrCreateAddress(address.getLine1(), address.getPostCode(), address.getCountry()));
		ret.setOwner(owner);
		ret.setEvents(new HashSet<FoodEvent>());
		ret = kitchenRepository.save(ret);
		return ret;
	}

	public Address findOrCreateAddress(String line1, String postCode, String country) {
		Address ret = new Address();
		ret.setLine1(line1);
		ret.setPostCode(postCode);
		ret.setCountry(country);
		List<Address> addresses = addressRepository.findByLine1AndPostCodeAndCountry(line1, postCode, country);
		for (Address address : addresses) {
			if (address.equals(ret)) {
				return ret;
			}
		}
		ret = addressRepository.save(ret);
		return ret;
	}
	
	public FoodEvent createFoodEvent(Kitchen kitchen, Date startTime, Date endTime, Set<FoodItem> menuItems) {
		
		List<FoodEvent> conflictingEvents = foodEventRepository.findByKitchenAndEndsAtAfterAndStartsAtBefore(kitchen, startTime, endTime);
		if(conflictingEvents.size() == 0) {
			FoodEvent ret = new FoodEvent();
			ret.setKitchen(kitchen);
			ret.setStartsAt(startTime);
			ret.setEndsAt(endTime);
			ret.setMenuItems(menuItems);
			ret = foodEventRepository.save(ret);
			kitchen.getEvents().add(ret);
			kitchenRepository.save(kitchen);
			return ret;
		} else {
			throw new ValidationException("Cannot create many events at same kitchen at same time");
		}
	}
	
	public FoodItem createFoodItem(String name, Set<FoodCategory> categories) {
		
		FoodItem item = foodItemRepository.findByName(name);
		if(item == null) {
			item = new FoodItem();
			item.setCategories(categories);
			item = foodItemRepository.save(item);
			return item;
		} else {
			return item;
		}
	}

}
