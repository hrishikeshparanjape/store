package com.c.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.c.domain.location.ZipCode;

public interface ZipCodeRepository extends CrudRepository<ZipCode, Long> {
	public ZipCode findByCode(@Param("code") String code);
}
