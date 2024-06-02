package com.yaps.petstore.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import  com.yaps.petstore.domain.constant.CreditCardStatus;
import com.yaps.petstore.domain.dto.CreditCardDTO;
import com.yaps.petstore.exception.FinderException;

@Service
public class CreditCardServiceImpl implements CreditCardService {

private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardServiceImpl.class);
	
	@Value("${barkbank.uri}")
	private String barkbankUri;
	
	@Override
	public void verifyCreditCard(CreditCardDTO creditCardDTO) throws FinderException {
		String mname = "verifyCreditCard";
		LOGGER.debug("entering "+mname);
		ResponseEntity<String> entity = null;
		String result = null;

		try {
			RestTemplate restTemplate = new RestTemplate();
			entity = restTemplate.postForEntity(barkbankUri,creditCardDTO ,String.class);
			result = entity.getBody();
			LOGGER.debug(mname+" - IN VERIF CC ......RESULT IS ... " + result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinderException("Connection exception");
		}
		if (result.equals(CreditCardStatus.INVALID_DATE))
			throw new FinderException(CreditCardStatus.INVALID_DATE);
		if (result.equals(CreditCardStatus.INVALID_NUMBER))
			throw new FinderException(CreditCardStatus.INVALID_NUMBER);
		if (result.equals(CreditCardStatus.INVALID_CREDIT_CARD))
			throw new FinderException(CreditCardStatus.INVALID_CREDIT_CARD);
		LOGGER.debug("exiting "+mname+" without raising exception");
	}
}
