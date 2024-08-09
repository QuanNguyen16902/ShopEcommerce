package com.ecommerce.shipping;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.ShippingRate;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer>{
	public ShippingRate findByCountryAndState(Country country, String state);
	
}
