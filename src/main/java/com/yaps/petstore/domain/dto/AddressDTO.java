package com.yaps.petstore.domain.dto;

import java.io.Serializable;

/**
 * This class encapsulates all the data for an address.
 *
 * @see CustomerDTO
 * @see OrderDTO
 */
@SuppressWarnings("serial")
public class AddressDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipcode;
    private String country;

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getStreet1() {
        return street1;
    }

    public void setStreet1(final String street1) {
    	this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(final String street2) {
    	this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
    	this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
    	this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(final String zipcode) {
    	this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
    	this.country = country;
    }

}
