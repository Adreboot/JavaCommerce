package com.yaps.petstore.api;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yaps.petstore.domain.service.OrderService;
import com.yaps.petstore.domain.service.ShoppingCartService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;

@Secured("ROLE_USER")
@Controller
public class ShoppingCartCheckoutController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCheckoutController.class);

	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	OrderService orderService; 
	
	@GetMapping("/checkout")
	protected String checkoutCart(Model model,Principal principal) throws CreateException, FinderException {
		final String mname = "checkoutCart";
		LOGGER.debug("entering "+mname);

		try {
			Long orderId = orderService.createOrder(principal.getName(), shoppingCartService.getCart());
			model.addAttribute("orderId", orderId);
			shoppingCartService.empty();
		} catch (CreateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName()+ " : Order object is empty");
			return "error";
		}
		LOGGER.debug("exiting "+mname);
		
		return "checkout";
	}
	
}
