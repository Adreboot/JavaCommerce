package com.yaps.petstore.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class UpdateAccountTestWebClient {
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
    private MockMvc mockMvc;
	
	private HtmlPage updateAccountPage, displayCustomersPage;
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
    public void a_testWebUpdateAccount() throws Exception {
    	updateAccountPage = webClient.getPage("/update-account/jeff01");
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("Modifiez votre compte"));

        updateAccountPage.getFormByName("update-account-form").getInputByName("telephone").setValueAttribute("0102030405");
        updateAccountPage.getFormByName("update-account-form").getInputByName("password").setValueAttribute("cnam");
        updateAccountPage = updateAccountPage.getFormByName("update-account-form").getButtonByName("update-account-button").click();
        	
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("account updated"));     
        
        mockMvc.perform(logout());
    }
   
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
    public void b_testWebCheckAccountAfterUpdate() throws Exception  {	
		displayCustomersPage = webClient.getPage("/display-customers");
		assertTrue("could not login after updating account. Something broken ?", displayCustomersPage.getBody().asNormalizedText().contains("Liste des clients"));   
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
    public void c_testWebUpdateAccountWithInvalidValues() throws Exception {
    	updateAccountPage = webClient.getPage("/update-account/jeff01");
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("Modifiez votre compte"));

        updateAccountPage.getFormByName("update-account-form").getInputByName("firstname").setValueAttribute("");
        updateAccountPage.getFormByName("update-account-form").getInputByName("password").setValueAttribute("cnam");
        updateAccountPage = updateAccountPage.getFormByName("update-account-form").getButtonByName("update-account-button").click();
        	
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("Invalid customer first name"));      
    }
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
    public void d_testWebUpdateAccountWithInvalidCCValues() throws Exception {
    	updateAccountPage = webClient.getPage("/update-account/jeff01");
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("Modifiez votre compte"));

        updateAccountPage.getFormByName("update-account-form").getInputByName("password").setValueAttribute("cnam");
        updateAccountPage.getFormByName("update-account-form").getInputByName("creditCardType").setValueAttribute("Visa");
        updateAccountPage.getFormByName("update-account-form").getInputByName("creditCardNumber").setValueAttribute("4564 1231 4564 1221");
        updateAccountPage.getFormByName("update-account-form").getInputByName("creditCardExpiryDate").setValueAttribute("10/23");
        updateAccountPage = updateAccountPage.getFormByName("update-account-form").getButtonByName("update-account-button").click();
        	
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("INVALID CREDIT CARD NUMBER"));      
    }
	
	@Test
    public void f_testWebDeleteUnknownAccount() throws Exception {
    	updateAccountPage = webClient.getPage("/delete-account/unknown");
        assertTrue(updateAccountPage.getBody().asNormalizedText().contains("User could not be deleted. 404 Not Found from DELETE"));
    }
}
