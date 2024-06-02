package com.yaps.petstore.domain.service;

import com.yaps.petstore.domain.dto.CreditCardDTO;
import com.yaps.petstore.exception.FinderException;

public interface CreditCardService {
	
	 public void verifyCreditCard(CreditCardDTO creditCardDTO) throws FinderException;

}
