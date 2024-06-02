package com.yaps.petstore.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.yaps.petstore.authentication.domain.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FindUsersControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;

	@Test
	@WithMockUser(roles = "ADMIN")
	public void accesDisplayFranchiseesWithRoleAdmin() throws Exception 	{
	    mockMvc.perform(MockMvcRequestBuilders.get("/display-franchisees")
	            .accept(MediaType.ALL))
	            .andExpect(status().isOk())
	            .andExpect(content().string(containsString("username")));
	}
	
	@Test
	@WithMockUser(roles = "FRANCHISEE")
	public void accesDisplayFranchiseesWithRoleFranchisee() throws Exception 	{
	    mockMvc.perform(MockMvcRequestBuilders.get("/display-franchisees")
	            .accept(MediaType.ALL))
	    		.andExpect(status().isOk())
	    		.andExpect(content().string(containsString("Exception")));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void accesDisplayCustomersWithRoleAdmin() throws Exception 	{
	    mockMvc.perform(MockMvcRequestBuilders.get("/display-customers")
	            .accept(MediaType.ALL))
	            .andExpect(status().isOk())
	            .andExpect(content().string(containsString("username")));
	}
	
	@Test
	@WithMockUser(roles = "FRANCHISEE")
	public void accesDisplayCustomersWithRoleFranchisee() throws Exception 	{
	    mockMvc.perform(MockMvcRequestBuilders.get("/display-customers")
	            .accept(MediaType.ALL))
	            .andExpect(status().isOk())
	            .andExpect(content().string(containsString("username")));
	}
	
	@Test
	@WithMockUser(roles = "USER")
	public void accesDisplayCustomersWithRoleUser() throws Exception 	{
	    mockMvc.perform(MockMvcRequestBuilders.get("/display-customers")
	            .accept(MediaType.ALL))
	            .andExpect(status().isOk())
	            .andExpect(content().string(containsString("Exception")));
	}

}
