package com.yaps.petstore.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
public class ManageCatalogTestWebClient {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebClient webClient;

	private HtmlPage catManagePage, prodManagePage, itemManagePage, catUpdateHtmlPage, prodUpdateHtmlPage, itemUpdateHtmlPage;

	private HtmlElement itemUpdateLink, catUpdateLink, prodUpdateLink ;
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testWebUpdateCategory() throws Exception {
		catManagePage = webClient.getPage("/manage/manage-categories");
		assertTrue(catManagePage.getBody().asNormalizedText().contains("Gestion du catalogue"));
		assertTrue(catManagePage.getBody().asNormalizedText().contains("Catégories"));

		catUpdateLink = catManagePage.getBody().getOneHtmlElementByAttribute("a", "href", "/manage/update-category/BIRDS");
		catUpdateHtmlPage = catUpdateLink.click();
		assertTrue(catUpdateHtmlPage.getBody().asNormalizedText().contains("Modification d'une catégorie"));
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testWebUpdateproduct() throws Exception {
		try {
			webClient.getPage("/manage/manage-products").getWebResponse();
		} catch (Exception e) {
			assertTrue(e instanceof FailingHttpStatusCodeException);
		}
		prodManagePage = webClient.getPage("/manage/manage-products/BIRDS");
		assertTrue(prodManagePage.getBody().asNormalizedText().contains("Gestion du catalogue"));
		assertTrue(prodManagePage.getBody().asNormalizedText().contains("Produits"));

		prodUpdateLink = prodManagePage.getBody().getOneHtmlElementByAttribute("a", "href", "/manage/update-product/AVCB01");
		prodUpdateHtmlPage = prodUpdateLink.click();
		assertTrue(prodUpdateHtmlPage.getBody().asNormalizedText().contains("Modification d'un produit"));
	}

	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testWebUpdateItem() throws Exception {
		/* items */
		try {
			webClient.getPage("/manage/manage-items").getWebResponse();
		} catch (Exception e) {
			assertTrue(e instanceof FailingHttpStatusCodeException);
		}
		itemManagePage = webClient.getPage("/manage/manage-items/AVCB01");
		assertTrue(itemManagePage.getBody().asNormalizedText().contains("Gestion du catalogue"));
		assertTrue(itemManagePage.getBody().asNormalizedText().contains("Articles"));

		itemUpdateLink = itemManagePage.getBody().getOneHtmlElementByAttribute("a", "href", "/manage/update-item/EST25");
		itemUpdateHtmlPage = itemUpdateLink.click();
		assertTrue(itemUpdateHtmlPage.getBody().asNormalizedText().contains("Modification d'un article"));
//		itemManagePage = itemUpdateHtmlPage.getFormByName("itemUpdateForm").getButtonByName("itemUpdateButton").click();
//		assertTrue(itemManagePage.getBody().asNormalizedText().contains("EST25 a bien été modifié"));
	}

	/**
	 * A user should not be able to manage the catalog
	 */
	@Test
    @WithMockUser(username = "bill000", password = "cnam", roles = "USER")
	public void testWebUpdateCatalogAsUser() throws Exception {
		try {
			webClient.getPage("/manage/manage-categories");
		} catch (FailingHttpStatusCodeException e) {
			assertTrue(e.getMessage().contains("403 Forbidden"));
		}
		try {
			webClient.getPage("/manage/manage-products/BIRDS");
		} catch (FailingHttpStatusCodeException e) {
			assertTrue(e.getMessage().contains("403 Forbidden"));
		}
		try {
			webClient.getPage("/manage/manage-items/AVCB01");
		} catch (FailingHttpStatusCodeException e) {
			assertTrue(e.getMessage().contains("403 Forbidden"));
		}
		mockMvc.perform(logout());
	}

}
