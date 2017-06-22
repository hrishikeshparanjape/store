package com.c.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.c.domain.location.ZipCode;

@RepositoryRestResource(exported = false)
public interface ZipCodeRepository extends CrudRepository<ZipCode, Long> {
	public ZipCode findByCode(@Param("code") String code);
}
