package com.yaps.petstore.domain.service;

import java.util.Collection;
import java.util.Map;

import com.yaps.petstore.domain.dto.ShoppingCartItemDTO;


/**
 * This interface gives a remote view of the ShoppingCartBean. Any distant client that wants to call
 * a method on the ShoppingCartBean has to use this interface.
 */
public interface ShoppingCartService {
	
	// ======================================
    // =           Business methods         =
    // ======================================

    /**
     * This method returns the shopping cart. The shopping cart is represented as a Map (key, value)
     * where item ids and quantities are stored.
     *
     * @return the shopping cart
     */
    public Map<String, Integer> getCart();

    /**
     * This method returns a collection of ShoppingCartDTO. It uses the item id that is stored
     * in the shopping cart to get all item information (id, name, product, quantity, cost).
     *
     * @return a collection of ShoppingCartDTO
     */
    public Collection<ShoppingCartItemDTO> getItems();

    /**
     * This method adds an item to the shopping cart with a quantity equals to one.
     *
     * @param itemId
     * @throws RemoteException
     */
    void addItem(String itemId);

    /**
     * This method removes an item from the shopping cart.
     *
     * @param itemId
     */
    public void removeItem(String itemId);

    /**
     * This method updates the quantity of a given item in the shopping cart. If the quantity is
     * equal to zero, the item is removed.
     *
     * @param itemId
     * @param newQty
     */
    public void updateItemQuantity(String itemId, int newQty);

    /**
     * This method computes the total amount in the shopping cart by multiplying all items
     * by their quantity.
     *
     * @return
     */
    public Double getTotal();

    /**
     * This method empties the shopping cart.
     *
     */
    public void empty();

}
