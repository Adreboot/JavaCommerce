package com.yaps.petstore.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


/**
 * This class tests the HTML Pages and controllers
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebTestMockMvc {
    
    @Autowired
    private MockMvc mockMvc;

    /**
     * Checks that all pages are deployed
     */
    @Test
    public void testWebCheckPages() {
        try {
        	this.mockMvc.perform(get("/dummy.html")).andExpect(status().is3xxRedirection());
        } catch (Exception e) {
        }

        try {
        	this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("The YAPS Pet Store Demo for Spring Boot is a fictional sample application")));
        } catch (Exception e) {
            fail("Root / hasn't been found");
        }

        try {
        	this.mockMvc.perform(get("/createcustomer.html")).andExpect(status().is3xxRedirection());
        } catch (Exception e) {
        }

   }

    @Test
    public void testWebCheckDeployedFindItem() {
        try {
        	this.mockMvc.perform(get("/find-item?itemId=EST25")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("Amazon Parrot sélectionné")));
        } catch (Exception e) {
            fail("The FindItem Controller hasn't been found");
        }
    }
    
    @Test
    public void testWebCheckDeployedFindItems() {
        try {
        	this.mockMvc.perform(get("/find-items?productId=AVCB01")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("Liste des articles appartenant au produit Amazon Parrot")));
        } catch (Exception e) {
            fail("The FindItems Controller hasn't been found");
        }
    }
    
 // Ce test nécessite que TP12 soit lancé
    @Test
    public void testWebCheckDeployedFindProducts() {
        try {
        	this.mockMvc.perform(get("/find-products?categoryId=BIRDS")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("Liste de produits appartenant à la catégorie BIRDS")));
        } catch (Exception e) {
            fail("The FindProducts Controller hasn't been found");
        }
    }

    @Test
    public void testWebCheckServlets() {

        try {
        	this.mockMvc.perform(get("/new-account")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("Nouveau compte")));
        } catch (Exception e) {
            fail("The new-account Controller hasn't been found");
        }
    }

    /**
     * Checks franchisee and customer listing
     */
    @Test
    @WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
    public void testWebCheckFranchiseeDisplay() {
    	try {
			this.mockMvc.perform(get("/display-franchisees")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("Liste des franchisés")));
		} catch (Exception e) {
			fail("displayFranchisees has failed");
		}
    }
    
    @Test
    @WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
    public void testWebCheckCustomerDisplay() {
    	try {
			this.mockMvc.perform(get("/display-customers")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("Liste des clients")));
		} catch (Exception e) {
			fail("displayCustomers has failed");
		}
    }
    
    /**
     * Checks franchisee and customer listing without proper authority level
     */
    @Test
    @WithMockUser(username = "bill000", password = "cnam", roles = "USER")
    public void testWebInvalidCheckFranchiseeDisplay() {
    	try {
			this.mockMvc.perform(get("/display-franchisees")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
    }
    
    @Test
    @WithMockUser(username = "bill000", password = "cnam", roles = "USER")
    public void testWebInvalidCheckCustomerDisplay() {
    	try {
			this.mockMvc.perform(get("/display-customers")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
    }
    
    /**
     * Checks franchisee and customer listing logged as a franchisee
     */
    @Test
    @WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
    public void testWebCheckCustomerDisplayAsFranchisee() {
    	try {
			this.mockMvc.perform(get("/display-franchisees")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
    	
    	try {
			this.mockMvc.perform(get("/display-customers")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("Liste des clients")));
		} catch (Exception e) {
			fail("displayCustomers has failed");
		}
    }    
    
}
