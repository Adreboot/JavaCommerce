package com.yaps.petstore.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.yaps.petstore.domain.service.OrderService;
import com.yaps.petstore.domain.service.ShoppingCartService;

@Secured("ROLE_USER")
@Controller
public class ShoppingCartController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	OrderService orderService; 
	
	@GetMapping("/view-cart")
	protected String viewCart(Model model) {
		final String mname = "viewCart";
		LOGGER.debug("entering "+mname);
		model.addAttribute("cart", shoppingCartService.getItems());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting "+mname);
		return "cart";
	}

	@GetMapping("/add-to-cart")
	protected String addItemToCart(Model model, @RequestParam String itemId) {
		final String mname = "addItemToCart";
		LOGGER.debug("entering "+mname);
		// Adds the itemId into the Shopping Cart
		shoppingCartService.addItem(itemId);
		model.addAttribute("cart", shoppingCartService.getItems());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting "+mname);
		return "cart";
	}
	
	@GetMapping("/remove-from-cart")
	protected String removeItemFromCart(Model model, @RequestParam String itemId) {
		final String mname = "removeItemFromCart";
		LOGGER.debug("entering "+mname);
		// removes the itemId form the Shopping Cart
		shoppingCartService.removeItem(itemId);
		model.addAttribute("cart", shoppingCartService.getItems());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting "+mname);
		return "cart";
	}
	
	@PostMapping("/update-cart")
	protected String updateCart(Model model, @RequestParam String itemId, @RequestParam int quantity) {
		final String mname = "updateCart";
		LOGGER.debug("entering "+mname);
		// updates the Shopping Cart
		shoppingCartService.updateItemQuantity(itemId, quantity);
		model.addAttribute("cart", shoppingCartService.getItems());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting "+mname);
		return "cart";
	}
	
}
