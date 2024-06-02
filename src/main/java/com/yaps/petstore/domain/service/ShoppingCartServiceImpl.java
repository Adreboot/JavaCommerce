package com.yaps.petstore.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ShoppingCartItemDTO;
import com.yaps.petstore.exception.FinderException;

@Service
@Transactional
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	CatalogService catalogService;
	
	Map<String, Integer> cart = new HashMap<String, Integer>();
	
	Collection<ShoppingCartItemDTO> listCartItemDTO = new ArrayList<ShoppingCartItemDTO>();
	
	/**
     * This method returns the shopping cart. The shopping cart is represented as a Map (key, value)
     * where item ids and quantities are stored.
     *
     * @return the shopping cart
     */
	@Override
	public Map<String, Integer> getCart() {
		
		return cart;
	}

	
	/**
     * This method returns a collection of ShoppingCartDTO. It uses the item id that is stored
     * in the shopping cart to get all item information (id, name, product, quantity, cost).
     *
     * @return a collection of ShoppingCartDTO
     */
	@Override
	public Collection<ShoppingCartItemDTO> getItems() {
		listCartItemDTO.clear();
		for(String itemId : cart.keySet()) {
			try {
				ItemDTO itemDTO = catalogService.findItem(itemId);
				
				ShoppingCartItemDTO shoppingCartItemDTO = new ShoppingCartItemDTO(itemId,itemDTO.getName(),itemDTO.getProductDTO().getDescription(),cart.get(itemId),itemDTO.getUnitCost());
				
				listCartItemDTO.add(shoppingCartItemDTO);
				
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		return listCartItemDTO;
	}

	/**
     * This method adds an item to the shopping cart with a quantity equals to one.
     *
     * @param itemId
     * @throws RemoteException
     */
	@Override
	public void addItem(String itemId) {
		
		if(cart.containsKey(itemId)) {
			cart.replace(itemId, cart.get(itemId)+1);
		}else {
			cart.put(itemId, 1);
		}

	}

	/**
     * This method removes an item from the shopping cart.
     *
     * @param itemId
     */
	@Override
	public void removeItem(String itemId) {

		cart.remove(itemId);

	}

	/**
     * This method updates the quantity of a given item in the shopping cart. If the quantity is
     * equal to zero, the item is removed.
     *
     * @param itemId
     * @param newQty
     */
	@Override
	public void updateItemQuantity(String itemId, int newQty) {
		if(newQty == 0) {
			this.removeItem(itemId);
		}else {
			cart.replace(itemId, newQty);
		}
	}

	/**
     * This method computes the total amount in the shopping cart by multiplying all items
     * by their quantity.
     *
     * @return
     */
	@Override
	public Double getTotal() {
		this.getItems();
		Double total = 0.0;
		
		for(ShoppingCartItemDTO cartItemDTO : listCartItemDTO) {
			total = total + cartItemDTO.getTotalCost();
		}
		
		return total;
	}

	/**
     * This method empties the shopping cart.
     *
     */
	@Override
	public void empty() {
		cart.clear();

	}

}
