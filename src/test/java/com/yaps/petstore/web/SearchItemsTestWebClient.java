package com.yaps.petstore.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchItemsTestWebClient {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebClient webClient;

	private HtmlPage priceSearchPage, itemsPage;

	@Test
	@WithMockUser(username = "bill000", password = "cnam", roles = "USER")
	public void testSearchItemByKeywordAndPrice() throws Exception {

		priceSearchPage = webClient.getPage("/price-search");
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Recherche d'articles par prix"));
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Rechercher les articles contenant ..."));
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("keyword").setValueAttribute("female");
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getRadioButtonsByName("gtLt").get(1).setChecked(true);
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("price").setValueAttribute("20");
		itemsPage = priceSearchPage.getFormByName("priceAndKeywordSearchForm").getButtonByName("searchByPriceButton").click();

		assertTrue(itemsPage.getBody().asNormalizedText().contains("Liste des articles correspondant au mot clé ... female dont le prix est supérieur à 20.0 euros"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("EST26 - Female Adult"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("Great for reducing mouse populations"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("22.0"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("120.0"));
		
		mockMvc.perform(logout());
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testSearchItemByKeywordAndPrice2() throws Exception {

		priceSearchPage = webClient.getPage("/price-search");
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Recherche d'articles par prix"));
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Rechercher les articles contenant ..."));
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("keyword").setValueAttribute("Tailless");
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getRadioButtonsByName("gtLt").get(0).setChecked(true);
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("price").setValueAttribute("82");
		itemsPage = priceSearchPage.getFormByName("priceAndKeywordSearchForm").getButtonByName("searchByPriceButton").click();

		assertTrue(itemsPage.getBody().asNormalizedText().contains("Liste des articles correspondant au mot clé ... Tailless dont le prix vaut exactement 82.0 euros"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("EST16 - Tailless"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("Great family dog"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("82.0"));
		
		mockMvc.perform(logout());
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testSearchItemByKeywordAndPrice3() throws Exception {

		priceSearchPage = webClient.getPage("/price-search");
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Recherche d'articles par prix"));
		assertTrue(priceSearchPage.getBody().asNormalizedText().contains("Rechercher les articles contenant ..."));
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("keyword").setValueAttribute("Spotted");
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getRadioButtonsByName("gtLt").get(2).setChecked(true);
		priceSearchPage.getFormByName("priceAndKeywordSearchForm").getInputByName("price").setValueAttribute("20");
		itemsPage = priceSearchPage.getFormByName("priceAndKeywordSearchForm").getButtonByName("searchByPriceButton").click();

		assertTrue(itemsPage.getBody().asNormalizedText().contains("Liste des articles correspondant au mot clé ... Spotted dont le prix est inférieur à 20.0 euros"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("EST3 - Spotted"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("Saltwater fish from Australia"));
		assertTrue(itemsPage.getBody().asNormalizedText().contains("12.0"));
		
		mockMvc.perform(logout());
	}
	
	@Test
	public void testSearchItemByKeywordAndPrice4() throws Exception {
		try {
			priceSearchPage = webClient.getPage("/price-search");
			} catch (com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException e) {
				assertTrue(e.getMessage().contains("Too much redirect for http://localhost:8089/auth/realms/PetstoreKeycloak/protocol/openid-connect/auth?response_type=code&client_id=petstore-app&redirect_uri=http%3A%2F%2Flocalhost%3A8089%2Fsso%2Flogin?response_type%3Dcode%26client_id%3Dpetstore-app%26"));
			}
	}
}