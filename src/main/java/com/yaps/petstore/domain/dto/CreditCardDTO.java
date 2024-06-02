package com.yaps.petstore.domain.dto;

import java.io.Serializable;

/**
 * This class encapsulates all the data for a credit card.
 *
 * @see CustomerDTO
 * @see OrderDTO
 */
@SuppressWarnings("serial")
public class CreditCardDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String creditCardNumber;
    private String creditCardType;
    private String creditCardExpiryDate;

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(final String creditCardNumber) {
    	this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(final String creditCardType) {
    	this.creditCardType = creditCardType;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(final String creditCardExpiryDate) {
    	this.creditCardExpiryDate = creditCardExpiryDate;
    }
    
}
