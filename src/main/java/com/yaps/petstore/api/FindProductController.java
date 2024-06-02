package com.yaps.petstore.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yaps.petstore.domain.dto.ProductDTO;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
public class FindProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindProductController.class);
    
	@Autowired
	private ApiHelper helper;
	
	@GetMapping("/find-products")
	protected String findProducts(Model model, @RequestParam String categoryId) {
		final String mname = "findProducts";
		LOGGER.debug("entering " + mname);
		try {
			List<ProductDTO> productDTOs = helper.retrieveProducts(categoryId);
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("productDTOs", productDTOs);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "No product for category " + categoryId);
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "products";
	}
}
