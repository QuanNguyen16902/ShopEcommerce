package com.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.common.entity.Customer;
import com.ecommerce.customer.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired private CustomerRepository repo;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		
		if(customer != null) {
			return new CustomerUserDetails(customer);
		}
		
		throw new UsernameNotFoundException("No customer found with the email " + email);
	}

}
