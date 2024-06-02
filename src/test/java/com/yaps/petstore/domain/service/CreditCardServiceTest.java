package com.yaps.petstore.domain.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yaps.petstore.domain.constant.CreditCardStatus;
import com.yaps.petstore.domain.dto.CreditCardDTO;
import com.yaps.petstore.exception.FinderException;

/**
 * This class tests the CreditCardService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CreditCardServiceTest implements CreditCardStatus {
		
	@Autowired
	private CreditCardService service;
		
    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This method checks a valid credit card.
     */
    @Test
    public void testVerifyValidCreditCard() {
    	CreditCardDTO validCreditCard = new CreditCardDTO();
    	validCreditCard.setCreditCardNumber("4564 1231 4564 2222");
    	validCreditCard.setCreditCardExpiryDate("10/23");
    	validCreditCard.setCreditCardType("Visa");
        try {
        	service.verifyCreditCard(validCreditCard);
        } catch (FinderException e) {
        	fail("Credit card is valid");
        }
    }

    /**
     * This method checks a credit card with an old date.
     */
    @Test
    public void testVerifyTooOldCreditCard() {
    	CreditCardDTO creditCard = new CreditCardDTO();
    	creditCard.setCreditCardNumber("4564 1231 4564 2222");
    	creditCard.setCreditCardExpiryDate("01/01");
    	creditCard.setCreditCardType("Visa");
        try {
        	service.verifyCreditCard(creditCard);
        	fail("Credit card is too old");
        } catch (FinderException e) {
            String assertMessage = "Credit card is too old. The exception message should be " + INVALID_DATE;
            assertEquals(assertMessage, INVALID_DATE, e.getMessage());
        }
    }

    /**
     * This method checks a credit card with a month > 12.
     */
    @Test
    public void testCreditCardWithInvalidMonth() throws Exception {
    	CreditCardDTO creditCard = new CreditCardDTO();
    	creditCard.setCreditCardNumber("4564 1231 4564 2222");
    	creditCard.setCreditCardExpiryDate("99/25");
    	creditCard.setCreditCardType("Visa");
        try {
        	service.verifyCreditCard(creditCard);
        	fail("Credit card has invalid month");
        } catch (FinderException e) {
            String assertMessage = "Credit card has invalid month. The exception message should be " + INVALID_DATE;
            assertEquals(assertMessage, INVALID_DATE, e.getMessage());
        }
    }

    /**
     * This method checks a credit card with a bad format for the year.
     */
    @Test
    public void testVerifyCreditCardWithInvalidYearFormat() throws Exception {
    	CreditCardDTO creditCard = new CreditCardDTO();
    	creditCard.setCreditCardNumber("4564 1231 4564 2222");
    	creditCard.setCreditCardExpiryDate("juin deux mille trente");
    	creditCard.setCreditCardType("Visa");
        try {
        	service.verifyCreditCard(creditCard);
        	fail("Credit card has invalid year format");
        } catch (FinderException e) {
            String assertMessage = "Credit card has invalid year format. The exception message should be " + INVALID_CREDIT_CARD;
            assertEquals(assertMessage, INVALID_CREDIT_CARD, e.getMessage());
        }
    }

    /**
     * This method checks a credit card with an invalid number for a visa.
     */
    @Test
    public void testVerifyCreditCardWithInvalidNumber() throws Exception {
    	CreditCardDTO creditCard = new CreditCardDTO();
    	creditCard.setCreditCardNumber("4564 1231 4564 1111");
    	creditCard.setCreditCardExpiryDate("10/23");
    	creditCard.setCreditCardType("Visa");
        try {
        	service.verifyCreditCard(creditCard);
        	fail("Credit card number is wrong");
        } catch (FinderException e) {
            String assertMessage = "Credit card number is wrong. The exception message should be " + INVALID_NUMBER;
            assertEquals(assertMessage, INVALID_NUMBER, e.getMessage());
        }
    }
    
}