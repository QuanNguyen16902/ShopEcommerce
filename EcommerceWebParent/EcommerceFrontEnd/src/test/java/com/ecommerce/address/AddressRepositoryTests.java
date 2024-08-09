package com.ecommerce.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {
	@Autowired private AddressRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer1() {
		Integer customerId = 5;
		Integer countryId = 234;
		
		Address newAddress = new Address();
		newAddress.setCountry(new Country(countryId));
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setFirstName("Toby");
		newAddress.setLastName("Abel");
		newAddress.setPhoneNumber("190-2415-224");
		newAddress.setAddressLine1("4213 Golden Street");
		newAddress.setAddressLine2("Nova Building");
		newAddress.setCity("Chino");
		newAddress.setState("California");
		newAddress.setPostalCode("91710");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress.getId()).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateAddress() {
		Integer id = 3;
		Address address = repo.findById(id).get();
		
		address.setDefaultForShipping(true);
		
		Address savedAddress = repo.save(address);
//		assertThat(savedAddress.getPhoneNumber()).is
	}
	
	@Test
	public void testSetDefault() {
		Integer id = 5;
		repo.setDefaultAddress(id);
		
		Address address = repo.findById(id).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefault() {
		Integer id = 5;
		Integer customerId = 5;
		repo.setNonDefaultAddressForOthers(id, customerId);

	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 5;
		Address address = repo.findDefaultByCustomer(customerId);
		
		assertThat(address).isNotNull();
		System.out.println(address);
		
	}
	
}
