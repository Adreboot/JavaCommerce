package com.yaps.petstore.authentication.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.service.KeycloakService;
import com.yaps.petstore.authentication.domain.service.UserService;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

@Controller
public class AuthenticationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private KeycloakService keycloakService;

	@GetMapping(path = "/login")
	public String logWithKeycloak(Model model) {
		final String mname = "logWithKeycloak";
		LOGGER.debug("entering / exiting" + mname);
		return "index";
	}

	@GetMapping(path = "/update-account/{username}")
	public String showAccount(Model model, @PathVariable String username) {
		final String mname = "showAccount";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		try {
			userDTO = userService.findUser(username);
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		LOGGER.debug("exiting " + mname);
		return "update-account";
	}

	@PostMapping(path = "/update-account")
	public String updateAccount(@Valid @ModelAttribute UserDTO userDTO, BindingResult result, Model model) {
		final String mname = "updateAccount";
		LOGGER.debug("entering " + mname);
		if (result.hasErrors()) {
			return "update-account";
		}
		HttpStatus responseStatus;
		try {
			responseStatus = keycloakService.updateKeycloakUser(userDTO);
		} catch (WebClientResponseException e) {
			model.addAttribute("exception", "User could not be updated. " + e.getMessage());
			return "error";
		}
		if (responseStatus == HttpStatus.NO_CONTENT) {
			try {
				userService.updateUser(userDTO);
				model.addAttribute("message", "account updated");
				return "index";
			} catch (UpdateException e) {
				model.addAttribute("exception", e.getMessage());
				return "error";
			} catch (Exception exc) {
				model.addAttribute("exception", exc.getMessage());
				return "error";
			}
		} else {
			model.addAttribute("exception", "User could not be updated. HTTPStatus : " + responseStatus);
			return "error";
		}
	}

	@GetMapping(path = "/delete-account/{username}")
	public String deleteAccount(Model model, @PathVariable String username, HttpServletRequest request) throws ServletException {
		final String mname = "deleteAccount";
		LOGGER.debug("entering " + mname);
		HttpStatus responseStatus;
		try {
			responseStatus = keycloakService.deleteKeycloakUser(username);
		} catch (WebClientResponseException e) {
			model.addAttribute("exception", "User could not be deleted. " + e.getMessage());
			return "error";
		}
		if (responseStatus == HttpStatus.NO_CONTENT) {
			try {
				userService.deleteUser(username);
			} catch (RemoveException | FinderException e) {
				model.addAttribute("exception", e.getMessage());
				return "error";
			}
		} else {
			model.addAttribute("exception", "User could not be deleted. HTTPStatus : " + responseStatus);
			return "error";
		}
		request.logout();
		LOGGER.debug("exiting " + mname);
		return "redirect:/";
	}	
	
}
