package com.yaps.petstore.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientsConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebClientsConfig.class);
	
	@Value("${app.uri}")
	private String appUri;
	
	@Value("${keycloak.auth-server-url}")
	private String keycloakUri;
	
	@Bean
    public WebClient webClient() {
    	return  WebClient.builder()
    			.baseUrl(appUri)
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.filter(logRequest()) 
    			.build();
    }
	
	@Bean
    public WebClient keyCloakClient() {
    	return  WebClient.builder()
    			.baseUrl(keycloakUri)
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.filter(logRequest()) 
    			.build();
    }
    
    
 // This method returns filter function which will log request data
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        	LOGGER.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOGGER.debug("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
