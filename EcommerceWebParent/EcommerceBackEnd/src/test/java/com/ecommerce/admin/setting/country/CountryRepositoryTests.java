package com.ecommerce.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.admin.setting.country.CountryRepository;
import com.ecommerce.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {

	@Autowired
	private CountryRepository repo;
	
	@Test
	public void testCreateCountry() {
//		Country country = repo.save(new Country("China", "CN")); 
		
//		Country country = repo.save(new Country("United States", "US")); 
//		Country country = repo.save(new Country("United Kingdom", "UK")); 
		Country country = repo.save(new Country("Vietnam", "VN")); 
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllCountry() {
		List<Country> countries = repo.findAllByOrderByNameAsc();
		countries.forEach(System.out::println);
		
		assertThat(countries.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdatecountry() {
		int countryId = 1;
		String name = "Republic Of China";
		Country country = repo.findById(countryId).get();
		
		country.setName(name);
		Country updatedCountry = repo.save(country);
		
		assertThat(updatedCountry.getName()).isEqualTo(name);
	}
}
