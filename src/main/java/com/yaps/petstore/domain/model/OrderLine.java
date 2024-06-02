package com.yaps.petstore.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * An Order has several order lines. This class represent one order line.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_ORDER_LINE") 
public class OrderLine implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="order_line_seq_gen")
	private Long id;
	
	@Positive(message = "invalid Quantity")
    private int quantity;
	
    @Column(name="UNIT_COST")
    @Positive(message = "invalid Unit Cost")
    private double unitCost;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="ITEM_FK", nullable = false)
    @NotNull(message = "invalid Item")
    private Item item;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="ORDER_FK", nullable = false)
    @NotNull(message = "invalid Order")
    private Order order;

    // ======================================
    // =            Constructors            =
    // ======================================
    public OrderLine() {}

    public OrderLine(final int quantity, final double unitCost, final Order order, final Item item) {
        setQuantity(quantity);
        setUnitCost(unitCost);
        setOrder(order);
        setItem(item);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
    	this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
    	this.item = item;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("\nOrderLine {");
        buf.append("\n\tId=").append(getId());
        buf.append("\n\tQuantity=").append(getQuantity());
        buf.append("\n\tUnit Cost=").append(getUnitCost());
        buf.append("\n\tItem Id=").append(getItem().getId());
        buf.append("\n\tItem Name=").append(getItem().getName());
        buf.append("\n}");
        return buf.toString();
    }
}
