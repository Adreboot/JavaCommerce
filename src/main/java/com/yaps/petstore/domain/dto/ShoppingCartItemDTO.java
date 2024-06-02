package com.yaps.petstore.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * This class is a client view of an item of the Shopping Cart.
 * A shopping cart is made of several items.
 * This class only transfers data from a distant service to a client.
 */
public class ShoppingCartItemDTO {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "id must be defined")
    private String itemId;
	@NotBlank(message = "invalid Name")
    private String itemName;
	@NotBlank(message = "invalid Description")
    private String productDescription;
	@Positive(message = "invalid quantity")
    private int quantity;
	@Positive(message = "invalid price")
    private double unitCost;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ShoppingCartItemDTO(final String itemId, final String itemName, final String productDescription, final int quantity, final double unitCost) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getTotalCost() {
        return quantity * unitCost;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("ItemDTO{");
        buf.append("itemId=").append(getItemId());
        buf.append(",itemName=").append(getItemName());
        buf.append(",description=").append(getProductDescription());
        buf.append(",quantity=").append(getQuantity());
        buf.append(",unitCost=").append(getUnitCost());
        buf.append('}');
        return buf.toString();
    }
}

