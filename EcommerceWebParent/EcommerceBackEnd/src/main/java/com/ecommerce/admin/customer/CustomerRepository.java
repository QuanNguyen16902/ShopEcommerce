package com.ecommerce.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.common.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
			+ "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state,"
			+ "' ', c.postalCode, ' ', c.country.name) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pagealbe);
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public Long countById(Integer id);
}
