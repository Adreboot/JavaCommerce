package com.yaps.petstore.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.yaps.petstore.authentication.domain.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	
	public Iterable<User> findAllByRole(String role);

}
