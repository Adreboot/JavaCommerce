package com.yaps.petstore.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.domain.model.Category;
import com.yaps.petstore.domain.model.Product;

public interface ProductRepository extends CrudRepository<Product, String> {

	Iterable<Product> findAllByCategory(Category category);

}
