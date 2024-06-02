package com.yaps.petstore.authentication.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.yaps.petstore.authentication.domain.dao.UserRepository;
import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.model.User;
import com.yaps.petstore.domain.service.CreditCardService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class UserServiceImpl implements UserService {

	// ======================================
	// = Attributes =
	// ======================================

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private ModelMapper commonModelMapper, userDTOModelMapper;

	// ======================================
	// = Constructors =
	// ======================================
	public UserServiceImpl() {	}

	// ======================================
	// = Business methods =
	// ======================================
	@Override
	@Transactional
	public UserDTO createUser(@Valid UserDTO userDTO) throws CreateException, FinderException {
		final String mname = "createUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new CreateException("User object is null");

		try {
			checkId(userDTO.getUsername());
		} catch (FinderException e) {
			throw new CreateException("id is invalid");
		}

		if(userRepository.findById(userDTO.getUsername()).isPresent())
			throw new DuplicateKeyException();

		// Credit Card Check
		if (userDTO.getCreditCardNumber() != null && userDTO.getCreditCardExpiryDate() != null
				&& !userDTO.getCreditCardNumber().equals("") && !userDTO.getCreditCardExpiryDate().equals(""))
			creditCardService.verifyCreditCard(userDTO.getCreditCardDTO());

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = userDTOModelMapper.map(userDTO, User.class);
		
		if (user.getRole() == null ) {
				user.setRole("ROLE_USER");
		}
		// Creates the object
		userRepository.save(user);

		UserDTO result = commonModelMapper.map(user, UserDTO.class);

		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUser(final String username) throws FinderException {
		final String mname = "findUser";
		LOGGER.debug("entering " + mname + " with " + username);

		checkId(username);

		User user = userRepository.findById(username).orElseThrow(() -> new FinderException("User must exist to be found"));

		UserDTO result = commonModelMapper.map(user, UserDTO.class);
		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional
	public void deleteUser(final String username) throws RemoveException, FinderException {
		final String mname = "deleteUser";
		LOGGER.debug("entering " + mname + " with " + username);
		
		checkId(username);
		
		User user = userRepository.findById(username).orElseThrow(() -> new RemoveException("User must exist to be deleted"));

		userRepository.delete(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateUser(@Valid UserDTO userDTO) throws UpdateException, FinderException {
		final String mname = "updateUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new UpdateException("User object is null");

		// Checks if the object exists
		if (userRepository.findById(userDTO.getUsername()).isEmpty())
			throw new UpdateException("User must exist to be updated");

		// Credit Card Check
		if (userDTO.getCreditCardNumber() != null && userDTO.getCreditCardExpiryDate() != null
				&& !userDTO.getCreditCardNumber().equals("") && !userDTO.getCreditCardExpiryDate().equals(""))
			creditCardService.verifyCreditCard(userDTO.getCreditCardDTO());

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = commonModelMapper.map(userDTO, User.class);
		// Role must be set
		User userToUpdate = userRepository.findById(userDTO.getUsername()).get();
		user.setRole(userToUpdate.getRole());

		// Updates the object
		userRepository.save(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsers() throws FinderException {
		final String mname = "findUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> users = userRepository.findAll();
		int size;
		if ((size = ((Collection<User>) users).size()) == 0) {
			throw new FinderException("No user in the database");
		}
		List<UserDTO> usersDTO = ((List<User>) users)
								.stream()
								.map(user -> commonModelMapper.map(user, UserDTO.class))
								.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsersByRole(final String role) throws FinderException {
		final String mname = "findUsersByRole";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> usersByRole = userRepository.findAllByRole(role);
		int size;
		
		if ((size = ((Collection<User>) usersByRole).size()) == 0) {
			throw new FinderException("No user in the database for role " + role);
		}
		List<UserDTO> usersDTOByRole = ((List<User>) usersByRole)
										.stream()
										.map(user -> commonModelMapper.map(user, UserDTO.class))
										.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTOByRole;
	}

	// ======================================
	// = Private Methods =
	// ======================================

	private void checkId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException("Id should not be null or empty");
	}

}
