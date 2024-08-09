package com.ecommerce.customer;

import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.ecommerce.common.entity.AuthenticationType;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.exception.CustomerNotFoundException;
import com.ecommerce.setting.CountryRepository;

import jakarta.transaction.Transactional;

import java.lang.String;
@Service
@Transactional
public class CustomerService {
	
	@Autowired private CountryRepository countryRepo;
	@Autowired private CustomerRepository customerRepo;
	
	 private PasswordEncoder passwordEncoder;
	 @Autowired @Lazy
	 private void passwordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	public void registerCustomer(Customer customer)
	{
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		
		String randomCode = StringUtils.randomAlphanumeric(64);
		customer.setVerificationCode(randomCode);
		
		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
		
		if(customer == null || customer.isEnabled()) {
			return false;
		}else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		
		setName(name, customer);
		
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));
		
		customerRepo.save(customer);
	}
	
	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName(""); 
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}
	
	public void update(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		 
		if(customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			
			if(!customerInForm.getPassword().isEmpty()) {
				encodePassword(customerInForm);
			}else {
				customerInForm.setPassword(customerInDB.getPassword());
			}
		}else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		 
		 customerInForm.setEnabled(customerInDB.isEnabled());
		 customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		 customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		 customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		 customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
		 customerRepo.save(customerInForm);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepo.findByEmail(email);
		if(customer != null) {
			String token = StringUtils.randomAlphanumeric(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);
			return token;
		}else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
	}
	
	public Customer getByResetPasswordToken(String token) {
		return customerRepo.findByResetPasswordToken(token);
	}
	
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if(customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}
		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		encodePassword(customer);

		customerRepo.save(customer);
	}
}
