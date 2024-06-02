package com.yaps.petstore.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.yaps.petstore.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of an Order. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class OrderDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private long id;
    private Date orderDate;
    private String firstname;
    private String lastname;
    private AddressDTO addressDTO = new AddressDTO();
    private CreditCardDTO creditCardDTO = new CreditCardDTO();
    private UserDTO customerDTO;

    // ======================================
    // =            Constructors            =
    // ======================================
    public OrderDTO() {
    }

    public OrderDTO(final String firstname, final String lastname, final String street1, final String city, final String zipcode, final String country) {
        setFirstname(firstname);
        setLastname(lastname);
        setStreet1(street1);
        setCity(city);
        setZipcode(zipcode);
        setCountry(country);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public long getId() {
        return id;
    }

    public void setId(final long id) {
    	this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate) {
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
        return addressDTO.getStreet1();
    }

    public void setStreet1(final String street1) {
        addressDTO.setStreet1(street1);
    }

    public String getStreet2() {
        return addressDTO.getStreet2();
    }

    public void setStreet2(final String street2) {
        addressDTO.setStreet2(street2);
    }

    public String getCity() {
        return addressDTO.getCity();
    }

    public void setCity(final String city) {
        addressDTO.setCity(city);
    }

    public String getState() {
        return addressDTO.getState();
    }

    public void setState(final String state) {
        addressDTO.setState(state);
    }

    public String getZipcode() {
        return addressDTO.getZipcode();
    }

    public void setZipcode(final String zipcode) {
        addressDTO.setZipcode(zipcode);
    }

    public String getCountry() {
        return addressDTO.getCountry();
    }

    public void setCountry(final String country) {
        addressDTO.setCountry(country);
    }
    
    public CreditCardDTO getCreditCardDTO() {
        return creditCardDTO;
    }

    public void setCreditCardDTO(final CreditCardDTO creditCardDTO) {
        this.creditCardDTO=creditCardDTO;
    }

    public String getCreditCardNumber() {
        return creditCardDTO.getCreditCardNumber();
    }

    public void setCreditCardNumber(final String creditCardNumber) {
        creditCardDTO.setCreditCardNumber(creditCardNumber);
    }

    public String getCreditCardType() {
        return creditCardDTO.getCreditCardType();
    }

    public void setCreditCardType(final String creditCardType) {
        creditCardDTO.setCreditCardType(creditCardType);
    }

    public String getCreditCardExpiryDate() {
        return creditCardDTO.getCreditCardExpiryDate();
    }

    public void setCreditCardExpiryDate(final String creditCardExpiryDate) {
        creditCardDTO.setCreditCardExpiryDate(creditCardExpiryDate);
    }

    public UserDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(UserDTO customerDTO) {
    	this.customerDTO = customerDTO;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("OrderDTO{");
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
        buf.append(",creditCardExpiry Date=").append(getCreditCardExpiryDate());
        buf.append(",customerId=").append(getCustomerDTO().getUsername()).append(']');
        buf.append('}');
        return buf.toString();
    }
}
