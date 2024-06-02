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
import com.yaps.petstore.domain.dto.ItemDTO;

@Secured("ROLE_ADMIN")
@RequestMapping("/manage")
@Controller
public class ManageItemController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageItemController.class);
	
	@Autowired
	private ApiHelper helper;

	@GetMapping("/manage-items/{productId}")
	public String manageItems(@PathVariable String productId, Model model) {
		final String mname = "manageItems";
		LOGGER.debug("entering " + mname);
		List<ItemDTO> itemDTOs = helper.retrieveItems(productId);
		model.addAttribute("itemDTOs", itemDTOs);
		LOGGER.debug("exiting " + mname);
		return "manage-catalog";
	}

	@GetMapping("/update-item/{itemId}")
	public String showItem(@PathVariable String itemId, Model model) {
		final String mname = "showItem";
		LOGGER.debug("entering " + mname);
		ItemDTO itemDTO = helper.retrieveItem(itemId);
		model.addAttribute("itemDTO", itemDTO);	
		LOGGER.debug("exiting " + mname);
		return "update-item";
	}

	@PostMapping("/update-item")
	public String updateItem(@Valid @ModelAttribute ItemDTO itemDTO, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "updateItem";
		LOGGER.debug("entering " + mname);
		String JsonIt = helper.restUpdateItem(itemDTO, request, cookie);
		LOGGER.debug("updatedItem : "+JsonIt);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("itemUpdated", itemDTO.getId());		
		LOGGER.debug("exiting "+mname);
		return "manage-catalog";
	}

	@GetMapping("/delete-item/{itemId}")
	public String deleteItem(@PathVariable String itemId, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "deleteItem";
		LOGGER.debug("entering " + mname);
		helper.restDeleteItem(itemId, request, cookie);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("itemDeleted", itemId);
		LOGGER.debug("exiting " + mname);
		return "manage-catalog";
	}
}
