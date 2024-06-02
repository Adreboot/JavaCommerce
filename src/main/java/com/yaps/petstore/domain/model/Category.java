package com.yaps.petstore.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * This class represents a Category in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_CATEGORY")  
public class Category implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "id must be defined")
	@Id
	private String id;
	@NotBlank(message = "invalid Name")
    private String name;
    @NotBlank(message = "invalid Description")
    private String description;

    // ======================================
    // =            Constructors            =
    // ======================================
    public Category() {}

    public Category(final String id) {
        setId(id);
    }

    public Category(final String id, final String name, final String description) {
        setId(id);
        setName(name);
        setDescription(description);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
    	this.description = description;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("\n\tCategory {");
        buf.append("\n\t\tId=").append(getId());
        buf.append("\n\t\tName=").append(getName());
        buf.append("\n\t\tDescription=").append(getDescription());
        buf.append("\n\t}");
        return buf.toString();
    }
}
