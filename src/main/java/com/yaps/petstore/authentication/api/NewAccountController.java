package com.yaps.petstore.authentication.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.service.KeycloakService;
import com.yaps.petstore.authentication.domain.service.UserService;
import com.yaps.petstore.domain.constant.AmericanStates;
import com.yaps.petstore.domain.constant.Countries;
import com.yaps.petstore.domain.constant.CreditCardTypes;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.DuplicateKeyException;

@Controller
public class NewAccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewAccountController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	KeycloakService keycloakService;
	
	@GetMapping(path = "/new-account")
	public String newAccount(Model model) {
		final String mname = "newAccount";
		model.addAttribute("states", AmericanStates.getAll());
		model.addAttribute("countries", Countries.getAll());
		model.addAttribute("ccTypes", CreditCardTypes.getAll());
		LOGGER.debug("entering "+mname);
		
		model.addAttribute("userDTO", new UserDTO());
		return "new-account";
	}

	@PostMapping(path = "/new-account")
	public String createAccount(@Valid UserDTO userDTO, BindingResult result, Model model) {
		final String mname = "createAccount";
		LOGGER.debug("entering "+mname);
		 if (result.hasErrors()) {
	         return "new-account";
	     }
		HttpStatus responseStatus;
		try {
		responseStatus = keycloakService.createKeycloakUser(userDTO);
		} catch (WebClientResponseException e) {
			model.addAttribute("exception","User could not be created. "+e.getMessage());	
			return "error";
		}
		System.err.println(responseStatus);
		if(responseStatus == HttpStatus.CREATED) {
			try {
				userService.createUser(userDTO);
				model.addAttribute("message","account created");
				return "index";
			} catch (CreateException e) {
				// on détruit le compte dans keycloak si le compte ne peut être créé localement (prb de cc ?)
				keycloakService.deleteKeycloakUser(userDTO.getUsername());
				if(e instanceof DuplicateKeyException)
					model.addAttribute("exception", "this id is already assigned");
				else
					model.addAttribute("exception", e.getMessage());
				return "error";
			} catch(Exception exc) {
				keycloakService.deleteKeycloakUser(userDTO.getUsername());
				model.addAttribute("exception", exc.getMessage());
				return "error";
			}
		} else {
		model.addAttribute("exception","User could not be created. HTTPStatus : "+responseStatus);	
		return "error";
		}
	}
}
