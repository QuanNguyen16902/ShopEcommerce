package com.ecommerce.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.State;

public interface StateRepository extends JpaRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
