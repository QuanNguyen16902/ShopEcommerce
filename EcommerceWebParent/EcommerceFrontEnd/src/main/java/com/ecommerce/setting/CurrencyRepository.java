package com.ecommerce.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	
}
