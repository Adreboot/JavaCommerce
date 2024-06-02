package com.yaps.petstore.catalogRestApi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.service.CatalogService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

@RestController
@RequestMapping (path="/api", produces = "application/json")
public class CategoryRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRestController.class);

	@Autowired
	private CatalogService catalogService;
	
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getCategories() {
		final String mname = "getCategories";
		LOGGER.debug("entering "+mname);

		try {
			return ResponseEntity.ok( catalogService.findCategories());
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable String categoryId) {
		final String mname = "getCategory";
		LOGGER.debug("entering "+mname);

		try {
			return ResponseEntity.ok(catalogService.findCategory(categoryId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/category")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
		final String mname = "createCategory";
		LOGGER.debug("entering "+mname);
		try {
			return new ResponseEntity<>(catalogService.createCategory(categoryDTO), HttpStatus.CREATED);
		} catch (CreateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable String categoryId) {
		final String mname = "updateCategory";
		LOGGER.debug("entering "+mname);
		if(categoryDTO.getId()== null || categoryDTO.getId().equals("") || ! categoryDTO.getId().equals(categoryId))
			return ResponseEntity.badRequest().build();
		try {
			CategoryDTO catDTO = catalogService.updateCategory(categoryDTO);
			return ResponseEntity.ok(catDTO);
		} catch (UpdateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/category/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
		final String mname = "deleteCategory";
		LOGGER.debug("entering "+mname);
		try {
			catalogService.deleteCategory(categoryId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException | RemoveException | FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
