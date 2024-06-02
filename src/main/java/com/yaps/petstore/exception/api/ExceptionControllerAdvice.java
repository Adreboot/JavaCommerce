package com.yaps.petstore.exception.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
	
private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	
	@ExceptionHandler(BindException.class)
    public String handleBindExceptions(BindException ex, Model model) {
		final String mname = "BindException";
        LOGGER.error("entering "+mname);
       List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorClass = error.getClass().getSimpleName();
            String errorMessage = error.getDefaultMessage();
            errors.add(errorClass+" on field "+fieldName+" - "+errorMessage);
        });
        model.addAttribute("exceptions", errors);
        LOGGER.error("exiting "+mname);
        return "error";
    }
	
	@ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Exception ex, Model model) {
		final String mname = "Exception";
        LOGGER.error("entering "+mname+" - "+ex.getMessage());
        model.addAttribute("exception", "Request [" +req.getMethod()+"] : "+ req.getRequestURL() + " raised " + ex.getClass().getName()+" / "+ex.getMessage()+" /  *****  Veuillez revenir sur la page d'accueil");
        LOGGER.error("exiting "+mname);
        return "error";
    }

}