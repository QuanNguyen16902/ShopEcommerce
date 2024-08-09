package com.ecommerce.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.common.entity.AuthenticationType;
import com.ecommerce.common.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
	@Query("Update Customer c SET c.enabled = true, c.verificationCode = null where c.id = ?1")
	@Modifying
	public void enable(Integer id); 
	
	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAuthenticationType(Integer customerId, AuthenticationType type);

	@Query("SELECT c FROM Customer c WHERE c.resetPasswordToken = ?1")
	public Customer findByResetPasswordToken(String token);
}
