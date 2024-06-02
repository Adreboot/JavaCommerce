package com.yaps.petstore.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.service.UserService;
import com.yaps.petstore.exception.FinderException;

/**
 * This servlet displays user according to their role.
 */
@Controller
public class FindUserController  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindUserController.class);
    
    @Autowired
	private UserService userService;
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/display-franchisees")
    public String showFranchisees(Model model) {
    	final String mname = "showFranchisees";
		LOGGER.debug("entering "+mname);
		
		List<UserDTO> franchiseeDTOs = null;
    	try {
    		franchiseeDTOs = userService.findUsersByRole("ROLE_FRANCHISEE");
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
    	model.addAttribute("franchiseeDTOs", franchiseeDTOs);
    	return "display-users";
    }
    
    @Secured({"ROLE_ADMIN","ROLE_FRANCHISEE"})
    @GetMapping("/display-customers")
    public String showCustomers(Model model) {
    	final String mname = "showCustomers";
		LOGGER.debug("entering "+mname);
		
		List<UserDTO> customerDTOs = null;
    	try {
    		customerDTOs = userService.findUsersByRole("ROLE_USER");
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
        model.addAttribute("customerDTOs", customerDTOs);
    	return "display-users";
    }  
}