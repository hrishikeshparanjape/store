package com.c.repositories;

import org.springframework.data.repository.CrudRepository;

import com.c.domain.location.ZipCode;

public interface ZipCodeRepository extends CrudRepository<ZipCode, Long> {
}
