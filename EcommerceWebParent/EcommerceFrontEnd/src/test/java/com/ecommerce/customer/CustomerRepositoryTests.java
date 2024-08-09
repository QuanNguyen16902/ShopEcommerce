package com.ecommerce.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.common.entity.AuthenticationType;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	
	@Autowired private CustomerRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Fountaine");
		customer.setPassword("pass123");
		customer.setEmail("david@gmail.com");
		customer.setPhoneNumber("312-425-242");
		customer.setAddressLine1("1927 West Drive");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("95875");
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer.getId()).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId = 106;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Sanya");
		customer.setLastName("Lad");
		customer.setPassword("pass456");
		customer.setEmail("sanua@gmail.com");
		customer.setPhoneNumber("312-425-242");
		customer.setAddressLine1("173 West Drive");
		customer.setCity("Mumbai");
		customer.setState("Maharashtra");
		customer.setPostalCode("40013");
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer.getId()).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Stanfield";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repo.save(customer);
		
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isPresent();
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void  testAuthenticationType() {
		Integer id = 1;
		repo.updateAuthenticationType(1, AuthenticationType.FACEBOOK);
		
		Customer customer = repo.findById(id).get();
		
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
	}
	
}
