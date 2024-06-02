package com.yaps.petstore.domain.dto;

import java.io.Serializable;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of an Item. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class ItemDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String id;
    private String name;
    private double unitCost;
    private String imagePath;
    private ProductDTO productDTO;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ItemDTO() {
    }

    public ItemDTO(final String id, final String name, final double unitCost) {
        setId(id);
        setName(name);
        setUnitCost(unitCost);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getId() {
        return id;
    }

    public void setId(final String id) {
    	this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
    	this.name = name;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final double unitCost) {
    	this.unitCost = unitCost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(final String imagePath) {
    	this.imagePath = imagePath;
    }

    public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("ItemDTO{");
        buf.append("id=").append(getId());
        buf.append(",name=").append(getName());
        buf.append(",unitCost=").append(getUnitCost());
        buf.append(",imagePath=").append(getImagePath());
        buf.append(",productId=").append(getProductDTO().getId());
        buf.append(",productName=").append(getProductDTO().getName());
        buf.append(",productDescription=").append(getProductDTO().getDescription());
        buf.append('}');
        return buf.toString();
    }
}
