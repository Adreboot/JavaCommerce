package com.yaps.petstore.domain.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yaps.petstore.domain.dao.CounterRepository;
import com.yaps.petstore.domain.model.Counter;

@Service
public class CounterServiceImpl implements CounterService {
	
private static final Logger LOGGER = LoggerFactory.getLogger(CounterServiceImpl.class);
    
    @Autowired
    private CounterRepository counterRepository;

	@Override
	@Transactional
	public String getUniqueId(String name) {
		final String mname = "getUniqueId";
		LOGGER.debug("entering "+mname);
		int nextId = 0;
		Counter counter = null ;
		try {
			counter = counterRepository.findById(name).get();
			nextId = counter.getValue()+1;
			counter.setValue(nextId);
			counterRepository.save(counter);
		} catch (NoSuchElementException e) {
			counter = new Counter();
			counter.setName(name);
			nextId = 1;
			counter.setValue(nextId);
			counterRepository.save(counter);
		}		
		LOGGER.debug("exiting "+mname+" returning id "+nextId);
		return String.valueOf(nextId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String getLastId(String name) {
		String LastId = "";
		final String mname = "getLastId";
		LOGGER.debug("entering "+mname);
		Counter counter = null ;
		try {
			counter = counterRepository.findById(name).get();
		} catch (NoSuchElementException e) {}		
		LastId = String.valueOf(counter.getValue());
		LOGGER.debug("exiting "+mname+" returning id "+LastId);
		return LastId;
	}

	@Override
	@Transactional
	public void deleteById(String name) {
		final String mname = "deleteById";
		LOGGER.debug("entering "+mname);
		counterRepository.deleteById(name);
		LOGGER.debug("exiting "+mname);
	}

}
