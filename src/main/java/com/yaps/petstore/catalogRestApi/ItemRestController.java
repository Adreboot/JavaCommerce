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

import com.yaps.petstore.domain.dto.ItemDTO;
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
public class ItemRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemRestController.class);
    
    @Autowired
	private CatalogService catalogService;
    
    @GetMapping("/items")
	public ResponseEntity<List<ItemDTO>> getAllItems() {
		final String mname = "getAllItems";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findItems());
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
    	
	@GetMapping("/items/{productId}")
	public ResponseEntity<List<ItemDTO>> getItems(@PathVariable String productId) {
		final String mname = "getItems";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findItems(productId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/item/{itemId}")
	public ResponseEntity<ItemDTO> getItem(@PathVariable String itemId) {
		final String mname = "getItem";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.findItem(itemId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/itemsByKeyword/{keyword}")
	public ResponseEntity<List<ItemDTO>> getItemsByKeyword(@PathVariable String keyword) {
		final String mname = "getItemsByKeyword";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.searchItems(keyword));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/itemsByPrice/{gtLt}/{price}")
	public ResponseEntity<List<ItemDTO>> getItemsByPrice(@PathVariable String gtLt, @PathVariable double price) {
		final String mname = "getItemsByPrice";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.searchItemsByPrice(gtLt, price));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/itemsByPriceAndKeyword/{gtLt}/{price}/{keyword}")
	public ResponseEntity<List<ItemDTO>> getItemsByPriceAndKeyword(@PathVariable String gtLt, @PathVariable double price, @PathVariable String keyword) {
		final String mname = "getItemsByPriceAndKeyword";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(catalogService.searchItemsByPriceAndKeyword(gtLt, price,keyword));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping(path="/item", consumes="application/json")
	public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
		final String mname = "createItem";
		LOGGER.debug("entering "+mname);
		try {
			return new ResponseEntity<>(catalogService.createItem(itemDTO), HttpStatus.CREATED);
		} catch (CreateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping(path="/item/{itemId}", consumes="application/json")
	public ResponseEntity<ItemDTO>  updateItem(@RequestBody ItemDTO itemDTO, @PathVariable String itemId) {
		final String mname = "updateItem";
		LOGGER.debug("entering "+mname);
		if(itemDTO.getId()== null || itemDTO.getId().equals("") || ! itemDTO.getId().equals(itemId))
			return ResponseEntity.badRequest().build();
		try {
			ItemDTO itDTO = catalogService.updateItem(itemDTO);
			return ResponseEntity.ok(itDTO);
		} catch (UpdateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/item/{itemId}")
	public ResponseEntity<Void> deleteItem(@PathVariable String itemId) {
		final String mname = "deleteItem";
		LOGGER.debug("entering "+mname);
		try {
			catalogService.deleteItem(itemId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException | RemoveException | FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
