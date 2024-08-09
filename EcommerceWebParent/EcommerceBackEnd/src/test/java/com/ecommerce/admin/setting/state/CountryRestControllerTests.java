package com.ecommerce.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ecommerce.admin.setting.country.CountryRepository;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
//	@Autowired TestEntityManager entityManager;
	
	@Autowired CountryRepository countryRepo;
	@Autowired StateRepository stateRepo;
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testListCountries() throws Exception {
		
		Integer countryId = 2;
		String url = "/states/list_by_country/" + countryId;
		
		MvcResult result = 	mockMvc.perform(get(url))
								.andExpect(status().isOk())
								.andDo(print())
								.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
//		System.out.println(jsonResponse);
		
		State[]	states = objectMapper.readValue(jsonResponse, State[].class);
//		for(Country country : countries) {
//			System.out.println(country);
//		}
		 
		assertThat(states).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId = 2;
//		String countryName = "Germany";
//		String countryCode = "DE";
		Country country = countryRepo.findById(countryId).get();
		State state = new State("Arizona", country);
		
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(state))
					.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testUpdateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer stateId = 2;
		String stateName = "Alaska";
		
		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);
		
		mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(state))
					.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(stateId)));
		
//		String response = result.getResponse().getContentAsString();
//		Integer countryId = Integer.parseInt(response);
		
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
		
		State updateState = findById.get();
		assertThat(updateState.getName()).isEqualTo(stateName);
//		System.out.println("Country ID: " + response);
	}
	
	@Test
	@WithMockUser(username = "213@gmail.com", password = "12414", roles = "Admin")
	public void testDeleteCountry() throws Exception {
		Integer stateId = 6;
		String url = "/states/delete/" + stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		
		Optional<State> findById = stateRepo.findById(stateId);
		
		assertThat(findById).isNotPresent();
	}
}
