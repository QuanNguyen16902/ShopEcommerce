package com.ecommerce.admin.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	public List<Currency> findAllByOrderByNameAsc();
	
	
}
