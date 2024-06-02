package com.yaps.petstore.web;

import static org.junit.Assert.assertFalse;
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
import com.yaps.petstore.domain.dto.ProductDTO;
import com.yaps.petstore.domain.service.CatalogService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductRestTestClient {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private CatalogService cs;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void tesRestShowProducts() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/products")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("AVCB01"));
		assertTrue(result.getResponse().getContentAsString().contains("Amazon Parrot"));
		assertTrue(result.getResponse().getContentAsString().contains("Great companion for up to 75 years"));
		assertTrue(result.getResponse().getContentAsString().contains("BIRDS"));
		assertTrue(result.getResponse().getContentAsString().contains("Birds"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}
	
	@Test
	public void tesRestShowProductByCategoryId() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/products/REPTILES")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("RPLI02"));
		assertTrue(result.getResponse().getContentAsString().contains("Iguana"));
		assertTrue(result.getResponse().getContentAsString().contains("Friendly green friend"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
	}
	
	@Test
	public void tesRestShowProductByUnknownCategoryId() throws Exception {
		mockMvc.perform(get("/api/products/DINOS")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}
	
	@Test
	public void tesRestShowProductByProductId() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/product/RPSN01")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		assertFalse(result.getResponse().getContentAsString().contains("AVCB01"));
		assertFalse(result.getResponse().getContentAsString().contains("Amazon Parrot"));
		assertFalse(result.getResponse().getContentAsString().contains("Great companion for up to 75 years"));
		assertFalse(result.getResponse().getContentAsString().contains("BIRDS"));
		assertFalse(result.getResponse().getContentAsString().contains("Birds"));
		assertFalse(result.getResponse().getContentAsString().contains("Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}
	
	@Test
	public void tesRestShowProductByUnknownProductId() throws Exception {
		mockMvc.perform(get("/api/product/RPSN001")
			       	.contentType(MediaType.APPLICATION_JSON)
			       	.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateProduct() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setName("Big Amazon Parrot");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/product/AVCB01")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isOk())
			       	.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"AVCB01\",\"name\":\"Big Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":{\"id\":\"BIRDS\",\"name\":\"Birds\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}}"));
	}
	
	@Test
	public void testRestUpdateProductNotAuthenticated() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setName("Big Amazon Parrot");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/product/AVCB01")
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
	public void testRestUpdateProductWithWrongRole() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setName("Big Amazon Parrot");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(put("/api/product/AVCB01")
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
	public void testRestUpdateProductWithWrongId1() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setName("Big Amazon Parrot");
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/product/AVSB02")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateProductWithWrongId2() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setId("AVCB101");
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/product/AVCB101")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateProductWithNullId() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setId(null);
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(put("/api/product/AVCB01")
					.with(csrf())
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.accept(MediaType.APPLICATION_JSON))
			       	.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestCreateProduct() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setId("AVCB001");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(post("/api/product")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isCreated())
			       .andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"AVCB001\",\"name\":\"Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":{\"id\":\"BIRDS\",\"name\":\"Birds\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}}"));
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testRestCreateProductNotAutorized() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setId("AVCB001");
		String json = objectMapper.writeValueAsString(parrot);
		MvcResult result = mockMvc.perform(post("/api/product")
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
	public void testRestCreateInvalidProduct() throws Exception {
		ProductDTO parrot = cs.findProduct("AVCB01");
		parrot.setId(null);
		String json = objectMapper.writeValueAsString(parrot);
		mockMvc.perform(post("/api/product")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestDeleteProduct() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/product/AVCB01")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isNoContent())
			       .andReturn();
		assertTrue(result.getResponse().getStatus()==204); // successful DELETE should imply no content Http Status
		
		mockMvc.perform(get("/api/product/AVCB01")
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
