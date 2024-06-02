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
import com.yaps.petstore.domain.dto.CategoryDTO;
import com.yaps.petstore.domain.service.CatalogService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CatagoryRestTestClient {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private CatalogService cs;
	
	@Autowired
	private ObjectMapper objectMapper;
		
	@Test
	public void tesRestShowCategories() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/categories")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn()
			    	;
		assertTrue(result.getResponse().getContentAsString().contains("BIRDS"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("CATS"));
		assertTrue(result.getResponse().getContentAsString().contains("DOGS"));
		assertTrue(result.getResponse().getContentAsString().contains("FISH"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}
	
	@Test
	public void tesRestShowCategoryById() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/category/BIRDS")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn()
			    	;
		assertTrue(result.getResponse().getContentAsString().contains("BIRDS"));
		assertTrue(result.getResponse().getContentAsString().contains("Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertFalse(result.getResponse().getContentAsString().contains("CATS"));
		assertFalse(result.getResponse().getContentAsString().contains("DOGS"));
		assertFalse(result.getResponse().getContentAsString().contains("FISH"));
		assertFalse(result.getResponse().getContentAsString().contains("REPTILES"));
		assertFalse(result.getResponse().getContentAsString().contains("Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}
	
	@Test
	public void tesRestShowCategoryByUnknownCategoryId() throws Exception {
		mockMvc.perform(get("/api/category/RPSN001")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateCategory() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setName("Birdies");
		String json = objectMapper.writeValueAsString(birds);
		MvcResult result = mockMvc.perform(put("/api/category/BIRDS")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isOk())
			       .andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"BIRDS\",\"name\":\"Birdies\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}"));
	}
	
	@Test
	public void testRestUpdateCategoryNotAuthenticated() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setName("Birdies");
		String json = objectMapper.writeValueAsString(birds);
		MvcResult result = mockMvc.perform(put("/api/category/BIRDS")
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
	public void testRestUpdateCategoryWithWrongRole() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setName("Birdies");
		String json = objectMapper.writeValueAsString(birds);
		MvcResult result = mockMvc.perform(put("/api/category/BIRDS")
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
	public void testRestUpdateCategoryWithWrongId1() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setName("Birdies");
		String json = objectMapper.writeValueAsString(birds);
		mockMvc.perform(put("/api/category/DOGS")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateCategoryWithWrongId2() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setId("BIRDIES");
		String json = objectMapper.writeValueAsString(birds);
		mockMvc.perform(put("/api/category/BIRDIES")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestUpdateCategoryWithNullId() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setId(null);
		String json = objectMapper.writeValueAsString(birds);
		mockMvc.perform(put("/api/category/BIRDS")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isBadRequest());
	}
	
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestCreateCategory() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setId("BIRDIES");
		String json = objectMapper.writeValueAsString(birds);
		MvcResult result = mockMvc.perform(post("/api/category")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
			       .andExpect(status().isCreated())
			       .andReturn();
		assertTrue(result.getResponse().getContentAsString().equals("{\"id\":\"BIRDIES\",\"name\":\"Birds\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}"));
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testRestCreateCategoryNotAuthorized() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setId("BIRDIES");
		String json = objectMapper.writeValueAsString(birds);
		MvcResult result = mockMvc.perform(post("/api/category")
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
	public void testRestCreateInvalidCategory() throws Exception {
		CategoryDTO birds = cs.findCategory("BIRDS");
		birds.setId(null);
		String json = objectMapper.writeValueAsString(birds);
		mockMvc.perform(post("/api/category")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .content(json)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestDeleteCategory() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/category/BIRDS")
					.with(csrf())
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNoContent())
			       .andReturn();
		assertTrue(result.getResponse().getStatus()==204); // successful DELETE should imply no content Http Status
		
		mockMvc.perform(get("/category/BIRDS")
			       .contentType(MediaType.APPLICATION_JSON)
			       .accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound()); 	// deleted category should not be found
	}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "ADMIN")
	public void testRestFailedDeleteCategory() throws Exception {
		MvcResult result = mockMvc.perform(delete("/api/category/BIRDIES")		// No such category
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
		MvcResult result = mockMvc.perform(delete("/api/category/BIRDS")
				.with(csrf())
		       .contentType(MediaType.APPLICATION_JSON)
		       .accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isForbidden())
		       .andReturn();
		assertTrue(result.getResponse().getStatus()==403);
		}
	
}
