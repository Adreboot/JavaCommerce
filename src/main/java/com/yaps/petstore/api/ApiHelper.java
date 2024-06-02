package com.yaps.petstore.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.dto.ProductDTO;

import reactor.core.publisher.Mono;

@Component
public class ApiHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiHelper.class);
	
	@Autowired
	private WebClient webClient;
	
	protected CategoryDTO retrieveCategory(String categoryId) {
		return this.webClient
					.get()
					.uri("/api/category/" + categoryId)
					.retrieve()
					.bodyToMono(CategoryDTO.class)
					.block();
	}
	
	protected List<CategoryDTO> retrieveCategories() {
		final String mname = "retrieveCategories";
		LOGGER.debug("entering " + mname);
		return this.webClient
					.get()
					.uri("/api/categories")
					.retrieve()
					.bodyToFlux(CategoryDTO.class)
					.collectList()
					.block();		
	}
	
	protected String restUpdateCategory(CategoryDTO categoryDTO, HttpServletRequest request, String cookie) {
		final String mname = "restUpdateCategory";
		LOGGER.debug("entering " + mname);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
//		HttpSession session = request.getSession();
//		String id = session.getId();
//		LOGGER.debug("JSESSIONID : "+id);
		LOGGER.debug("JSESSIONID : "+cookie);
		return this.webClient
					.put()
					.uri("/api/category/"+categoryDTO.getId())
					.header("X-CSRF-TOKEN", sessionToken.getToken())		// oubli => 403 FORBIDDEN
					.cookie("JSESSIONID",cookie)							// oubli => 302 FOUND
					.body(Mono.just(categoryDTO), CategoryDTO.class)
					.retrieve()
					// Facilitateur de deboggage
					.onStatus(HttpStatus::is3xxRedirection, clientResponse -> Mono.error(new Exception("is3xxRedirection : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("is4xxClientError : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::is5xxServerError, clientResponse ->  Mono.error(new Exception("is5xxServerError : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::isError, clientResponse -> Mono.error(new Exception("error : "+clientResponse.statusCode())))
					.bodyToMono(String.class)
					.block();
	}
	
	protected void restDeleteCategory(String categoryId, HttpServletRequest request, String cookie) {
		final String mname = "restDeleteCategory";
		LOGGER.debug("entering " + mname);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		this.webClient
					.delete()
					.uri("/api/category/" + categoryId)
					.header("X-CSRF-TOKEN", sessionToken.getToken())
					.cookie("JSESSIONID",cookie)
					.retrieve()
					// Facilitateur de deboggage
					.onStatus(HttpStatus::is3xxRedirection, clientResponse -> Mono.error(new Exception("is3xxRedirection : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("is4xxClientError : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::is5xxServerError, clientResponse ->  Mono.error(new Exception("is5xxServerError : "+clientResponse.statusCode())))
					.onStatus(HttpStatus::isError, clientResponse -> Mono.error(new Exception("error : "+clientResponse.statusCode())))
					.bodyToMono(Void.class)
					.block();
	}
		
	protected ProductDTO retrieveProduct(String productId) {
		final String mname = "retrieveProduct";
		LOGGER.debug("entering " + mname);
		return this.webClient
						.get()
						.uri("/api/product/" + productId)
						.retrieve()
						.bodyToMono(ProductDTO.class)
						.block();
	}
	
	protected List<ProductDTO> retrieveProducts(String categoryId) {
		final String mname = "retrieveProducts";
		LOGGER.debug("entering " + mname);
		return this.webClient
						.get()
						.uri("/api/products/" + categoryId)
						.retrieve()
						.bodyToFlux(ProductDTO.class)
						.collectList()
						.block();	
	}
	
	protected String restUpdateProduct(ProductDTO productDTO, HttpServletRequest request, String cookie) {
		final String mname = "restUpdateProduct";
		LOGGER.debug("entering " + mname);

		final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");
		CsrfToken sessionToken = (CsrfToken) request.getSession().getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
		return this.webClient
				.put()
				.uri("/api/product/" + productDTO.getId())
				.header("X-CSRF-TOKEN", sessionToken.getToken()) // oubli => 403 FORBIDDEN
				.cookie("JSESSIONID", cookie) // oubli => 302 FOUND
				.body(Mono.just(productDTO), ProductDTO.class).retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::is3xxRedirection, clientResponse -> Mono.error(new Exception("is3xxRedirection : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("is4xxClientError : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError, clientResponse ->  Mono.error(new Exception("is5xxServerError : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::isError, clientResponse -> Mono.error(new Exception("error : "+clientResponse.statusCode())))
				.bodyToMono(String.class)
				.block();
	}

	protected void restDeleteProduct(String productId, HttpServletRequest request, String cookie) {
		final String mname = "restDeleteProduct";
		LOGGER.debug("entering " + mname);
//		LOGGER.debug("productId to delete : " + productId);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		this.webClient
				.delete()
				.uri("/api/product/" + productId)
				.header("X-CSRF-TOKEN", sessionToken.getToken())
				.cookie("JSESSIONID", cookie)
				.retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::is3xxRedirection, clientResponse -> Mono.error(new Exception("is3xxRedirection : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("is4xxClientError : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError, clientResponse ->  Mono.error(new Exception("is5xxServerError : "+clientResponse.statusCode())))
				.onStatus(HttpStatus::isError, clientResponse -> Mono.error(new Exception("error : "+clientResponse.statusCode())))
				.bodyToMono(Void.class)
				.block();
	}
	
	protected ItemDTO retrieveItem(String itemId) {
		final String mname = "findItem / retrieveItem";
		LOGGER.debug("entering "+mname);
		return this.webClient
						.get()
						.uri("/api/item/" + itemId)
						.retrieve()
						.bodyToMono(ItemDTO.class)
						.block();
	}
    
	protected List<ItemDTO> retrieveItems(String productId) {
		final String mname = "findItems / retrieveItems";
		LOGGER.debug("entering "+mname);
		return this.webClient
						.get()
						.uri("/api/items/" + productId)
						.retrieve()
						.bodyToFlux(ItemDTO.class)
						.collectList()
						.block();
	}
	
	protected String restUpdateItem(@Valid ItemDTO itemDTO, HttpServletRequest request, String cookie) {
		final String mname = "restUpdateItem";
		LOGGER.debug("entering " + mname);
		final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");
		CsrfToken sessionToken = (CsrfToken) request.getSession().getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
		return this.webClient
				.put()
				.uri("/api/item/" + itemDTO.getId())
				.header("X-CSRF-TOKEN", sessionToken.getToken()) // oubli => 403 FORBIDDEN
				.cookie("JSESSIONID", cookie) // oubli => 302 FOUND
				.body(Mono.just(itemDTO), ItemDTO.class)
				.retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::is3xxRedirection,clientResponse -> Mono.error(new Exception("is3xxRedirection : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError,clientResponse -> Mono.error(new Exception("is4xxClientError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError,clientResponse -> Mono.error(new Exception("is5xxServerError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::isError, clientResponse -> Mono.error(new Exception("error : " + clientResponse.statusCode())))
				.bodyToMono(String.class)
				.block();
	}
	
	protected void restDeleteItem(String itemId, HttpServletRequest request, String cookie) {
		final String mname = "restDeleteItem";
		LOGGER.debug("entering " + mname);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		this.webClient
				.delete()
				.uri("/api/item/" + itemId)
				.header("X-CSRF-TOKEN", sessionToken.getToken())
				.cookie("JSESSIONID", cookie)
				.retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::is3xxRedirection,clientResponse -> Mono.error(new Exception("is3xxRedirection : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError,clientResponse -> Mono.error(new Exception("is4xxClientError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError,clientResponse -> Mono.error(new Exception("is5xxServerError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::isError,clientResponse -> Mono.error(new Exception("error : " + clientResponse.statusCode())))
				.bodyToMono(Void.class)
				.block();
	}
	
	protected List<ItemDTO> retrieveItemsByKeyword(String keyword) {
    	final String mname = "retrieveItemsByKeyword";
		LOGGER.debug("entering " + mname);
		return this.webClient
				.get()
				.uri("/api/itemsByKeyword/" + keyword)
				.retrieve()
				.bodyToFlux(ItemDTO.class)
				.collectList()
				.block();
	}
    
	protected List<ItemDTO> retrieveItemsByPrice(String gtLt, double price) {
    	final String mname = "retrieveItemsByPrice";
		LOGGER.debug("entering " + mname);
		return this.webClient
				.get()
				.uri("/api/itemsByPrice/" + gtLt + "/"+ price)
				.retrieve()
				.bodyToFlux(ItemDTO.class)
				.collectList()
				.block();
	}
    
	protected List<ItemDTO> retrieveItemsByPriceAndKeyword(String gtLt, double price, String keyword) {
    	final String mname = "retrieveItemsByPriceAndKeyword";
		LOGGER.debug("entering " + mname);
		return this.webClient
				.get()
				.uri("/api/itemsByPriceAndKeyword/" + gtLt + "/"+ price+ "/"+ keyword)
				.retrieve()
				.bodyToFlux(ItemDTO.class)
				.collectList()
				.block();
	}
}