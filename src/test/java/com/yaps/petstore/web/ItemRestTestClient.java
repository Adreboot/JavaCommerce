package com.yaps.petstore.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaps.petstore.domain.dto.ItemDTO;
import com.yaps.petstore.domain.service.CatalogService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemRestTestClient {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private CatalogService cs;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void tesRestShowItems() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/items")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("EST1"));
		assertTrue(result.getResponse().getContentAsString().contains("Large"));
		assertTrue(result.getResponse().getContentAsString().contains("fish1.jpg"));
		assertTrue(result.getResponse().getContentAsString().contains("10"));
		assertTrue(result.getResponse().getContentAsString().contains("FISW01"));
		assertTrue(result.getResponse().getContentAsString().contains("Angelfish"));
		assertTrue(result.getResponse().getContentAsString().contains("Saltwater fish from Australia"));
		assertTrue(result.getResponse().getContentAsString().contains("EST12"));
		assertTrue(result.getResponse().getContentAsString().contains("Spotted Female Puppy"));
		assertTrue(result.getResponse().getContentAsString().contains("dog2.jpg"));
		assertTrue(result.getResponse().getContentAsString().contains("32"));
		assertTrue(result.getResponse().getContentAsString().contains("K9PO02"));
		assertTrue(result.getResponse().getContentAsString().contains("Poodle"));
		assertTrue(result.getResponse().getContentAsString().contains("Cute dog from France"));
	}
	
	@Test
	public void tesRestShowItemByProductId() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/items/AVCB01")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("EST25"));
		assertTrue(result.getResponse().getContentAsString().contains("Male Adult"));
		assertTrue(result.getResponse().getContentAsString().contains("120"));
		assertTrue(result.getResponse().getContentAsString().contains("bird2.jpg"));
		assertTrue(result.getResponse().getContentAsString().contains("AVCB01"));
		assertTrue(result.getResponse().getContentAsString().contains("Amazon Parrot"));
		assertTrue(result.getResponse().getContentAsString().contains("Great companion for up to 75 years"));
		assertTrue(result.getResponse().getContentAsString().contains("EST26"));
		assertTrue(result.getResponse().getContentAsString().contains("Female Adult"));
	}
	
	@Test
	public void tesRestShowItemByUnknownProductId() throws Exception {
		mockMvc.perform(get("/api/items/AVCB72")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}
	
	@Test
	public void tesRestShowItemByItemId() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/item/EST4")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("EST4"));
		assertTrue(result.getResponse().getContentAsString().contains("Spotless"));
		assertTrue(result.getResponse().getContentAsString().contains("12"));
		assertTrue(result.getResponse().getContentAsString().contains("fish4.jpg"));
		assertTrue(result.getResponse().getContentAsString().contains("FISW02"));
		assertTrue(result.getResponse().getContentAsString().contains("Tiger Shark"));
		assertTrue(result.getResponse().getContentAsString().contains("Saltwater fish from Australia"));
	}
	
	@Test
	public void tesRestShowItemByUnknownItemsId() throws Exception {
		mockMvc.perform(get("/api/item/EST44")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateItem() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setName("Big Male Adult");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/item/EST25")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isOk())
			       	.andReturn();
		System.err.println(result.getResponse().getContentAsString());
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"EST25\",\"name\":\"Big Male Adult\",\"unitCost\":120.0,\"imagePath\":\"bird2.jpg\",\"productDTO\":{\"id\":\"AVCB01\",\"name\":\"Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":null}}"));
	}
	
	@Test
	public void testRestUpdateItemNotAuthenticated() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setName("Big male Adult");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/item/EST25")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is3xxRedirection())
					.andReturn();
		assertTrue(result.getResponse().getRedirectedUrl().contains("/sso/login"));
		}
	
	@Test
	@WithMockUser(username = "bill000", password = "cnam", roles = "USER")
	public void testRestUpdateItemWithWrongRole() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setName("Big male Adult");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/item/EST25")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isForbidden())
					.andReturn();
		assertTrue(result.getResponse().getStatus()==403);
		}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateItemWithWrongId1() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setName("Big male Adult");
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/item/EST255")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateitemWithWrongId2() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setId("EST205");
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/item/EST205")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateItemWithNullId() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setId(null);
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/item/EST25")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestCreateItem() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setId("EST250");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(post("/api/item")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isCreated())
			       .andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"EST250\",\"name\":\"Male Adult\",\"unitCost\":120.0,\"imagePath\":\"bird2.jpg\",\"productDTO\":{\"id\":\"AVCB01\",\"name\":\"Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":null}}"));
		}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testRestCreateItemNotAutorized() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setId("EST250");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(post("/api/item")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isForbidden())
			       .andReturn();
		assertTrue(result.getResponse().getStatus()==403);
		}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestCreateInvalidItem() throws Exception {
		ItemDTO parrot = cs.findItem("EST25");
		parrot.setId(null);
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(post("/api/item")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestDeleteItem() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/item/EST25")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isNoContent())
			       .andReturn();
		assertTrue(result.getResponse().getStatus()==204); // successful DELETE should imply no content Http Status
		
		mockMvc.perform(get("/api/item/EST25")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound()); 	// deleted product should not be found
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestFailDeleteProduct() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/product/AVCB12") // AVCB12 product does not exist
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isNotFound())
			       .andReturn();
		assertTrue(result.getResponse().getStatus()==404);
	}
	
	@Test
	@WithMockUser(username = "bill000", password = "cnam", roles = "USER")
	public void testRestDeleteCategoryNotAuthorized() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/product/AVCB01")
				.with(csrf())
		       .contentType(MediaType.APPLICATION_JSON)
		       .accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isForbidden())
		       .andReturn();
		assertTrue(result.getResponse().getStatus()==403);
		}
}
