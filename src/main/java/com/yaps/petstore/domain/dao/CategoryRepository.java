package com.yaps.petstore.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.domain.model.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

}
