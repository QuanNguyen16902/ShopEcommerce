package com.ecommerce.admin.setting.country;

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

import com.ecommerce.common.entity.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
//	@Autowired TestEntityManager entityManager;
	
	@Autowired CountryRepository repo;
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testListCountries() throws Exception {
		String url = "/countries/list";
		
		MvcResult result = 	mockMvc.perform(get(url))
								.andExpect(status().isOk())
								.andDo(print())
								.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
//		System.out.println(jsonResponse);
		
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
//		for(Country country : countries) {
//			System.out.println(country);
//		}
		
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
//		Integer countryId = 7;
		String countryName = "Germany";
		String countryCode = "DE";
		Country country = new Country(countryName, countryCode);
		
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(country))
					.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(response);
		
		Optional<Country> findById = repo.findById(countryId);
		assertThat(findById.isPresent());
		
		Country savedCountry = findById.get();
		
		assertThat(savedCountry.getName()).isEqualTo(countryName);
//		System.out.println("Country ID: " + response);
	}
	
	@Test
	@WithMockUser(username = "123@gmail.com", password = "q234", roles = "Admin, Editor")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		Integer countryId = 7;
		String countryName = "Singapore";
		String countryCode = "SN";
		Country country = new Country(countryId, countryName, countryCode);
		
		mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(country))
					.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(countryId)));
		
//		String response = result.getResponse().getContentAsString();
//		Integer countryId = Integer.parseInt(response);
		
		Optional<Country> findById = repo.findById(countryId);
		assertThat(findById.isPresent());
		
		Country savedCountry = findById.get();
		
		assertThat(savedCountry.getName()).isEqualTo(countryName);
//		System.out.println("Country ID: " + response);
	}
	
	@Test
	@WithMockUser(username = "213@gmail.com", password = "12414", roles = "Admin")
	public void testDeleteCountry() throws Exception {
		Integer countryId = 7;
		String url = "/countries/delete/" + countryId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		
		Optional<Country> findById = repo.findById(countryId);
		
		assertThat(findById).isNotPresent();
	}
}
