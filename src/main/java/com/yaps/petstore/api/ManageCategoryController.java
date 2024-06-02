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

@Secured("ROLE_ADMIN")
@RequestMapping("/manage")
@Controller
public class ManageCategoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageCategoryController.class);
	
	@Autowired
	private ApiHelper helper;

	@GetMapping("/manage-categories")
	public String manageCategories(Model model) {
		final String mname = "manageCategories";
		LOGGER.debug("entering "+mname);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);		
		LOGGER.debug("exiting "+mname);
		return "manage-catalog";
	}

	@GetMapping("/update-category/{categoryId}")
	public String showCategory(@PathVariable String categoryId, Model model) {
		final String mname = "showCategory";
		LOGGER.debug("entering "+mname);
		CategoryDTO categoryDTO = helper.retrieveCategory(categoryId);
		model.addAttribute("categoryDTO", categoryDTO);	
		LOGGER.debug("exiting "+mname);
		return "update-category";
	}

	@PostMapping("/update-category")
	public String updateCategory(@Valid @ModelAttribute CategoryDTO categoryDTO, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "updateCategory";
		LOGGER.debug("entering "+mname);
		String JsonCat = helper.restUpdateCategory(categoryDTO, request,cookie);
		LOGGER.debug("updatedcategory : "+JsonCat);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("categoryUpdated", categoryDTO.getId());
		LOGGER.debug("exiting "+mname);
		return "manage-catalog";
	}

	@GetMapping("/delete-category/{categoryId}")
	public String deleteCategory(@PathVariable String categoryId, Model model, HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "deleteCategory";
		LOGGER.debug("entering "+mname);
		helper.restDeleteCategory(categoryId, request,cookie);
		List<CategoryDTO> categoryDTOs = helper.retrieveCategories();
		model.addAttribute("categoryDTOs", categoryDTOs);
		model.addAttribute("categoryDeleted", categoryId);
		LOGGER.debug("exiting "+mname);
		return "manage-catalog";
	}
}
