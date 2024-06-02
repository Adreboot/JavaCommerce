package com.yaps.petstore.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.domain.model.Counter;

public interface CounterRepository extends CrudRepository<Counter, String> {

}
