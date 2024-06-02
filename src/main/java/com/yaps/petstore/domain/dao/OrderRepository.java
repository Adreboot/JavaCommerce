package com.yaps.petstore.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.domain.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
	
	@Query("select MAX(id) from Order o")
	public Optional<Long> findLastId();	

}
