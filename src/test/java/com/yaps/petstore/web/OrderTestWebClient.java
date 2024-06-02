package com.yaps.petstore.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderTestWebClient {
	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private WebClient webClient;

	private HtmlPage fishPage, koiHtmlPage, SpecificKoiHtmlPage, basketHtmlPage, checkOutHtmlPage;

	private HtmlElement getKoisLink, getSpecificKoiLink, addToBasketLink, checkoutLink;

	@Test
	@WithMockUser(username = "job5", password = "cnam", roles = "USER")
	public void testOrderAsUser() throws Exception {
		fishPage = webClient.getPage("/find-products?categoryId=FISH");
		assertTrue(fishPage.getBody().asNormalizedText().contains("Liste de produits appartenant à la catégorie FISH"));

		getKoisLink = fishPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-items?productId=FIFW01");
		koiHtmlPage = getKoisLink.click();
		assertTrue(koiHtmlPage.getBody().asNormalizedText().contains("Liste des articles appartenant au produit Koi"));
		getSpecificKoiLink = koiHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-item?itemId=EST5");
		SpecificKoiHtmlPage = getSpecificKoiLink.click();
		assertTrue(SpecificKoiHtmlPage.getBody().asNormalizedText().contains("Koi sélectionné"));
		addToBasketLink = SpecificKoiHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/add-to-cart?itemId=EST5");
		basketHtmlPage = addToBasketLink.click();
		assertTrue(basketHtmlPage.getBody().asNormalizedText().contains("retirer du panier"));
		assertTrue(basketHtmlPage.getBody().asNormalizedText().contains("Freshwater fish from Japan"));
		assertTrue(basketHtmlPage.getBody().asNormalizedText().contains("TOTAL : 12.0"));
		checkoutLink = basketHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/checkout");
		checkOutHtmlPage = checkoutLink.click();
		assertTrue(checkOutHtmlPage.getBody().asNormalizedText().contains("Votre commande est finalisée"));	
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testOrderAsfranchisee() throws Exception {

		  mockMvc.perform(MockMvcRequestBuilders.get("/find-item?itemId=EST5")
		            .accept(MediaType.ALL))
		            .andExpect(status().isOk())
		            .andExpect(content().string(containsString("Koi sélectionné")));
		  
		  mockMvc.perform(MockMvcRequestBuilders.get("/add-to-cart?itemId=EST5")
		            .accept(MediaType.ALL))
		  			.andExpect(status().isOk())
		  			.andExpect(content().string(containsString("AccessDeniedException")));
		}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "FRANCHISEE")
	public void testOrderAsAdmin() throws Exception {

		  mockMvc.perform(MockMvcRequestBuilders.get("/find-item?itemId=EST5")
		            .accept(MediaType.ALL))
		            .andExpect(status().isOk())
		            .andExpect(content().string(containsString("Koi sélectionné")));
		  
		  mockMvc.perform(MockMvcRequestBuilders.get("/add-to-cart?itemId=EST5")
		            .accept(MediaType.ALL))
		  			.andExpect(status().isOk())
		  			.andExpect(content().string(containsString("AccessDeniedException")));
		}
	
}
