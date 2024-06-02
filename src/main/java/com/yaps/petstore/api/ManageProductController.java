package com.yaps.petstore.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ProductDTO;

@Secured("ROLE_ADMIN")
@RequestMapping("/manage")
@Controller
public class ManageProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageProductController.class);

	@Autowired
	private ApiHelper helper;

	@GetMapping("/manage-products/{categoryId}")
	public String manageProducts(@PathVariable String categoryId, Model model) {
		final String mname = "manageProducts";
		LOGGER.debug("entering " + mname);
		List<ProductDTO> productDTOs = helper.retrieveProducts(categoryId);
		model.addAttribute("productDTOs", productDTOs);	
		return "manage-catalog";
	}

	@GetMapping("/update-product/{productId}")
	public String showProduct(@PathVariable String productId, Model model) {
		final String mname = "showProduct";
		LOGGER.debug("entering " + mname);
		ProductDTO productDTO = helper.retrieveProduct(productId);
		model.addAttribute("productDTO", productDTO);
		LOGGER.debug("exiting " + mname);
		return "update-product";
	}

	@PostMapping("/update-product")
	public String updateProduct(@Valid @ModelAttribute ProductDTO productDTO, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "updateProduct";
		LOGGER.debug("entering " + mname);
		String JsonProd = helper.restUpdateProduct(productDTO, request, cookie);
		LOGGER.debug("updatedProduct : "+JsonProd);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("productUpdated", productDTO.getId());
		LOGGER.debug("exiting "+mname);
		return "manage-catalog";
	}

	@GetMapping("/delete-product/{productId}")
	public String deleteProduct(@PathVariable String productId, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "deleteProduct";
		LOGGER.debug("entering " + mname);
		helper.restDeleteProduct(productId, request, cookie);
		LOGGER.debug("deletedProduct id : " + productId);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("productDeleted", productId);
		LOGGER.debug("exiting " + mname);
		return "manage-catalog";
	}
}
