package com.yaps.petstore.domain.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.domain.model.Order;
import com.yaps.petstore.domain.model.OrderLine;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {

	Collection<OrderLine> findAllByOrder(Order order);
	
	@Query("select MAX(id) from OrderLine o")
	public Optional<Long> findLastId();	

}
