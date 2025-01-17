package com.ecommerce.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.common.entity.Currency;
import com.ecommerce.common.entity.setting.Setting;
import com.ecommerce.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	CurrencyRepository repo;

	@Test
	public void testCreateGerenalSettings() {
		List<Currency> listCurrencies = Arrays.asList(
				new Currency("United States Dollar", "$", "USD"),
				new Currency("British", "£", "GBP"),
				new Currency("Japanese Yen", "¥", "JPY"),
				new Currency("Euro","€", "EUR"),
				new Currency("Russian Ruble", "₽", "RUB"),
				new Currency("South Korean Won", "₩", "KRW"),
				new Currency("Chinese Yuan" ,"¥", "CNY"),
				new Currency("Brazilian Real", "R$", "BRL"),
				new Currency("Australian Dollar", "$", "AUD"),
				new Currency("Canadian Dollar", "$", "CAD"),
				new Currency("Vietnamese đồng", "đ", "VND"),
				new Currency("Indian Rupee", "₹", "INR")
				);
		
		repo.saveAll(listCurrencies);
		Iterable<Currency> iterable = repo.findAll();
		
		assertThat(iterable).size().isEqualTo(12);
	} 
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		assertThat(currencies.size()).isGreaterThan(0);
	}
}
