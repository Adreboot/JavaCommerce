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

import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.domain.service.CatalogService;
import com.yaps.petstore.exception.CreateException;
import com.yaps.petstore.exception.FinderException;
import com.yaps.petstore.exception.RemoveException;
import com.yaps.petstore.exception.UpdateException;

/**
 * These servlets returns the selected item / items.
 */
@RestController
@RequestMapping (path="/api", produces = "application/json")
public class ProductRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);
    
    @Autowired
	private CatalogService catalogService;
    
    @GetMapping("/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		final String mname = "getProducts";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findProducts());
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
    	
	@GetMapping("/products/{categoryId}")
	public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable String categoryId) {
		final String mname = "getProducts";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findProducts(categoryId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<ProductDTO> getProduct(@PathVariable String productId) {
		final String mname = "getProduct";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findProduct(productId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping(path="/product", consumes="application/json")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
		final String mname = "createProduct";
		LOGGER.debug("entering "+mname);
		try {
			return new ResponseEntity<>(catalogService.createProduct(productDTO), HttpStatus.CREATED);
		} catch (CreateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping(path="/product/{productId}", consumes="application/json")
	public ResponseEntity<ProductDTO>  updateProduct(@RequestBody ProductDTO productDTO, @PathVariable String productId) {
		final String mname = "updateProduct";
		LOGGER.debug("entering "+mname);
		if(productDTO.getId()== null || productDTO.getId().equals("") || ! productDTO.getId().equals(productId))
			return ResponseEntity.badRequest().build();
		try {
			ProductDTO prodDTO = catalogService.updateProduct(productDTO);
			return ResponseEntity.ok(prodDTO);
		} catch (UpdateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		final String mname = "deleteProduct";
		LOGGER.debug("entering "+mname);
		try {
			catalogService.deleteProduct(productId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException | RemoveException | FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
