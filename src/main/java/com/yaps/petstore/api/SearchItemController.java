package com.yaps.petstore.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yaps.petstore.domain.dto.ItemDTO;

/**
 * These servlets returns the selected item / items.
 */
@Controller
public class SearchItemController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchItemController.class);
	
	@Autowired
	private ApiHelper helper;
   
    @PostMapping("/search-items")
    protected String searchItems(Model model, @RequestParam String keyword) {
    	final String mname = "searchItems";
        LOGGER.debug("entering "+mname);
        List<ItemDTO> itemDTOs = helper.retrieveItemsByKeyword(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("itemDTOs", itemDTOs);
        LOGGER.debug("exiting "+mname);
        return "items";
    }

	@GetMapping("/price-search")
    protected String priceSearchForm() {
       return "search-by-price";
    }
    
    @PostMapping("/price-search")
    protected String priceSearch(Model model, @RequestParam String gtLt, @RequestParam double price) {
        final String mname = "priceSearch";
        LOGGER.debug("entering "+mname);
        List<ItemDTO> itemDTOs = helper.retrieveItemsByPrice(gtLt, price);
        model.addAttribute("price", price);
        model.addAttribute("gtLt", gtLt);
        model.addAttribute("itemDTOs", itemDTOs);
        LOGGER.debug("exiting "+mname);
        return "items";
    }

	@PostMapping("/price-and-keyword-search")
    protected String priceAndKeywordSearch(Model model, @RequestParam String gtLt, @RequestParam double price, @RequestParam String keyword) {
		final String mname = "price-and-keyword-search";
        LOGGER.debug("entering "+mname);
       	List<ItemDTO> itemDTOs = helper.retrieveItemsByPriceAndKeyword(gtLt, price, keyword);
        model.addAttribute("price", price);
        model.addAttribute("gtLt", gtLt);
        model.addAttribute("itemDTOs", itemDTOs);
        model.addAttribute("keyword", keyword);
        LOGGER.debug("exiting "+mname);
        return "items";
    }
}
