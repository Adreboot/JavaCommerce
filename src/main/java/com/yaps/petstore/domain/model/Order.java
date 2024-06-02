package com.yaps.petstore.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yaps.petstore.authentication.domain.model.User;

/**
 * An order represents the items that a customer buys. This order has several
 * order items and is relevant for one customer. The order has address information
 * like the street, the city, the country... This is because a customer can order
 * a pet and wants it delivered at another address. By default, the order address
 * is the same that the customer's one but it can be changed.
 *
 * @see User
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_ORDER") 
public class Order implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="order_seq_gen")
	private Long id;
	
	@Column(name = "ORDER_DATE")
	private Date orderDate;
	
	@NotBlank(message = "invalid Firstname")
	private String firstname;
	
	@NotBlank(message = "invalid Lastname")
	private String lastname;
	
	@Embedded
	@NotNull(message = "invalid Address")
	private Address address = new Address();
	
    @Embedded
    private CreditCard creditCard = new CreditCard();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_FK", nullable = false)
    @NotNull(message = "invalid Customer")
    private User customer;

    // ======================================
    // =            Constructors            =
    // ======================================
    public Order() {
    	this.orderDate=new Date();
    }

    public Order(final String firstname, final String lastname, final String street1, final String city, final String zipcode, final String country, final User customer) {
    	this.orderDate=new Date();
        setOrderDate(orderDate);
        setFirstname(firstname);
        setLastname(lastname);
        setStreet1(street1);
        setCity(city);
        setZipcode(zipcode);
        setCountry(country);
        setCustomer(customer);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    public Long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public Date getOrderDate() {
        return orderDate;
    }

    private void setOrderDate(final Date orderDate) {
    	this.orderDate = orderDate;
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(final User customer) {
    	this.customer = customer;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("Order{");
        buf.append("id=").append(getId());
        buf.append(",orderDate=").append(getOrderDate());
        buf.append(",firstname=").append(getFirstname());
        buf.append(",lastname=").append(getLastname());
        buf.append(",street1=").append(getStreet1());
        buf.append(",street2=").append(getStreet2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append(",creditCardNumber=").append(getCreditCardNumber());
        buf.append(",creditCardType=").append(getCreditCardType());
        buf.append(",creditCardExpiryDate=").append(getCreditCardExpiryDate());
        buf.append('}');
        return buf.toString();
    }
}
