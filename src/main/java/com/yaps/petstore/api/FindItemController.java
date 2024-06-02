package com.yaps.petstore.api;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yaps.petstore.authentication.domain.dto.UserDTO;
import com.yaps.petstore.authentication.domain.service.UserService;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.exception.FinderException;

/**
 * These servlets returns the selected item / items.
 */
@Controller
public class FindItemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindItemController.class);
    
	@Autowired
	private ApiHelper helper;
  
    @Autowired
	private UserService userService;

    @GetMapping("/find-item")
    protected String findItem(Model model, @RequestParam String itemId, Principal principal) {
        final String mname = "findItem";
        LOGGER.debug("entering "+mname);
        try {
        	ItemDTO itemDTO = helper.retrieveItem(itemId);
			if(principal != null) {
    			UserDTO userDTO = userService.findUser(principal.getName());
    			if(userDTO.getRole().equals("ROLE_USER"))
    				model.addAttribute("username", principal.getName());
    		}
			model.addAttribute("itemId", itemId);
			model.addAttribute("itemDTO", itemDTO);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "Item " + itemId+ " not found");
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (FinderException e) {
			LOGGER.error("unknown user exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName() + " - UNKNOWN USER");
			return "error";
		}
		return "item"; 
    }

	@GetMapping("/find-items")
    protected String findItems(Model model, @RequestParam String productId, Principal principal) {
        final String mname = "findItems";
        LOGGER.debug("entering "+mname);
        try {
        	List<ItemDTO> itemDTOs = helper.retrieveItems(productId);
			ProductDTO productDTO = helper.retrieveProduct(productId);
			if(principal != null) {
    			UserDTO userDTO = userService.findUser(principal.getName());
    			if(userDTO.getRole().equals("ROLE_USER"))
    				model.addAttribute("username", principal.getName());
    		}
			model.addAttribute("productDTO", productDTO);
			model.addAttribute("itemDTOs", itemDTOs);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "No item for product " + productId);
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (FinderException e) {
			LOGGER.error("unknown user exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName() + " - UNKNOWN USER");
			return "error";
		}
		return "items";
    }
}
