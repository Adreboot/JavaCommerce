package com.yaps.petstore.authentication.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

public interface UserService {

	 public UserDTO createUser(@Valid final UserDTO userDTO) throws CreateException, FinderException;

	    public UserDTO findUser(final String userId) throws FinderException;

	    public void deleteUser(final String userId) throws RemoveException, FinderException;

	    public void updateUser(@Valid final UserDTO userDTO) throws UpdateException, FinderException;

	    public List<UserDTO> findUsers() throws FinderException;

		public List<UserDTO> findUsersByRole(String role) throws FinderException;


}
