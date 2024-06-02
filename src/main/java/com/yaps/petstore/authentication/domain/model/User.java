package com.yaps.petstore.authentication.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.yaps.petstore.domain.model.Address;
import com.yaps.petstore.domain.model.CreditCard;


/**
 * This class represents a customer for the YAPS company.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_USER") 
public class User implements Serializable {

	// ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@Column(name="ID")
	@NotBlank(message = "username must be defined")
	private String username;
	
	@NotBlank(message = "Invalid customer first name")
    private String firstname;
	
	@NotBlank(message = "Invalid customer name")
    private String lastname;
	
	@Size(max=10, message="telephone nb's length exception (10 char.max)")
    private String telephone;
    
    private String email;
    
    @Embedded
    private Address address = new Address();
    
    @Embedded
    private CreditCard creditCard = new CreditCard();
    
    private String role;

	// ======================================
    // =            Constructors            =
    // ======================================
    
    public User() {}

    public User(final String username) {
    	this.username=username;
    }

    public User(final String username, final String firstname, final String lastname) {
    	this.username=username;
    	this.firstname = firstname;
    	this.lastname = lastname;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
    	this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
    	this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(final String telephone) {
    	this.telephone = telephone;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getStreet1() {
        return address.getStreet1();
    }

    public void setStreet1(final String street1) {
        address.setStreet1(street1);
    }

    public String getStreet2() {
        return address.getStreet2();
    }

    public void setStreet2(final String street2) {
        address.setStreet2(street2);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(final String city) {
        address.setCity(city);
    }

    public String getState() {
        return address.getState();
    }

    public void setState(final String state) {
        address.setState(state);
    }

    public String getZipcode() {
        return address.getZipcode();
    }

    public void setZipcode(final String zipcode) {
        address.setZipcode(zipcode);
    }

    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(final String country) {
        address.setCountry(country);
    }

	public CreditCard getCreditCard() {
		return creditCard;
	}
	
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard=creditCard;
	}
	
	public String getCreditCardNumber() {
        return creditCard.getCreditCardNumber();
    }

    public void setCreditCardNumber(final String creditCardNumber) {
        creditCard.setCreditCardNumber(creditCardNumber);
    }

    public String getCreditCardType() {
        return creditCard.getCreditCardType();
    }

    public void setCreditCardType(final String creditCardType) {
        creditCard.setCreditCardType(creditCardType);
    }

    public String getCreditCardExpiryDate() {
        return creditCard.getCreditCardExpiryDate();
    }

    public void setCreditCardExpiryDate(final String creditCardExpiryDate) {
        creditCard.setCreditCardExpiryDate(creditCardExpiryDate);
    }

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("\n\tCustomer {");
        buf.append("\n\t\tId=").append(getUsername());
        buf.append("\n\t\tFirst Name=").append(getFirstname());
        buf.append("\n\t\tLast Name=").append(getLastname());
        buf.append("\n\t\tTelephone=").append(getTelephone());
        buf.append("\n\t\temail=").append(getEmail());
        buf.append("\n\t\tstreet1=").append(getStreet1());
        buf.append(",street2=").append(getStreet2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append("\n\t\tcreditCardNumber=").append(getCreditCardNumber());
        buf.append(",creditCardType=").append(getCreditCardType());
        buf.append(",creditCardExpiryDate=").append(getCreditCardExpiryDate());
        buf.append("\n\t}");
        return buf.toString();
    }
}
