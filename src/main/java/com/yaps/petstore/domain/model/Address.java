package com.yaps.petstore.domain.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {
	
	// ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "invalid Street")
	private String street1;
	private String street2;
	 @NotBlank(message = "invalid City")
	private String city;
	private String state;
	@NotBlank(message = "invalid Zipcode")
	private String zipcode;
	@NotBlank(message = "invalid Country")
	private String country;
	
	public Address() {}

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