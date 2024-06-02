package com.yaps.petstore.domain.dto;

import java.io.Serializable;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of an Order Line. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class OrderLineDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private int quantity;
    private double unitCost;
    private ItemDTO itemDTO;
    private OrderDTO orderDTO;

    // ======================================
    // =            Constructors            =
    // ======================================
    public OrderLineDTO() {
    }

    public OrderLineDTO(final int quantity, final double unitCost) {
        setQuantity(quantity);
        setUnitCost(unitCost);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
    	this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final double unitCost) {
    	this.unitCost = unitCost;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(final ItemDTO itemDTO) {
    	this.itemDTO = itemDTO;
    }

    public OrderDTO getOrderDTO() {
		return orderDTO;
	}

	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}

	public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("OrderLineDTO{");
        buf.append("quantity=").append(getQuantity());
        buf.append(",unitCost=").append(getUnitCost());
        buf.append(",itemId=").append(getItemDTO().getId());
        buf.append(",itemName=").append(getItemDTO().getName());
        buf.append('}');
        return buf.toString();
    }
}
