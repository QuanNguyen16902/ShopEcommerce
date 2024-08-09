package com.ecommerce.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ecommerce.common.entity.setting.Setting;
import com.ecommerce.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
	
	@Autowired SettingRepository repo;
	
	@Test
	public void testCreateGerenalSettings() {
//		 Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
//		 Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Ecommerce Ltd.", SettingCategory.GENERAL);
		 Setting currencyId = new Setting("CURRENCY_ID", "2" , SettingCategory.CURRENCY);
		 Setting symbol = new Setting("CURRENCY_SYMBOL", "$" , SettingCategory.CURRENCY);
		 Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT" , SettingCategory.CURRENCY);
		 Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2" , SettingCategory.CURRENCY);
		 Setting thousandsPointType = new Setting("THOUSAND_POINT_TYPE", "COMMA" , SettingCategory.CURRENCY);
		 Setting currencySymbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		 repo.saveAll(List.of(currencyId, symbol,decimalPointType, 
				 currencySymbolPosition, decimalDigits, thousandsPointType));
		 Iterable<Setting> iterable = repo.findAll();
//		 
		 Setting orderConfirm = new Setting("ORDER_CONFIRMATION_SUBJECT","Verify", SettingCategory.MAIL_TEMPLATES);

		 Setting orderConfirmContent = new Setting("ORDER_CONFIRMATION_CONTENT","Verify", SettingCategory.MAIL_TEMPLATES);
		 assertThat(iterable).size().isGreaterThan(0);
//		 repo.save(currency);
	}
	@Test
	public void testCreateGerenalSetting() {
		 Setting siteName = new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		 Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		 Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Ecommerce Ltd.", SettingCategory.GENERAL);
		 Setting currencySymbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "after", SettingCategory.CURRENCY);
		 repo.save(currencySymbolPosition);
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out::println);
	}
	}
